pipeline {
	agent any
	stages {
		stage("Build") {
			steps {
				echo "Build app"
				mvn build
			}
		}
		stage("Test") {
			steps {
				echo "Testing app"
				mvn test
			}
		}
	}
}