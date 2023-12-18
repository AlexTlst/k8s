pipeline {
    agent any
    tools {
        terraform 'tf1.6'
    }

    stages {
        stage('Sparse Checkout') {
            steps {
                script {
                    checkout([$class: 'GitSCM', 
                              branches: [[name: 'main']],
                              doGenerateSubmoduleConfigurations: false,
                              extensions: [[
                                  $class: 'SparseCheckoutPaths', 
                                  sparseCheckoutPaths: [[path: 'k8S_AWS_infra/TF']]
                              ]],
                              userRemoteConfigs: [[
                                  credentialsId: 'access_to_git',
                                  url: 'https://github.com/AlexTlst/k8s.git'
                              ]]
                    ])
                }
            }
        }

        stage('Terraform Plan') {
            steps {
                sh '''
                cd ./k8S_AWS_infra/TF
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
                cd ./k8S_AWS_infra/TF
                terraform apply terraform.tfplan
                '''
            }
        }
    }
}
