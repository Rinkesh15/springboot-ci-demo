pipeline {
    agent any

    options {
        durabilityHint('MAX_SURVIVABILITY')
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        APP_NAME = "springboot-ci-demo"
        JAR_NAME = "springboot-ci-demo.jar"

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
                    mvn -B pmd:pmd site
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

        stage('Package JAR') {
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

        stage('Build RPM (DEV)') {
            when { branch 'dev' }
            steps {
                sh '''
                    set -e
                    mkdir -p ~/rpmbuild/SOURCES
                    cp target/*.jar ~/rpmbuild/SOURCES/${JAR_NAME}

                    rpmbuild -ba ~/rpmbuild/SPECS/springboot-ci-demo.spec
                '''
            }
        }

        stage('Install RPM (DEV)') {
            when { branch 'dev' }
            steps {
                sh '''
                    set -e
                    sudo dnf remove -y springboot-ci-demo || true
                    sudo dnf install -y ~/rpmbuild/RPMS/noarch/springboot-ci-demo-*.rpm
                '''
            }
        }

        stage('Build RPM (QA)') {
            when { branch 'qa' }
            steps {
                sh '''
                    set -e
                    mkdir -p ~/rpmbuild/SOURCES
                    cp target/*.jar ~/rpmbuild/SOURCES/${JAR_NAME}

                    rpmbuild -ba ~/rpmbuild/SPECS/springboot-ci-demo.spec
                '''
            }
        }

        stage('Install RPM (QA)') {
            when { branch 'qa' }
            steps {
                sh '''
                    set -e
                    sudo dnf remove -y springboot-ci-demo || true
                    sudo dnf install -y ~/rpmbuild/RPMS/noarch/springboot-ci-demo-*.rpm
                '''
            }
        }
    }
}
