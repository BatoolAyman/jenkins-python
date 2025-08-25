pipeline {
    agent any

    parameters {
        string(name: 'ENV', defaultValue: 'dev', description: 'اختر البيئة')
        booleanParam(name: 'RUN_TESTS', defaultValue: true, description: 'هل تشغل الاختبارات؟')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Environment: ${params.ENV}"
            }
        }

        stage('Setup Python') {
            steps {
                bat 'python --version'
                bat 'pip install --upgrade pip'
                bat 'pip install -r requirements.txt'
                echo "Python setup completed"
            }
        }

        stage('Run Tests') {
            when { expression { params.RUN_TESTS } }
            steps {
                bat 'pytest tests/'
            }
        }

        stage('Parallel Tasks') {
            parallel {
                stage('Task 1') {
                    steps {
                        bat 'echo Running Task 1 in parallel'
                    }
                }
                stage('Task 2') {
                    steps {
                        bat 'echo Running Task 2 in parallel'
                    }
                }
            }
        }

        stage('Finish') {
            steps {
                echo "Pipeline finished successfully ✅"
            }
        }
    }

    post {
        success {
            echo "Notification: Build SUCCESS"
        }
        failure {
            echo "Notification: Build FAILURE"
        }
    }
}
