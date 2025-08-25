#!/bin/bash

# Leonardo Backend - Local Environment Setup Script
# This script sets up environment variables for local development

echo "🔧 Setting up Leonardo Backend environment variables..."

# Load environment variables from .env file
if [ -f ".env" ]; then
    echo "📁 Loading variables from .env file..."
    # Source the .env file to load all variables
    source .env
    echo "✅ All variables from .env loaded successfully"
else
    echo "⚠️  .env file not found, using default values"
    
    # API Configuration
    # Note: Use ./scripts/generate-api-key.sh to generate a proper API key
    export API_KEY="leonardo_dev_example_key_1234567890abcdef"
    export API_SECURITY_ENABLED=true
    
    # Database Configuration for local development
    export DB_URL="jdbc:mysql://localhost:3306/leonardo_senasoft?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8&useUnicode=true"
    export DB_USERNAME="leonardo_user"
    export DB_PASSWORD="REPLACE_WITH_YOUR_PASSWORD"
    
    # MySQL Root Password (for Docker only)
    export MYSQL_ROOT_PASSWORD="REPLACE_WITH_YOUR_ROOT_PASSWORD"
fi

echo "✅ Environment variables set for current session"
echo ""
echo "📋 Variables configured:"

# Show all relevant environment variables
echo "🔐 API Security:"
echo "  - API_KEY: ${API_KEY:-[NOT SET]}"
echo "  - API_SECURITY_ENABLED: ${API_SECURITY_ENABLED:-[NOT SET]}"

echo ""
echo "🗄️  Database Configuration:"
echo "  - DB_URL: ${DB_URL:-[NOT SET]}"
echo "  - DB_USERNAME: ${DB_USERNAME:-[NOT SET]}"
echo "  - DB_PASSWORD: ${DB_PASSWORD:+[SET]}"  # Only show if set, don't expose value
echo "  - MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD:+[SET]}"

echo ""
echo "🌍 Environment Info:"
echo "  - Current Profile: ${SPRING_PROFILES_ACTIVE:-dev}"
echo "  - Project Directory: ${PWD}"

echo ""
echo "⚠️  These variables are only active in the current terminal session."
echo "   To make them permanent, add them to your ~/.zshrc or ~/.bash_profile"
echo ""
echo "💡 To verify variables are loaded, run: env | grep -E '(API_|DB_|MYSQL_)'"
