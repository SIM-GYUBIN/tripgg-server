pipeline {
    agent any

    stages {
        stage('Secrets Setup') {
            steps {
                withCredentials([
                    file(credentialsId: 'prod-yaml', variable: 'prodFile'),
                    file(credentialsId: 'secret-yaml', variable: 'secretFile')
                ]) {
                    sh '''
                        cp $prodFile src/main/resources/application-prod.yml
                        cp $secretFile src/main/resources/application-secret.yml
                    '''
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh 'docker build -t jenkins-test .'
                    sh 'docker rm -f jenkins-test || true'  // 컨테이너가 없을 경우 에러 방지
                    sh 'docker run -d --name jenkins-test -p 8080:8080 jenkins-test'
                }
            }
        }
    }  // stages 끝
}  // pipeline 끝