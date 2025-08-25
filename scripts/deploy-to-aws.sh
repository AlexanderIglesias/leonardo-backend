#!/bin/bash

# Deployment Script for Leonardo Backend to AWS EC2
# This script builds the application and deploys it to EC2

set -e

# Configuration
EC2_HOST=""  # To be filled with actual EC2 public IP
EC2_USER="ec2-user"
KEY_FILE=""  # To be filled with path to SSH key file
APP_NAME="leonardo-backend"

# API Key for testing endpoints (required for security)
API_KEY="${API_KEY:-}"  # Can be set via environment variable

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Starting deployment to AWS EC2...${NC}"
echo ""
echo -e "${YELLOW}📋 IMPORTANT: API Key Required for Testing${NC}"
echo -e "${YELLOW}To test endpoints after deployment, you need to set an API key:${NC}"
echo -e "${YELLOW}1. Generate API key: ./scripts/generate-api-key.sh${NC}"
echo -e "${YELLOW}2. Set environment variable: export API_KEY='your_api_key_here'${NC}"
echo -e "${YELLOW}3. Or modify this script to set API_KEY directly${NC}"
echo ""

# Check if required variables are set
if [ -z "$EC2_HOST" ] || [ -z "$KEY_FILE" ]; then
    echo -e "${RED}❌ Error: Please set EC2_HOST and KEY_FILE variables in this script${NC}"
    exit 1
fi

# Check if API key is set for testing
if [ -z "$API_KEY" ]; then
    echo -e "${YELLOW}⚠️  Warning: API_KEY not set. Endpoint testing will be skipped.${NC}"
    echo -e "${YELLOW}   Set API_KEY to test endpoints after deployment.${NC}"
    SKIP_TESTING=true
else
    echo -e "${GREEN}✅ API_KEY is set. Endpoint testing will be performed.${NC}"
    SKIP_TESTING=false
fi
echo ""

# Check if SSH key file exists
if [ ! -f "$KEY_FILE" ]; then
    echo -e "${RED}❌ Error: SSH key file not found: $KEY_FILE${NC}"
    exit 1
fi

# Step 1: Build the application
echo -e "${YELLOW}📦 Building application...${NC}"
./mvnw clean package -DskipTests -Dspring.profiles.active=aws

# Check if JAR was built successfully
JAR_FILE="target/${APP_NAME}-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo -e "${RED}❌ Error: JAR file not found. Build may have failed.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Build completed successfully${NC}"

# Step 2: Stop the running application on EC2
echo -e "${YELLOW}🛑 Stopping application on EC2...${NC}"
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "sudo systemctl stop leonardo-backend || true"

# Step 3: Backup current application (if exists)
echo -e "${YELLOW}💾 Creating backup of current application...${NC}"
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "
    if [ -f /opt/leonardo/leonardo-backend.jar ]; then
        sudo cp /opt/leonardo/leonardo-backend.jar /opt/leonardo/leonardo-backend.jar.backup.\$(date +%Y%m%d_%H%M%S)
        echo 'Backup created'
    else
        echo 'No existing application to backup'
    fi
"

# Step 4: Upload new JAR file
echo -e "${YELLOW}📤 Uploading new application...${NC}"
scp -i "$KEY_FILE" "$JAR_FILE" "$EC2_USER@$EC2_HOST:/tmp/leonardo-backend.jar"

# Step 5: Move JAR to correct location and set permissions
echo -e "${YELLOW}📁 Setting up application files...${NC}"
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "
    sudo mv /tmp/leonardo-backend.jar /opt/leonardo/leonardo-backend.jar
    sudo chown leonardo:leonardo /opt/leonardo/leonardo-backend.jar
    sudo chmod 755 /opt/leonardo/leonardo-backend.jar
"

# Step 6: Update application configuration if needed
echo -e "${YELLOW}⚙️  Updating configuration...${NC}"
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "
    # Create application.properties for AWS profile if it doesn't exist
    if [ ! -f /opt/leonardo/config/application-aws.properties ]; then
        echo 'Creating AWS configuration...'
        sudo mkdir -p /opt/leonardo/config
        sudo chown leonardo:leonardo /opt/leonardo/config
    fi
"

# Step 7: Start the application
echo -e "${YELLOW}🚀 Starting application...${NC}"
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "
    sudo systemctl daemon-reload
    sudo systemctl start leonardo-backend
    sudo systemctl enable leonardo-backend
"

# Step 8: Wait for application to start and check status
echo -e "${YELLOW}⏳ Waiting for application to start...${NC}"
sleep 30

# Check if application is running
echo -e "${YELLOW}🔍 Checking application status...${NC}"
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "
    if sudo systemctl is-active --quiet leonardo-backend; then
        echo '✅ Application is running'
        echo '📊 Service status:'
        sudo systemctl status leonardo-backend --no-pager -l
    else
        echo '❌ Application failed to start'
        echo '📋 Recent logs:'
        sudo journalctl -u leonardo-backend --no-pager -l -n 50
        exit 1
    fi
"

# Step 9: Test application endpoints
echo -e "${YELLOW}🧪 Testing application endpoints...${NC}"

if [ "$SKIP_TESTING" = true ]; then
    echo -e "${YELLOW}⚠️  Skipping endpoint testing (API_KEY not set)${NC}"
    echo -e "${YELLOW}   Set API_KEY to test endpoints manually:${NC}"
    echo -e "${YELLOW}   curl -H 'X-API-Key: your_api_key' http://$EC2_HOST:8080/actuator/health${NC}"
else
    echo -e "${GREEN}🔑 Testing with API key...${NC}"
    
    if curl -f -s "http://$EC2_HOST:8080/actuator/health" > /dev/null; then
        echo -e "${GREEN}✅ Health check passed${NC}"
    else
        echo -e "${RED}❌ Health check failed${NC}"
    fi

    if curl -f -s -H "X-API-Key: $API_KEY" "http://$EC2_HOST:8080/api/v1/metrics/scalar" > /dev/null; then
        echo -e "${GREEN}✅ API endpoints accessible with authentication${NC}"
    else
        echo -e "${RED}❌ API endpoints authentication failed${NC}"
        echo -e "${YELLOW}   Check if API key is correct and application is running${NC}"
    fi
fi

echo -e "${GREEN}🎉 Deployment completed!${NC}"
echo ""
echo -e "${GREEN}📋 Application URLs:${NC}"
echo -e "🔗 Health Check: http://$EC2_HOST:8080/actuator/health"
echo -e "🔗 API Documentation: http://$EC2_HOST:8080/swagger-ui.html"
echo -e "🔗 Metrics API: http://$EC2_HOST:8080/api/v1/metrics/scalar"
echo ""
echo -e "${GREEN}📊 Useful commands:${NC}"
echo -e "📋 Check logs: ssh -i $KEY_FILE $EC2_USER@$EC2_HOST 'sudo journalctl -u leonardo-backend -f'"
echo -e "🔄 Restart service: ssh -i $KEY_FILE $EC2_USER@$EC2_HOST 'sudo systemctl restart leonardo-backend'"
echo -e "📊 Check status: ssh -i $KEY_FILE $EC2_USER@$EC2_HOST 'sudo systemctl status leonardo-backend'"
