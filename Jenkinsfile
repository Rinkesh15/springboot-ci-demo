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
                    echo 'Publishing JUnit Test Results'
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Static Analysis - PMD') {
            steps {
                // IMPORTANT: pmd:site generates target/site/pmd.html
                bat 'mvn pmd:site'
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
        always {
            echo 'Archiving PMD Report'
            archiveArtifacts artifacts: 'target/site/pmd.html', allowEmptyArchive: true
        }

        success {
            echo 'Pipeline SUCCESS'
        }

        failure {
            echo 'Pipeline FAILED'
        }
    }
}
