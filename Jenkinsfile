pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Dhanush-S14/devops_proj.git'
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
                sh 'mvn sonar:sonar -Dsonar.projectKey=library-management-system -Dsonar.host.url=http://localhost:9000 -Dsonar.token=<squ_token here>'
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


