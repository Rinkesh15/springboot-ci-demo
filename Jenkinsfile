pipeline {
    agent any

    options {
        durabilityHint('MAX_SURVIVABILITY')
        disableConcurrentBuilds()
    }

    tools {
        jdk 'JDK21'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'

        BASE_DIR = "/opt/springboot"
        APP_NAME = "springboot-ci-demo.jar"

        DEV_PORT = "8081"
        QA_PORT  = "8082"

        DEV_LOG = "${BASE_DIR}/dev/app.log"
        QA_LOG  = "${BASE_DIR}/qa/app.log"
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
                sh './mvnw clean test'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: '**/surefire-reports/*.xml'
                }
            }
        }

        stage('PMD Analysis') {
            steps {
                sh './mvnw pmd:pmd'
            }
        }

        stage('Package Application') {
            when {
                anyOf {
                    branch 'dev'
                    branch 'qa'
                }
            }
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Deploy to DEV') {
            when { branch 'dev' }
            steps {
                sh '''
                mkdir -p ${BASE_DIR}/dev
                pkill -f ${DEV_PORT} || true
                cp target/*.jar ${BASE_DIR}/dev/${APP_NAME}
                nohup java -jar ${BASE_DIR}/dev/${APP_NAME} \
                --server.port=${DEV_PORT} \
                > ${DEV_LOG} 2>&1 &
                '''
            }
        }

        stage('Deploy to QA') {
            when { branch 'qa' }
            steps {
                sh '''
                mkdir -p ${BASE_DIR}/qa
                pkill -f ${QA_PORT} || true
                cp target/*.jar ${BASE_DIR}/qa/${APP_NAME}
                nohup java -jar ${BASE_DIR}/qa/${APP_NAME} \
                --server.port=${QA_PORT} \
                > ${QA_LOG} 2>&1 &
                '''
            }
        }
    }
}

