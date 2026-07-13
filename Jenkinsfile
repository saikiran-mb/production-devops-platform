pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE = "saikiran798/deploytracker"
        DOCKER_CREDENTIALS = "dockerhub-creds"
        SONARQUBE_ENV = "SonarQube"
        MAVEN_OPTS = "-Djava.net.preferIPv4Stack=true"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('backend') {
                    sh '''
                        mvn \
                          -Djava.net.preferIPv4Stack=true \
                          clean package \
                          -DskipTests
                    '''
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('backend') {
                    withSonarQubeEnv("${SONARQUBE_ENV}") {
                        sh '''
                            mvn \
                              -Djava.net.preferIPv4Stack=true \
                              sonar:sonar \
                              -Dsonar.projectKey=deploytracker \
                              -Dsonar.projectName=DeployTracker
                        '''
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh """
                    docker build \
                        -t ${DOCKER_IMAGE}:${BUILD_NUMBER} \
                        -t ${DOCKER_IMAGE}:latest \
                        -f backend/Dockerfile \
                        backend
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                        docker push ${DOCKER_IMAGE}:latest
                        docker logout
                    '''
                }
            }
        }

        stage('Notify DeployTracker') {
            steps {
                sh """
                curl -X POST http://host.docker.internal:8080/api/deployments \
                  -H "Content-Type: application/json" \
                  -d '{
                    "serviceName":"deploytracker",
                    "version":"${BUILD_NUMBER}",
                    "status":"SUCCESS"
                  }'
                """
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully'
        }

        failure {
            echo '❌ Pipeline failed'
        }

        always {
            cleanWs()
        }
    }
}
