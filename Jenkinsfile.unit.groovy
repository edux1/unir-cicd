pipeline {
    agent {
        label 'docker'
    }

    stages {
        stage('Source') {
            steps {
                git 'https://github.com/edux1/unir-cicd.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        stage('Unit tests') {
            steps {
                sh 'make test-unit'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
        stage('API tests') {
            steps {
                sh 'make test-api'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
        stage('E2E tests') {
            steps {
                sh 'make test-e2e'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
    }
    post {
        success {
            junit 'results/*_result.xml'
            cleanWs()
        }
        failure {
            emailext subject: "Pipeline error", body: "The Pipeline has failed", to: "eduardo.pinto988@comunidadunir.net"
            cleanWs()
        }
    }
}