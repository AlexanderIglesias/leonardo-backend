#!/bin/bash

# Leonardo Backend - API Key Generator
# Generates cryptographically secure API keys for different environments

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔑 Leonardo Backend - API Key Generator${NC}"
echo "================================================"

# Function to generate a secure API key
generate_api_key() {
    local prefix=$1
    local length=${2:-32}

    # Generate random bytes and encode to base64, then clean up
    local random_part=$(openssl rand -base64 $length | tr -d "=+/" | cut -c1-$length)
    echo "${prefix}_${random_part}"
}

# Function to generate UUID-based key (alternative method)
generate_uuid_key() {
    local prefix=$1
    local uuid1=$(uuidgen | tr -d '-' | tr '[:upper:]' '[:lower:]')
    local uuid2=$(uuidgen | tr -d '-' | tr '[:upper:]' '[:lower:]')
    echo "${prefix}_${uuid1:0:16}${uuid2:0:16}"
}

echo -e "${YELLOW}Choose API key generation method:${NC}"
echo "1. OpenSSL random (recommended)"
echo "2. UUID-based"
echo "3. Generate all environments"
echo ""

read -p "Enter choice (1-3): " choice

case $choice in
    1)
        echo -e "\n${GREEN}Generating OpenSSL-based API key...${NC}"
        api_key=$(generate_api_key "leonardo" 32)
        echo -e "API Key: ${GREEN}$api_key${NC}"
        ;;
    2)
        echo -e "\n${GREEN}Generating UUID-based API key...${NC}"
        api_key=$(generate_uuid_key "leonardo")
        echo -e "API Key: ${GREEN}$api_key${NC}"
        ;;
    3)
        echo -e "\n${GREEN}Generating keys for all environments...${NC}"
        echo ""

        dev_key=$(generate_api_key "leonardo_dev" 32)
        staging_key=$(generate_api_key "leonardo_staging" 32)
        prod_key=$(generate_api_key "leonardo_prod" 32)

        echo -e "${BLUE}Development:${NC} ${GREEN}$dev_key${NC}"
        echo -e "${BLUE}Staging:${NC}     ${GREEN}$staging_key${NC}"
        echo -e "${BLUE}Production:${NC}  ${GREEN}$prod_key${NC}"

        # Create environment-specific files
        echo "# Development API Key" > .env.dev.key
        echo "API_KEY=$dev_key" >> .env.dev.key

        echo "# Staging API Key" > .env.staging.key
        echo "API_KEY=$staging_key" >> .env.staging.key

        echo "# Production API Key" > .env.prod.key
        echo "API_KEY=$prod_key" >> .env.prod.key

        echo -e "\n${YELLOW}📝 Key files created:${NC}"
        echo "  - .env.dev.key"
        echo "  - .env.staging.key"
        echo "  - .env.prod.key"
        echo -e "\n${RED}⚠️  Remember to add these to your password manager!${NC}"
        ;;
    *)
        echo -e "${RED}Invalid choice${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${YELLOW}📋 Next Steps:${NC}"
echo "1. Copy the API key to your password manager"
echo "2. Add to your .env file: API_KEY=<generated_key>"
echo "3. Set as environment variable in AWS EC2"
echo "4. Update your GPT agent configuration"
echo ""
echo -e "${RED}⚠️  SECURITY REMINDER:${NC}"
echo "- Never commit API keys to git"
echo "- Store in environment variables only"
echo "- Use different keys for each environment"
echo "- Rotate keys periodically"
