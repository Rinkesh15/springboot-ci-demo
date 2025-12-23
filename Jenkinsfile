pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Static Analysis - PMD') {
            steps {
                bat 'mvn pmd:pmd'
            }
        }

        stage('Deploy DEV') {
            when {
                branch 'dev'
            }
            steps {
                echo 'Deploying to DEV environment'
                // bat 'mvn deploy -Pdev'
            }
        }

        stage('Deploy QA') {
            when {
                branch 'qa'
            }
            steps {
                echo 'Deploying to QA environment'
                // bat 'mvn deploy -Pqa'
            }
        }

        stage('Deploy PROD') {
            when {
                branch 'master'
            }
            steps {
                echo 'Deploying to PROD environment'
                // bat 'mvn deploy -Pprod'
            }
        }
    }

    post {
        success {
            echo 'Pipeline SUCCESS'
        }
        failure {
            echo 'Pipeline FAILED'
        }
    }
}
