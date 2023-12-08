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

        stage('Install Ansible') {
            steps {
        sh '''
        sudo apt-get update
        '''
            }
        }
        
        stage('Terraform Plan') {
            steps {
                sh '''
                cd ./pacman_pipeline/TF
                echo "yes" | terraform init
                terraform plan -out=terraform.tfplan
                '''
            }
        }

        stage('Plan verification and user input') {
            steps {
                input(
                    message: 'proceed or abort?', 
                    ok: 'ok'
                )
            }
        }

        stage('Terraform Apply') {
            steps {
                sh '''
                cd ./pacman_pipeline/TF
                terraform apply terraform.tfplan
                '''
            }
        }

        stage('Get Terraform Outputs') {
            steps {
                sh '''
                cd ./pacman_pipeline/TF
                terraform output web-address-PacmanDocker > ../ansible/instance_ip.txt
                '''
            }
        }    

        stage('Run Ansible') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'docker_k', keyFileVariable: 'SSH_KEY')]) {
                    sh '''
                    sleep 180
                    cd ./pacman_pipeline/ansible
                    ansible-playbook -i instance_ip.txt playbook_pacman_docker.yaml -u ubuntu --private-key=$SSH_KEY -e 'ansible_ssh_common_args="-o StrictHostKeyChecking=no"'
                    '''
                }
            }
        } 
    }
}
