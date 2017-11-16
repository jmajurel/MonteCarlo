pipeline {
  agent { docker 'hseeberger/scala-sbt'}
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

