pipeline {
    agent any
    tools {
        terraform 'tf1.6'
    }
    stages {
        stage('Clone Git repo') {
            steps {
                git(
                branch: 'main',
                url: 'https://github.com/AlexTlst/Pacman_db_docker_ec2.git',
                credentialsId: 'access_to_git'
                )
            }
        }
        stage('Terraform Initialize and Plan Destroy') {
            steps {
                sh '''
                cd ./pacman_pipeline/TF
                terraform init
                terraform plan -destroy -out=destroyplan.tfplan
                '''
            }
        }
        stage('Plan verification and user input for Destroy') {
            steps {
                input message: 'proceed or abort?', ok: 'ok'
            }
        }
        stage('Terraform Apply Destroy') {
            steps {
                sh '''
                cd ./pacman_pipeline/TF
                terraform apply destroyplan.tfplan
                '''
            }
        }
    }
}