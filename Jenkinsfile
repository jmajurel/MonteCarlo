pipeline {
  agent { docker 'scala-sbt' }
  stages {
    stage('build') {
      steps {
        sh 'sbt build'
      }
    }
  }
}

