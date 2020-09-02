pipeline {
    agent any
    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk11'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install'
                pushToCloudFoundry(
                  target: 'https://api.cap.explore.suse.dev',
                  organization: 'mircea_harapu_gmail_com',
                  cloudSpace: 'dev',
                  credentialsId: 'mircea.harapu@gmail.com'
                )
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
    }
}