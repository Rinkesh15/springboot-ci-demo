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

        // 🔥 DO NOT rely on Jenkins Global JDK
        // Explicit, verified Java path on EC2
        JAVA_HOME = "/usr/lib/jvm/java-21-amazon-corretto.x86_64"
        PATH = "${JAVA_HOME}/bin:/usr/bin:/bin"
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
                    set -e

                    echo "===== JAVA INFO ====="
                    echo "JAVA_HOME=$JAVA_HOME"
                    java -version

                    echo "===== MAVEN INFO ====="
                    mvn -version

                    echo "===== MAVEN CACHE ====="
                    mkdir -p $HOME/.m2

                    echo "===== BUILD & TEST ====="
                    mvn -Dmaven.repo.local=$HOME/.m2/repository clean test
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
            when {
                branch 'dev'
            }
            steps {
                sh '''
                    mvn -Dmaven.repo.local=$HOME/.m2/repository pmd:pmd site
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
                sh '''
                    mvn -Dmaven.repo.local=$HOME/.m2/repository package -DskipTests
                '''
            }
        }

        stage('Deploy DEV') {
            when {
                branch 'dev'
            }
            steps {
                sh '''
                    echo "===== DEPLOY DEV ====="

                    pkill -f "spring.profiles.active=dev" || true

                    mkdir -p ${BASE_DIR}/dev ${BASE_DIR}/logs

                    cp target/*.jar ${BASE_DIR}/dev/${APP_NAME}

                    nohup ${JAVA_HOME}/bin/java -jar ${BASE_DIR}/dev/${APP_NAME} \
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
                    echo "===== DEPLOY QA ====="

                    pkill -f "spring.profiles.active=qa" || true

                    mkdir -p ${BASE_DIR}/qa ${BASE_DIR}/logs

                    cp target/*.jar ${BASE_DIR}/qa/${APP_NAME}

                    nohup ${JAVA_HOME}/bin/java -jar ${BASE_DIR}/qa/${APP_NAME} \
                      --spring.profiles.active=qa \
                      --server.port=8082 \
                      > ${BASE_DIR}/logs/qa.log 2>&1 &
                '''
            }
        }
    }
}
