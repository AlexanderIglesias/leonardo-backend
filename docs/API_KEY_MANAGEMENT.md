# API Key Management Guide - Leonardo Backend

## 🔑 API Key Generation

### Generation Methods

#### 1. **Automated Script (Recommended)**
```bash
./scripts/generate-api-key.sh
```

This script generates cryptographically secure API keys using:
- **OpenSSL**: Preferred method with random bytes
- **UUID**: Alternative method based on UUIDs
- **Multi-environment**: Generates keys for dev, staging and production

#### 2. **Manual Generation**
```bash
# OpenSSL method
openssl rand -base64 32 | tr -d "=+/" | cut -c1-32

# With prefix
echo "leonardo_$(openssl rand -base64 32 | tr -d '=+/' | cut -c1-32)"
```

#### 3. **API Key Characteristics**
- **Length**: 32+ characters
- **Format**: `leonardo_[environment]_[random_string]`
- **Examples**:
  - `leonardo_dev_kJ8mN3qR7sT2vY5zA9cF4gH6jK0pL1mX`
  - `leonardo_prod_8Vx2nM9kL5sR7qT4zA6cF3gH1jP0mY5b`

## 📍 Where to Store API Keys

### **1. Local Development**
```bash
# File: .env (create from .env.example)
API_SECURITY_ENABLED=true
API_KEY=leonardo_dev_kJ8mN3qR7sT2vY5zA9cF4gH6jK0pL1mX
```

**Location**: `/Users/alexanderiglesiasmontalvo/Documents/Development/Personal/leonardo-backend/.env`

### **2. AWS EC2 Production**
```bash
# System environment variables
export API_KEY=leonardo_prod_8Vx2nM9kL5sR7qT4zA6cF3gH1jP0mY5b

# Or in profile file
echo 'export API_KEY=leonardo_prod_8Vx2nM9kL5sR7qT4zA6cF3gH1jP0mY5b' >> ~/.bashrc
source ~/.bashrc
```

### **3. AWS Systems Manager (Recommended for Production)**
```bash
# Store in Parameter Store
aws ssm put-parameter \
    --name "/leonardo-backend/prod/api-key" \
    --value "leonardo_prod_8Vx2nM9kL5sR7qT4zA6cF3gH1jP0mY5b" \
    --type "SecureString" \
    --description "Leonardo Backend Production API Key"

# Retrieve in application
aws ssm get-parameter \
    --name "/leonardo-backend/prod/api-key" \
    --with-decryption \
    --query 'Parameter.Value' \
    --output text
```

### **4. Docker (Local/Staging)**
```yaml
# docker-compose.yml
version: '3.8'
services:
  leonardo-backend:
    image: leonardo-backend:latest
    environment:
      - API_KEY=${API_KEY}
    env_file:
      - .env
```

### **5. Password Manager (Secure Backup)**
- **1Password**: Category "API Credentials"
- **Bitwarden**: Type "Secure Note"
- **LastPass**: "Secure Notes"

**Recommended format**:
```
Title: Leonardo Backend API Keys
Username: leonardo-backend
Notes:
  - Dev: leonardo_dev_[key]
  - Staging: leonardo_staging_[key]  
  - Production: leonardo_prod_[key]
  - Generated: 2025-08-24
  - Rotation: Every 90 days
```

## 🔄 API Key Rotation

### **Recommended Schedule**
- **Development**: Every 6 months
- **Staging**: Every 3 months  
- **Production**: Every 90 days

### **Rotation Process**
1. Generate new API key
2. Update in corresponding environment
3. Test GPT agent connectivity
4. Update password manager
5. Revoke/delete previous key

## 🚦 GPT Agent Configuration

### **Required Header**
```http
X-API-Key: leonardo_prod_8Vx2nM9kL5sR7qT4zA6cF3gH1jP0mY5b
```

### **Request Example**
```http
GET /api/v1/metrics/scalar
Host: your-ec2-instance.com:8080
X-API-Key: leonardo_prod_8Vx2nM9kL5sR7qT4zA6cF3gH1jP0mY5b
Content-Type: application/json
```

### **Swagger Documentation**
Documentation will be available at:
- **Local**: http://localhost:8080/swagger-ui.html
- **AWS**: http://your-ec2-instance:8080/swagger-ui.html

## ⚠️ Security and Best Practices

### **DO ✅**
- Use different keys for each environment
- Store in environment variables
- Rotate periodically
- Use the included generation script
- Backup in password manager

### **DON'T ❌**
- Hardcode in source code
- Commit to Git repository
- Share via email/chat
- Use the same key in multiple environments
- Store in plain text files

### **Monitoring**
- Log requests with API key (hash, not full value)
- Alerts for expired keys
- Usage metrics per key
- Rate limiting per key

## 🔧 Code Implementation

Configuration will be read from:
```properties
# application.properties
api.security.enabled=${API_SECURITY_ENABLED:true}
api.key=${API_KEY}
```

The security filter will validate the `X-API-Key` header on all requests (except Swagger and health check).
