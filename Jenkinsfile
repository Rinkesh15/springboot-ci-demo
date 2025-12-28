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

        // ---------------- OLD PROJECT ----------------
        stage('Build & Test - OLD App') {
            steps {
                echo 'Running build for OLD Java application'
                dir('springboot-ci-demo') {
                    bat 'mvn clean test'
                }
            }
        }

        // ---------------- NEW PROJECT ----------------
        stage('Build & Test - Spring Boot CI Demo V1') {
            steps {
                echo 'Running build for NEW Spring Boot CI Demo V1'
                dir('springboot-ci-demo-v1/springboot-ci-demo-v1') {
                    bat 'mvn clean test'
                }
            }
        }

        stage('Static Analysis - PMD (Non-Blocking)') {
            steps {
                echo 'Running PMD (non-blocking) on NEW app'
                dir('springboot-ci-demo-v1/springboot-ci-demo-v1') {
                    bat '''
                        mvn pmd:pmd || echo "PMD failed or not configured – continuing pipeline"
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

        stage('Deploy QA') {
            when {
                branch 'qa'
            }
            steps {
                echo 'Deploying QA (POC placeholder)'
            }
        }

        stage('Deploy PROD') {
            when {
                branch 'master'
            }
            steps {
                echo 'Deploying PROD (POC placeholder)'
            }
        }
    }

    post {
        always {
            echo 'Publishing reports for NEW app'

            archiveArtifacts artifacts: 'springboot-ci-demo-v1/springboot-ci-demo-v1/**/target/surefire-reports/*.xml, springboot-ci-demo-v1/springboot-ci-demo-v1/**/target/pmd.xml',
                             allowEmptyArchive: true

            junit 'springboot-ci-demo-v1/springboot-ci-demo-v1/**/target/surefire-reports/*.xml'

            recordIssues(
                tools: [pmdParser(pattern: 'springboot-ci-demo-v1/springboot-ci-demo-v1/**/target/pmd.xml')],
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
