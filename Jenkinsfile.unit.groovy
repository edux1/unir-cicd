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
            mail subject: "Pipeline unstalbe", body: "The execution #${env.BUILD_NUMBER} of the job ${env.JOB_NAME} finished its execution with the status UNSTABLE.", to: "edu.pm97@gmail.com"
            cleanWs()
        }
        failure {
            mail subject: "Pipeline error", body: "The execution #${env.BUILD_NUMBER} of the job ${env.JOB_NAME} finished its execution with the status FAILURE.", to: "edu.pm97@gmail.com"
            cleanWs()
        }
    }
}