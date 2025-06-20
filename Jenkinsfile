pipeline {
    agent any
    stages {
        stage('Copy Files from devops_repo') {
            steps {
                sh '''
                    rm -rf *
                    cp -r /home/DhanushS/Documents/devops_proj/* .
                    sudo chown -R jenkins:jenkins .
                    sudo chmod -R u+rwX .
                '''
            }
        }
        stage('Cleanup') {
            steps {
                sh 'rm -f target/sonar/.sonar_lock'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Static Code Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.projectKey=library-management-system -Dsonar.host.url=http://localhost:9000 -Dsonar.token=squ_6a79e76db41eafbf3c7547dfb26a327ef3f88b81'
            }
        }
        stage('Dependency Scanning') {
            steps {
                sh './dependency-check/dependency-check/bin/dependency-check.sh --project library-management-system --scan . --disableCentral'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t library-management-system:latest .'
            }
        }
        stage('Ansible Deploy and Monitoring') {
            steps {
                sh 'ansible-playbook -i inventory.ini ansible_playbook.yml'
            }
        }
    }
}
