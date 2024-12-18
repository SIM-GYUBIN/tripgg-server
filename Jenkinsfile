pipeline {
    agent any

    environment {
        APP_NAME = 'jenkins-test'
        DOCKER_IMAGE = 'jenkins-test:latest'
        GRADLE_OPTS = '-Dorg.gradle.jvmargs="-Xmx256m"'  // Gradle 메모리 제한
    }

    stages {
        stage('Prepare Environment') {
            steps {
                sh '''
                    rm -rf src/main/resources
                    mkdir -p src/main/resources
                    chmod 777 src/main/resources

                    # Docker 캐시 정리
                    docker system prune -f
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
                    '''
                }
            }
        }

        stage('Build') {
            steps {
                sh '''
                    chmod +x gradlew
                    # gradle 빌드 시 테스트 스킵 (-x test)
                    # 메모리 제한 (256m)
                    # 데몬 비활성화 (--no-daemon)
                    ./gradlew clean build -x test --no-daemon --max-workers 2 -Dorg.gradle.jvmargs="-Xmx256m"
                '''
            }
        }

        stage('Docker Build & Deploy') {
            steps {
                script {
                    sh 'docker rm -f ${APP_NAME} || true'
                    sh 'docker rmi ${DOCKER_IMAGE} || true'

                    sh 'docker build --memory=512m --memory-swap=512m -t ${DOCKER_IMAGE} .'

                    sh '''
                        docker run -d \
                            --name ${APP_NAME} \
                            --restart unless-stopped \
                            --memory=512m \
                            --memory-swap=512m \
                            -p 8080:8080 \
                            ${DOCKER_IMAGE}
                    '''
                }
            }
        }
    }
}