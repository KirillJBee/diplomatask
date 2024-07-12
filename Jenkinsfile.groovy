pipeline {

    agent { 
            label 'NAME_YOUR_JENKINS_NODE'
          }

    environment {
        AWS_ACCESS_KEY_ID     = credentials('aws-access-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('aws-secret-access-key')
        AWS_DEFAULT_REGION    = 'YOUR_AWS_REGION'
        GIT_TOKEN = credentials('YOUR_GIT_TOKEN')
        DOCKERHUB_CREDENTIALS = credentials('YOUR_DOCKERHUB_CREDENTIALS')
        NAME_IMAGE = 'NAME_YOUR_IMAGE'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: 'development']],
                    userRemoteConfigs: [[credentialsId:'GIT_TOKEN',
                    url: 'https://YOUR_GITHUB']])
            }
        }

        stage('Build image webpage') {
            steps {
                sh 'docker build -t ${NAME_IMAGE} .'    
            }
        }

        stage('Push image webpage') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh 'docker push ${NAME_IMAGE}'
                sh 'docker rmi ${NAME_IMAGE}'
            }
        }

        stage('Terraform init') {
            steps {
                sh 'terraform -chdir=terraform init'
            }
        }

        stage('Terraform plan') {
            steps {
                sh 'terraform -chdir=terraform plan -out tfplan'
                sh 'terraform -chdir=terraform show -no-color tfplan > tfplan.txt'
            }
        }

        stage('Apply') {
            steps {
                sh 'terraform -chdir=terraform apply -input=false tfplan'
                //sh 'terraform -chdir=terraform destroy -auto-approve'
            }
        }

        stage('Deploy webimage') {

            steps {
                script {
                    withCredentials([
                        file(credentialsId: 'DH_vaultkey', variable: 'ANSIBLE_VAULT_KEY')
                        ]) {
                        sh 'ansible-playbook -i ./terraform/dynamic_inventory.ini --vault-password-file $ANSIBLE_VAULT_KEY ./ansible/playbook.yml'
                    }
                }         
            }
        }
    }
}