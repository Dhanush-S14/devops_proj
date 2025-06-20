pipeline {
    agent any

    stages {
        stage('Cleanup') {
            steps {
                sh 'rm -f /var/lib/jenkins/workspace/devops_proj/target/sonar/.sonar_lock'
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
                sh '/home/DhanushS/Documents/devops_proj/dependency-check/dependency-check/bin/dependency-check.sh --project library-management-system --scan . --disableCentral'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t library-management-system:latest .'
            }
        }

        stage('Ansible Deploy and Monitoring') {
            steps {
                sh 'ansible-playbook -i /home/DhanushS/Documents/devops_proj/inventory.ini /home/DhanushS/Documents/devops_proj/ansible_playbook.yml'
            }
        }
    }

    post {
        success {
            step([$class: 'GitHubCommitStatusSetter',
                contextSource: [$class: 'ManuallyEnteredCommitContextSource', context: 'Jenkins Build'],
                statusResultSource: [$class: 'ManuallyEnteredStatusResultSource', status: 'SUCCESS', message: 'Build succeeded']
            ])
        }
        failure {
            step([$class: 'GitHubCommitStatusSetter',
                contextSource: [$class: 'ManuallyEnteredCommitContextSource', context: 'Jenkins Build'],
                statusResultSource: [$class: 'ManuallyEnteredStatusResultSource', status: 'FAILURE', message: 'Build failed']
            ])
        }
    }
}
