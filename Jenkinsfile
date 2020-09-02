pipeline {
	agent any
	stages {
		stage("Build") {
			steps {
				echo "Build app"
				sh 'mvn build'
			}
		}
		stage("Test") {
			steps {
				echo "Testing app"
				sh 'mvn test'
			}
		}
	}
}