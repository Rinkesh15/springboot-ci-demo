pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven-3.9'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code'
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Running Maven Build & Unit Tests'
                bat 'mvn clean test'
            }
        }

        stage('Static Analysis - PMD (Non-Blocking)') {
            steps {
                echo 'Running PMD (non-blocking)'
                bat '''
                    mvn pmd:pmd || echo "PMD failed or not configured – continuing pipeline"
                '''
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
            echo 'Publishing Test & Analysis Reports'

            archiveArtifacts artifacts: '**/target/surefire-reports/*.xml,**/target/pmd.xml',
                             allowEmptyArchive: true

            junit '**/target/surefire-reports/*.xml'

            recordIssues(
                tools: [pmdParser(pattern: '**/target/pmd.xml')],
                enabledForFailure: true
            )
        }

        success {
            echo '✅ Pipeline SUCCESS'
        }

        failure {
            echo '❌ Pipeline FAILED'
        }
    }
}
