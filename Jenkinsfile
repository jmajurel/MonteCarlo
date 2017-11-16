pipeline {
  agent { docker 'hseeberger/scala-sbt'}
  stages {
    stage('compile') {
      steps {
        sh 'sbt compile'
      }
    }
    stage('test') {
      steps {
        sh 'sbt test'
      }
    }
    stage('package') {
      steps {
        sh 'sbt package'
      }
    }
  }
}

