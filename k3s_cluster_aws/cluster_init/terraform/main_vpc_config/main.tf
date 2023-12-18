terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~>5"
    }
    random = {
      source  = "hashicorp/random"
      version = "~>3.5"
    }
  }
  
  resource "aws_s3_bucket" "petprojects_bucket" {
    bucket = "terraform-state-petprojects"
    acl    = "private"

    versioning {
      enabled = true
    }

    logging {
      target_bucket = aws_s3_bucket.petprojects_bucket.bucket  # Corrected this line
      target_prefix = "logs/"
    }

    tags = {
      Terraform   = "true"
      Environment = "petprojects"
    }
  }

  backend "s3" {
    bucket = "terraform-state-petprojects"
    key    = "k3s_main_vpc/infra_setup.tfstate"
    region = "us-east-1"
  }
  
  required_version = ">= 1.3"
}

provider "aws" {
  region = "us-east-1"
}
