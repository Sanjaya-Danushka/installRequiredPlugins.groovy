pipeline {
    agent any

    stages {
        stage('Install .NET 7 SDK') {
            steps {
                sh '''
                    sudo pacman -Sy --noconfirm dotnet-sdk
                    dotnet --version
                '''
            }
        }

        stage('Install Docker Engine CE') {
            steps {
                sh '''
                    sudo pacman -Sy --noconfirm docker
                    sudo systemctl enable --now docker
                    docker --version
                '''
            }
        }

        stage('Add Jenkins User to Docker Group') {
            steps {
                sh '''
                    # Create docker group if not exists
                    if ! getent group docker; then
                        sudo groupadd docker
                    fi

                    # Add Jenkins user to docker group
                    sudo usermod -aG docker jenkins

                    # Restart Docker to apply group permissions
                    sudo systemctl restart docker
                '''
            }
        }
    }
}
