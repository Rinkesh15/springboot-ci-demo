pipeline {
    agent any

    options {
        durabilityHint('MAX_SURVIVABILITY')
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        BASE_DIR = "/opt/springboot"
        APP_NAME = "springboot-ci-demo.jar"

        JAVA_HOME = "/usr/lib/jvm/java-21-amazon-corretto.x86_64"
        PATH = "${JAVA_HOME}/bin:/usr/bin:/bin"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                sh '''
                    set -e
                    java -version
                    mvn -version
                    set -x
                    mvn clean test
                '''
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('PMD Code Quality (Report Only)') {
            when {
                anyOf {
                    branch 'dev'
                    branch 'qa'
                    changeRequest()
                }
            }
            steps {
                sh '''
                      mvn -B pmd:pmd
                   '''
            }
            post {
                always {
                    publishHTML(target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site',
                        reportFiles: 'pmd.html',
                        reportName: 'PMD Report'
                    ])
                }
            }
        }

        stage('Package') {
            when {
                anyOf {
                    branch 'dev'
                    branch 'qa'
                }
            }
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Deploy DEV') {
            when { branch 'dev' }
            steps {
                sh '''
                    pkill -f "spring.profiles.active=dev" || true
                    mkdir -p ${BASE_DIR}/dev ${BASE_DIR}/logs
                    cp target/*.jar ${BASE_DIR}/dev/${APP_NAME}

                    nohup java -jar ${BASE_DIR}/dev/${APP_NAME} \
                      --spring.profiles.active=dev \
                      --server.port=8081 \
                      > ${BASE_DIR}/logs/dev.log 2>&1 &
                '''
            }
        }

        stage('Deploy QA') {
            when { branch 'qa' }
            steps {
                sh '''
                    pkill -f "spring.profiles.active=qa" || true
                    mkdir -p ${BASE_DIR}/qa ${BASE_DIR}/logs
                    cp target/*.jar ${BASE_DIR}/qa/${APP_NAME}

                    nohup java -jar ${BASE_DIR}/qa/${APP_NAME} \
                      --spring.profiles.active=qa \
                      --server.port=8082 \
                      > ${BASE_DIR}/logs/qa.log 2>&1 &
                '''
            }
        }
    }
}
