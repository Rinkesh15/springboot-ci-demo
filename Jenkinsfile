pipeline {
    agent any

    options {
        durabilityHint('MAX_SURVIVABILITY')
        disableConcurrentBuilds()
    }

    environment {
        APP_NAME = "springboot-ci-demo"
        BASE_DIR = "/opt/springboot"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Test') {
            steps {
                sh './mvnw clean test'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('PMD Analysis') {
            steps {
                sh './mvnw pmd:pmd'
            }
            post {
                always {
                    publishHTML([
                        reportDir: 'target/site',
                        reportFiles: 'pmd.html',
                        reportName: 'PMD Report'
                    ])
                }
            }
        }
    }
}
