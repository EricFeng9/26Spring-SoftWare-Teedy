// practice10: 这个流水线用于完成 Docker 镜像构建、推送到 Docker Hub、并启动三个 Teedy 容器。
pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        // practice10: Docker Hub 仓库名，Jenkins 会把 Teedy 镜像推送到这个仓库。
        DOCKER_IMAGE = 'lekge/teedy-practice10'
        // practice10: 这个 ID 需要和 Jenkins Credentials 里保存的 Docker Hub 凭据 ID 一致。
        DOCKER_HUB_CREDENTIALS = 'dockerhub_credentials'
        // practice10: 使用 Jenkins build number 作为镜像版本，方便现场展示每次构建的结果。
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        // practice10: Teedy 容器内部监听 8080，lab 要求宿主机开放 8082、8083、8084。
        TEEDY_PORTS = '8082 8083 8084'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Package') {
            steps {
                // practice10: 先打包生成 docs-web/target/docs-web-*.war，Dockerfile 会把这个 WAR 放进镜像。
                sh 'mvn -B -Dmaven.test.skip=true clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                // practice10: 使用项目根目录 Dockerfile 构建 Teedy 镜像，不依赖额外 Docker Pipeline 插件。
                sh 'docker build -t "${DOCKER_IMAGE}:${DOCKER_TAG}" -t "${DOCKER_IMAGE}:latest" .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: env.DOCKER_HUB_CREDENTIALS, usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                    // practice10: 用 Jenkins Credentials 登录 Docker Hub，避免把密码写进代码。
                    sh 'echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKER_HUB_USER" --password-stdin'
                    // practice10: 推送 build number 标签和 latest 标签，方便现场展示 Docker Hub 仓库。
                    sh 'docker push "${DOCKER_IMAGE}:${DOCKER_TAG}"'
                    sh 'docker push "${DOCKER_IMAGE}:latest"'
                }
            }
        }

        stage('Run Three Containers') {
            steps {
                script {
                    // practice10: 先清理同名旧容器，避免 Jenkins 重跑时因为容器名重复而失败。
                    env.TEEDY_PORTS.split().each { port ->
                        sh "docker stop teedy-practice10-${port} || true"
                        sh "docker rm teedy-practice10-${port} || true"
                        sh "docker run -d --name teedy-practice10-${port} -p ${port}:8080 ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}"
                    }
                    // practice10: 输出三个容器状态，现场 Evaluation 可以直接截图或展示这段 Jenkins 日志。
                    sh 'docker ps --filter "name=teedy-practice10"'
                }
            }
        }
    }
}
