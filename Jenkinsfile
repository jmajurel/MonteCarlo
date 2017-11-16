pipeline {
  agent { docker 'maven:3-alpine' }
  stages {
    stage('build') {
      steps {
        sh 'sbt build'
      }
    }
  }
}

