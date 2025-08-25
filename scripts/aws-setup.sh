#!/bin/bash

# AWS EC2 Setup Script for Leonardo Backend
# This script sets up the EC2 instance for running the Spring Boot application

set -e

echo "🚀 Starting AWS EC2 setup for Leonardo Backend..."

# Update system packages
echo "📦 Updating system packages..."
sudo yum update -y

# Install Java 17 (Amazon Corretto)
echo "☕ Installing Java 17..."
sudo yum install -y java-17-amazon-corretto-headless

# Verify Java installation
echo "✅ Java version:"
java -version

# Install necessary tools
echo "🔧 Installing additional tools..."
sudo yum install -y wget curl htop

# Create application user
echo "👤 Creating application user..."
sudo useradd -m -s /bin/bash leonardo
sudo mkdir -p /opt/leonardo
sudo chown leonardo:leonardo /opt/leonardo

# Create application directories
echo "📁 Creating application directories..."
sudo mkdir -p /opt/leonardo/logs
sudo mkdir -p /opt/leonardo/config
sudo chown -R leonardo:leonardo /opt/leonardo

# Configure environment variables
echo "🔐 Configuring environment variables..."
sudo tee /etc/environment.d/leonardo-backend.conf > /dev/null <<EOF
# Leonardo Backend Environment Variables
# IMPORTANT: Update these values with your actual configuration

# API Security Configuration
API_KEY=REPLACE_WITH_YOUR_API_KEY
API_SECURITY_ENABLED=true

# Database Configuration
DB_URL=jdbc:mysql://YOUR_RDS_ENDPOINT.region.rds.amazonaws.com:3306/leonardo_senasoft?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=leonardo_user
DB_PASSWORD=REPLACE_WITH_SECURE_PASSWORD

# Application Configuration
SPRING_PROFILES_ACTIVE=aws
JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
EOF

# Also add to leonardo user's profile
sudo tee /home/leonardo/.bashrc >> /dev/null <<EOF

# Leonardo Backend Environment Variables
export API_KEY=REPLACE_WITH_YOUR_API_KEY
export API_SECURITY_ENABLED=true
export DB_URL=jdbc:mysql://YOUR_RDS_ENDPOINT.region.rds.amazonaws.com:3306/leonardo_senasoft?useSSL=true&requireSSL=true&serverTimezone=UTC
export DB_USERNAME=leonardo_user
export DB_PASSWORD=REPLACE_WITH_SECURE_PASSWORD
export SPRING_PROFILES_ACTIVE=aws
export JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
EOF

sudo chown leonardo:leonardo /home/leonardo/.bashrc



# Install and configure systemd service
echo "🔧 Creating systemd service..."
sudo tee /etc/systemd/system/leonardo-backend.service > /dev/null <<EOF
[Unit]
Description=Leonardo Backend Spring Boot Application
After=network.target

[Service]
Type=simple
User=leonardo
Group=leonardo
WorkingDirectory=/opt/leonardo
ExecStart=/usr/bin/java -Xmx768m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dspring.profiles.active=aws -jar /opt/leonardo/leonardo-backend.jar
ExecStop=/bin/kill -15 \$MAINPID
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=leonardo-backend

Environment=JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
Environment=API_KEY=\${API_KEY}
Environment=API_SECURITY_ENABLED=\${API_SECURITY_ENABLED}
Environment=DB_URL=\${DB_URL}
Environment=DB_USERNAME=\${DB_USERNAME}
Environment=DB_PASSWORD=\${DB_PASSWORD}
Environment=SPRING_PROFILES_ACTIVE=\${SPRING_PROFILES_ACTIVE}

[Install]
WantedBy=multi-user.target
EOF

# Configure firewall (if firewalld is running)
echo "🔥 Configuring firewall..."
if sudo systemctl is-active --quiet firewalld; then
    sudo firewall-cmd --permanent --add-port=8080/tcp
    sudo firewall-cmd --reload
    echo "✅ Firewall configured to allow port 8080"
else
    echo "ℹ️  Firewalld not active, skipping firewall configuration"
fi

# Configure logrotate for application logs
echo "📋 Configuring log rotation..."
sudo tee /etc/logrotate.d/leonardo-backend > /dev/null <<EOF
/opt/leonardo/logs/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 leonardo leonardo
    postrotate
        systemctl reload leonardo-backend > /dev/null 2>&1 || true
    endscript
}
EOF

# Enable systemd service
echo "🔧 Enabling systemd service..."
sudo systemctl daemon-reload
sudo systemctl enable leonardo-backend

# Set up CloudWatch agent (optional, for monitoring)
echo "📊 Installing CloudWatch agent..."
wget https://s3.amazonaws.com/amazoncloudwatch-agent/amazon_linux/amd64/latest/amazon-cloudwatch-agent.rpm
sudo rpm -U ./amazon-cloudwatch-agent.rpm
rm -f ./amazon-cloudwatch-agent.rpm

# Create CloudWatch agent configuration
sudo tee /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json > /dev/null <<EOF
{
    "logs": {
        "logs_collected": {
            "files": {
                "collect_list": [
                    {
                        "file_path": "/opt/leonardo/logs/application.log",
                        "log_group_name": "/aws/ec2/leonardo-backend",
                        "log_stream_name": "{instance_id}/application.log"
                    }
                ]
            }
        }
    },
    "metrics": {
        "namespace": "Leonardo/Backend",
        "metrics_collected": {
            "cpu": {
                "measurement": ["cpu_usage_idle", "cpu_usage_iowait", "cpu_usage_user", "cpu_usage_system"],
                "metrics_collection_interval": 300
            },
            "disk": {
                "measurement": ["used_percent"],
                "metrics_collection_interval": 300,
                "resources": ["*"]
            },
            "mem": {
                "measurement": ["mem_used_percent"],
                "metrics_collection_interval": 300
            }
        }
    }
}
EOF

echo "✅ AWS EC2 setup completed!"
echo ""
echo "📋 Next steps:"
echo "1. Update environment variables in /etc/environment.d/leonardo-backend.conf:"
echo "   - Set your actual API_KEY"
echo "   - Update DB_URL with your RDS endpoint"
echo "   - Set your secure DB_PASSWORD"
echo "2. Upload the leonardo-backend.jar file to /opt/leonardo/"
echo "3. Reload environment variables: source /etc/environment.d/leonardo-backend.conf"
echo "4. Start the service: sudo systemctl start leonardo-backend"
echo "5. Check status: sudo systemctl status leonardo-backend"
echo "6. View logs: journalctl -u leonardo-backend -f"
echo ""
echo "🔐 Security Notes:"
echo "- API keys are stored in /etc/environment.d/ for system-wide access"
echo "- Database passwords should be strong and unique"
echo "- Consider using AWS Systems Manager Parameter Store for production secrets"
echo ""
echo "🔗 Application will be available at: http://your-ec2-public-ip:8080"
