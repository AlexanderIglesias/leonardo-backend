#!/bin/bash

# Complete AWS Deployment Preparation Script
# This script prepares everything needed for AWS deployment

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Leonardo Backend - AWS Deployment Preparation${NC}"
echo "=================================================="

# Step 1: Run pre-deployment checks
echo -e "\n${YELLOW}📋 Step 1: Running pre-deployment checks...${NC}"
if ./scripts/pre-deployment-check.sh; then
    echo -e "${GREEN}✅ Pre-deployment checks passed${NC}"
else
    echo -e "${RED}❌ Pre-deployment checks failed${NC}"
    exit 1
fi

# Step 2: Build optimized JAR
echo -e "\n${YELLOW}📦 Step 2: Building production JAR...${NC}"
./mvnw clean package -DskipTests -Dspring.profiles.active=aws
echo -e "${GREEN}✅ Production JAR built successfully${NC}"

# Step 3: Create deployment package
echo -e "\n${YELLOW}📁 Step 3: Creating deployment package...${NC}"
DEPLOY_DIR="deployment-package"
rm -rf "$DEPLOY_DIR"
mkdir -p "$DEPLOY_DIR"

# Copy necessary files
cp target/leonardo-backend-0.0.1-SNAPSHOT.jar "$DEPLOY_DIR/"
cp scripts/aws-setup.sh "$DEPLOY_DIR/"
cp scripts/deploy-to-aws.sh "$DEPLOY_DIR/"
cp docs/AWS_DEPLOYMENT_GUIDE.md "$DEPLOY_DIR/"

# Create a simple README for the deployment package
cat > "$DEPLOY_DIR/README.md" << 'EOF'
# Leonardo Backend - AWS Deployment Package

This package contains everything needed to deploy the Leonardo Backend to AWS.

## Contents

- `leonardo-backend-0.0.1-SNAPSHOT.jar` - Production JAR file
- `aws-setup.sh` - EC2 instance setup script
- `deploy-to-aws.sh` - Deployment script
- `AWS_DEPLOYMENT_GUIDE.md` - Complete deployment guide

## Quick Start

1. Follow the AWS_DEPLOYMENT_GUIDE.md for step-by-step instructions
2. Update deploy-to-aws.sh with your EC2 IP and SSH key
3. Run the deployment script

## Support

Refer to the AWS_DEPLOYMENT_GUIDE.md for troubleshooting and detailed instructions.
EOF

echo -e "${GREEN}✅ Deployment package created in $DEPLOY_DIR/${NC}"

# Step 4: Generate AWS resource estimation
echo -e "\n${YELLOW}💰 Step 4: AWS Cost Estimation...${NC}"
cat << 'EOF'

AWS Free Tier Resource Usage:
==========================

🔸 EC2 Instance (t2.micro)
  - vCPUs: 1
  - Memory: 1 GB RAM
  - Storage: 8 GB EBS (gp2)
  - Free Tier: 750 hours/month
  - Estimated Cost: $0.00/month

🔸 RDS MySQL (db.t3.micro)
  - vCPUs: 2
  - Memory: 1 GB RAM
  - Storage: 20 GB SSD
  - Free Tier: 750 hours/month
  - Estimated Cost: $0.00/month

🔸 Data Transfer
  - Free Tier: 1 GB/month outbound
  - Estimated Cost: $0.00/month

💡 Total Monthly Cost: $0.00 (within Free Tier limits)

⚠️  Important Notes:
- Free Tier is valid for 12 months from AWS account creation
- Monitor usage in AWS Billing Dashboard
- Set up billing alerts at $1-5 to avoid surprises
- Stop instances when not in use to conserve hours

EOF

# Step 5: Generate deployment checklist
echo -e "\n${CYAN}📋 Step 5: Deployment Checklist${NC}"
cat << 'EOF'

Pre-AWS Setup Checklist:
========================

□ AWS Account created and verified
□ AWS CLI installed (optional but recommended)
□ EC2 Key Pair created and downloaded
□ Basic understanding of AWS console

AWS Resource Creation:
=====================

□ RDS MySQL instance created (db.t3.micro)
□ RDS Security Group configured (leonardo-db-sg)
□ EC2 instance launched (t2.micro, Amazon Linux 2023)
□ EC2 Security Group configured (leonardo-app-sg)
□ Security Groups properly linked (EC2 → RDS)

Deployment Steps:
================

□ SSH access to EC2 instance verified
□ aws-setup.sh executed on EC2
□ Database credentials configured
□ deploy-to-aws.sh updated with correct values
□ Application deployed and running
□ Health checks passing
□ API endpoints responding
□ Leonardo schema updated with production URLs

Post-Deployment:
===============

□ Application monitoring configured
□ Backup strategy implemented
□ Security groups reviewed
□ SSL/TLS certificates configured (if needed)
□ Domain name pointed to EC2 (optional)

EOF

# Step 6: Show next steps
echo -e "\n${GREEN}🎉 AWS Deployment Preparation Complete!${NC}"
echo -e "\n${BLUE}📂 Deployment Package: ./$DEPLOY_DIR/${NC}"
echo -e "\n${YELLOW}🚀 Next Steps:${NC}"
echo "1. Review the AWS_DEPLOYMENT_GUIDE.md"
echo "2. Create AWS RDS and EC2 instances"
echo "3. Update deploy-to-aws.sh with your values"
echo "4. Run the deployment"

echo -e "\n${CYAN}📖 Commands:${NC}"
echo -e "   View guide: ${YELLOW}cat $DEPLOY_DIR/AWS_DEPLOYMENT_GUIDE.md${NC}"
echo -e "   Start deployment: ${YELLOW}cd $DEPLOY_DIR && ./deploy-to-aws.sh${NC}"

echo -e "\n${GREEN}✨ Ready for AWS Free Tier deployment!${NC}"
