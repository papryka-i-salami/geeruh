pipeline {
  agent {
    docker { image 'openjdk:17-jdk-slim' }
  }
  stages {
    stage('Config Test 2') {
      steps {
        sh 'java --version'
      }
    }
  }
}
