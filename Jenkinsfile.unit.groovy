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
        unstable {
            emailext subject: "Pipeline unstalbe", body: "The execution #${env.BUILD_NUMBER0} of the job ${env.JOB_NAME} finished its execution with the status UNSTABLE.", to: "eduardo.pinto988@comunidadunir.net"
            cleanWs()
        }
        failure {
            emailext subject: "Pipeline error", body: "The execution #${env.BUILD_NUMBER0} of the job ${env.JOB_NAME} finished its execution with the status FAILURE.", to: "eduardo.pinto988@comunidadunir.net"
            cleanWs()
        }
    }
}