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
                sh 'mvn pmd:check pmd:pmd'
            }
        }

        stage('JaCoCo') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        stage('Site') {
            steps {
                sh 'mvn site'
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
