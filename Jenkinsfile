pipeline {
    agent any

    tools {
        jdk 'JDK21'
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

        stage('Build & Test - Spring Boot CI Demo V1') {
            steps {
                echo 'Running build for Spring Boot CI Demo V1'
                dir('springboot-ci-demo-v1') {
                    bat 'mvn clean test'
                }
            }
        }

        stage('Static Analysis - PMD (Non-Blocking)') {
            steps {
                echo 'Running PMD (non-blocking)'
                dir('springboot-ci-demo-v1') {
                    bat '''
                        mvn clean test pmd:pmd site || echo "PMD failed – continuing pipeline"
                    '''
                }
            }
        }

        stage('Deploy DEV') {
            when {
                branch 'dev'
            }
            steps {
                echo 'Deploying DEV (POC placeholder)'
            }
        }
    }

    post {
        always {
            echo 'Publishing reports (non-blocking)'

            // ✅ JUnit reports
            junit testResults: '**/target/surefire-reports/*.xml',
                  allowEmptyResults: true

            // ✅ Archive PMD XML (for Jenkins UI)
            archiveArtifacts artifacts: '**/target/pmd.xml',
                             allowEmptyArchive: true

            // ✅ Archive PMD HTML (clickable in Jenkins UI)
            archiveArtifacts artifacts: '**/target/site/pmd.html',
                             allowEmptyArchive: true

            // ✅ Show PMD issues in Jenkins UI
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
	// Updated Jenkinsfile for AWS CI demo
        }
    }
}
