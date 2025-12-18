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
                bat 'java -version'
                bat 'mvn -version'
                bat 'mvn clean test'
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

    post {
        success {
            echo 'Pipeline completed successfully'
        }
        failure {
            echo 'Pipeline failed'
        }
    }
}
