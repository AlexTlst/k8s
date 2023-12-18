resource "aws_launch_template" "k3s_master" {
  name_prefix   = "k3s-master-"
  image_id      = "ami-0fc5d935ebf8bc3bc" # Ubuntu 22.04 latest
  instance_type = "t3.small"             # Update as necessary
  key_name      = "docker_k"       

  vpc_security_group_ids = [data.aws_security_group.k3s_sg.id]
  # user_data = base64encode(<<EOF
  #     #!/bin/bash
  #     # Install K3s server with predefined token
  #     curl -sfL https://get.k3s.io | sh -s - server --token u2Qw5PbXC887MMv85LeG
  #     EOF
  # )
  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "K3s_Master"
    }
  }
}
