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
            emailext (
                subject: "Pipeline unstalbe",
                body: "The execution of ${env.JOB_NAME} (#${env.BUILD_NUMBER}) SUCCEEDED",
                to: "edu.pm97@gmail.com"
            )
            cleanWs()
        }
        unstable {
            emailext (
                subject: "Pipeline unstable",
                body: "The execution of ${env.JOB_NAME} (#${env.BUILD_NUMBER}) was UNSTABLE",
                to: "edu.pm97@gmail.com"
            )
            cleanWs()
        }
        failure {
            emailext (
                subject: "Pipeline unstalbe",
                body: "The execution of ${env.JOB_NAME} (#${env.BUILD_NUMBER}) FAILED",
                to: "edu.pm97@gmail.com"
            )
            cleanWs()
        }
    }
}