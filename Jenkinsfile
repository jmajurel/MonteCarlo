pipeline {
  agent { 
    docker {
      image 'hseeberger/scala-sbt'
      args '-v /root/.m2:/root/.m2'
    }
  }
  stages {
    stage('Build') {
      steps {
        sh 'sbt compile'
      }
    }
    stage('Test') {
      steps {
        sh 'sbt test'
      }
      post {
        always {
          junit 'target/test-reports/*.xml'
	}
      }
    }
    stage('Deliver') {
      steps {
        sh 'sbt package'
      }
    }
  }
}

