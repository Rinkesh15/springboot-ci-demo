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
        // ❌ DO NOT set JAVA_HOME here
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                    echo "======================"
                    echo "Java Version"
                    echo "======================"
                    java -version

                    echo "======================"
                    echo "Maven Version"
                    echo "======================"
                    mvn -version

                    echo "======================"
                    echo "Running Tests"
                    echo "======================"
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

        stage('Code Quality Reports') {
            steps {
                sh '''
                    mvn pmd:pmd site
                '''
            }
            post {
                always {
                    publishHTML(target: [
                        allowMissing: false,
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
                sh '''
                    mvn package -DskipTests
                '''
            }
        }

        stage('Deploy DEV') {
            when {
                branch 'dev'
            }
            steps {
                sh '''
                    echo "Deploying DEV"

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
            when {
                branch 'qa'
            }
            steps {
                sh '''
                    echo "Deploying QA"

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
