pipeline {
    agent any

    environment {
        APP_NAME = 'jenkins-test'
        DOCKER_IMAGE = 'jenkins-test:latest'
    }

    stages {
        stage('Prepare Environment') {
            steps {
                sh '''
                    rm -rf src/main/resources
                    mkdir -p src/main/resources
                    chmod 777 src/main/resources
                '''
            }
        }

        stage('Secrets Setup') {
            steps {
                withCredentials([
                    file(credentialsId: 'prod-yaml', variable: 'prodFile'),
                    file(credentialsId: 'secret-yaml', variable: 'secretFile')
                ]) {
                    sh '''
                        cp "$prodFile" src/main/resources/application-prod.yml
                        cp "$secretFile" src/main/resources/application-secret.yml

                        chmod 644 src/main/resources/application-*.yml
                        ls -la src/main/resources/
                    '''
                }
            }
        }

        stage('Build') {
            steps {
                sh '''
                    chmod +x gradlew
                    ./gradlew clean build --no-daemon
                '''
            }
        }

        stage('Docker Build & Deploy') {
            steps {
                script {
                    sh 'docker rm -f ${APP_NAME} || true'

                    sh 'docker rmi ${DOCKER_IMAGE} || true'

                    sh 'docker build -t ${DOCKER_IMAGE} .'

                    sh '''
                        docker run -d \
                            --name ${APP_NAME} \
                            --restart unless-stopped \
                            -p 8080:8080 \
                            ${DOCKER_IMAGE}
                    '''
                }
            }
        }
    }
}