pipeline {
  agent { docker 'hseeberger/scala-sbt'}
  stages {
    stage('build') {
      steps {
        sh 'sbt build'
      }
    }
  }
}

