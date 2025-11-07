// Using temporary STS credentials and injecting Jenkins secrets.
pipeline {
    agent any
    
    // Environment configuration and secret injection
    environment {
        // --- AWS and Image Configuration Variables ---
        AWS_REGION = 'us-east-1'
        
        // FULL ECR URI 
        ECR_REGISTRY = '683273507962.dkr.ecr.us-east-1.amazonaws.com/flashcard-api' 
        
        ECR_REPOSITORY = 'flashcard-api'
        IMAGE_TAG = "${BUILD_NUMBER}"

        // --- Injection of AWS Temporary Credentials (STS) ---
        AWS_ACCESS_KEY_ID     = credentials('aws-access-key')
        AWS_SECRET_ACCESS_KEY = credentials('aws-secret-key')
        AWS_SESSION_TOKEN     = credentials('aws-session-token')

        // --- Injection of Database Credentials ---
        DB_URL      = credentials('db-url')
        DB_USER     = credentials('db-user')
        DB_PASSWORD = credentials('db-password')

        // --- EC2 Backend Connection (For SSH Deployment) ---
        // PUBLIC IP OF YOUR EC2 BACKEND
        EC2_HOST = '35.170.95.1' 
        EC2_USER = 'ec2-user'
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                echo "1. Git source code checkout."
                checkout scm
            }
        }

        stage('2. Build Docker Image') {
            steps {
                echo "2. Building the Docker image with tag ${IMAGE_TAG}"
                sh """
                    docker build -t ${ECR_REPOSITORY}:${IMAGE_TAG} .
                    docker tag ${ECR_REPOSITORY}:${IMAGE_TAG} ${ECR_REGISTRY}:${IMAGE_TAG}
                    docker tag ${ECR_REPOSITORY}:${IMAGE_TAG} ${ECR_REGISTRY}:latest
                """
            }
        }

        stage('3. ECR Login & Push') {
            steps {
                echo "3. Autenticando no ECR e enviando imagem..."
                // Authenticates using the injected STS credentials
                sh """
                    # Login
                    aws ecr get-login-password --region ${AWS_REGION} --output text \
                    | docker login \
                        --username AWS \
                        --password-stdin ${ECR_REGISTRY}

                    # Push
                    docker push ${ECR_REGISTRY}:${IMAGE_TAG}
                    docker push ${ECR_REGISTRY}:latest
                """
            }
        }

        stage('4. Deploy to Backend EC2') {
            steps {
                echo "4. Deploying via SSH on EC2 Backend (${EC2_HOST})."
                
              // Uses the SSH key configured in Jenkins 
                sshagent (credentials: ['ec2-backend-ssh']) {
                    sh """
                        # Command executed on EC2 Backend via SSH
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                            echo "Logging in to the ECR inside the EC2 Backend..."
                            
                            # EC2 Backend also needs login to pull the image
                            aws ecr get-login-password --region ${AWS_REGION} \
                                | docker login --username AWS --password-stdin ${ECR_REGISTRY}

                            echo "Stop and remove old container (flashcard)..."
                            docker stop flashcard || true
                            docker rm flashcard || true

                            echo "Pulling new image..."
                            docker pull ${ECR_REGISTRY}:latest

                            echo "Uploading new container with DB variables..."
                            # Passing DB credentials as environment variables
                            docker run -d \
                                --name flashcard \
                                -p 8080:8080 \
                                -e SPRING_DATASOURCE_URL="${DB_URL}" \
                                -e SPRING_DATASOURCE_USERNAME="${DB_USER}" \
                                -e SPRING_DATASOURCE_PASSWORD="${DB_PASSWORD}" \
                                ${ECR_REGISTRY}:latest
                            
                            echo "Cleaning old images..."
                            docker image prune -af
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deploy completed successfully! Check the API on port 8080."
        }
        failure {
            echo "❌ Pipeline failed! Check the logs of the failed stage to diagnose."
        }
        always {
            cleanWs()
        }
    }
}
