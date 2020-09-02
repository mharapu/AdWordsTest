pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'jdk8'
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
            }
        }

        stage ('Deploy') {
            steps {
                pushToCloudFoundry(
                                  target: 'https://api.cap.explore.suse.dev',
                                  organization: 'mircea_harapu_gmail_com',
                                  cloudSpace: 'dev',
                                  credentialsId: 'cf_mircea',
                                   manifestChoice: [
                                      value: 'jenkinsConfig',
                                      appName: 'adwords',
                                      memory: '64',
                                      instances: '2'
                                    ]
                                )
            }
        }
    }
}