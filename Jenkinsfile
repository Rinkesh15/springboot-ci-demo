pipeline {
    agent any

    options {
        durabilityHint('MAX_SURVIVABILITY')
        disableConcurrentBuilds()
    }

    environment {
        BASE_DIR = "/opt/springboot"
        APP_NAME = "springboot-camel.jar"
        JAVA_HOME = "/usr/lib/jvm/java-21-amazon-corretto"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
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
                    echo "Java Version Used:"
                    java -version
                    echo "Maven Version Used:"
                    mvn -version

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
                        reportFiles: 'surefire-report.html',
                        reportName: 'JUnit Test Report'
                    ])
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
                    echo "Deploying DEV on same EC2"

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
                    echo "Deploying QA on same EC2"

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
