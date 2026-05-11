// ##Practice9 : Practice 9 需要的 Jenkins pipeline，按要求准备 CI、测试结果、Artifacts 与 Site documentation 展示链路。
pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout / Clean') {
            steps {
                checkout scm
                sh 'mvn clean'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('PMD') {
            steps {
                // ##Practice9 : 多模块项目在单独执行 PMD 目标前先安装 reactor 产物，避免子模块去远端仓库错误解析本地 SNAPSHOT 依赖。
                sh 'mvn -DskipTests install pmd:check pmd:pmd'
            }
        }

        stage('JaCoCo') {
            steps {
                // ##Practice9 : 先安装本地 SNAPSHOT 产物，再生成聚合覆盖率报告，保证跨模块依赖可被解析。
                sh 'mvn -DskipTests install jacoco:report'
            }
        }

        stage('Site') {
            steps {
                // ##Practice9 : 先安装本地 SNAPSHOT 产物，再生成 site 文档，避免 site 阶段对子模块依赖解析失败。
                sh 'mvn -DskipTests install site'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/site/**,docs-core/target/**,docs-web-common/target/**,docs-web/target/**', allowEmptyArchive: true
        }
    }
}
