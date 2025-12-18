pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven-3.9'
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
        }

        stage('Static Analysis - PMD') {
            steps {
                bat 'mvn pmd:pmd'
            }
        }

        stage('Deploy DEV') {
            when {
                branch 'feature/*'
            }
            steps {
                echo 'Deploying application to DEV App'
                bat 'echo DEV deployment successful'
            }
        }

        stage('Deploy QA') {
            when {
                branch 'dev'
            }
            steps {
                echo 'Deploying application to QA App'
                bat 'echo QA deployment successful'
            }
        }

        stage('Deploy PROD') {
            when {
                branch 'master'
            }
            steps {
                echo 'Deploying application to PROD App'
                bat 'echo PROD deployment successful'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed'
        }
        success {
            echo 'Pipeline SUCCESS'
        }
        failure {
            echo 'Pipeline FAILED'
        }
    }
}
