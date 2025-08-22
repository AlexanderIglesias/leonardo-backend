# 🚀 AWS Deployment Guide - Leonardo Backend

## AWS Free Tier Deployment Strategy

This guide will help you deploy the Leonardo Backend to AWS using only **FREE TIER** services.

### 📋 Prerequisites

1. **AWS Account** with Free Tier access
2. **SSH Key Pair** created in EC2
3. **Basic AWS CLI knowledge** (optional but recommended)

---

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User/Client   │    │   EC2 Instance  │    │   RDS MySQL     │
│                 │────│   t2.micro      │────│   db.t3.micro   │
│   (Leonardo)    │    │   Spring Boot   │    │   Database      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 💰 AWS Free Tier Resources Used

| Service | Type | Free Tier Limit | Usage |
|---------|------|-----------------|-------|
| **EC2** | t2.micro | 750 hours/month | Spring Boot Application |
| **RDS** | db.t3.micro | 750 hours/month | MySQL Database |
| **EBS** | gp2 | 30 GB/month | Storage for EC2 |
| **RDS Storage** | - | 20 GB/month | Database Storage |

---

## 🎯 Step-by-Step Deployment

### Step 1: Create RDS MySQL Instance

1. **Go to RDS Console**
   - Navigate to AWS RDS Console
   - Click "Create database"

2. **Configuration**
   ```
   Engine: MySQL 8.0
   Template: Free tier
   Instance: db.t3.micro
   Storage: 20 GB (Free tier limit)
   ```

3. **Database Settings**
   ```
   DB instance identifier: leonardo-db
   Master username: leonardo_user
   Master password: [SECURE_PASSWORD]
   Database name: leonardo_senasoft
   ```

4. **Security & Connectivity**
   ```
   VPC: Default VPC
   Publicly accessible: No
   Security group: Create new (leonardo-db-sg)
   ```

5. **Save the following info:**
   ```
   Endpoint: leonardo-db.cluster-xxxxxx.us-east-1.rds.amazonaws.com
   Port: 3306
   Username: leonardo_user
   Password: [YOUR_PASSWORD]
   ```

### Step 2: Create EC2 Instance

1. **Launch Instance**
   ```
   AMI: Amazon Linux 2023
   Instance type: t2.micro
   Key pair: [YOUR_KEY_PAIR]
   ```

2. **Security Group (leonardo-app-sg)**
   ```
   SSH (22): Your IP
   HTTP (8080): 0.0.0.0/0
   HTTPS (443): 0.0.0.0/0 (optional)
   ```

3. **Storage**
   ```
   Root volume: 8 GB gp2 (Free tier)
   ```

### Step 3: Configure Security Groups

1. **Database Security Group (leonardo-db-sg)**
   ```
   Type: MySQL/Aurora (3306)
   Source: leonardo-app-sg
   ```

2. **Application Security Group (leonardo-app-sg)**
   ```
   SSH (22): Your IP
   Custom TCP (8080): 0.0.0.0/0
   ```

### Step 4: Setup EC2 Instance

1. **Connect to EC2**
   ```bash
   ssh -i your-key.pem ec2-user@your-ec2-public-ip
   ```

2. **Run Setup Script**
   ```bash
   # Copy the setup script to EC2
   scp -i your-key.pem scripts/aws-setup.sh ec2-user@your-ec2-ip:/tmp/
   
   # Execute on EC2
   ssh -i your-key.pem ec2-user@your-ec2-ip
   chmod +x /tmp/aws-setup.sh
   sudo /tmp/aws-setup.sh
   ```

### Step 5: Configure Database Connection

1. **Update systemd service with real database credentials**
   ```bash
   sudo systemctl edit leonardo-backend
   ```

2. **Add environment variables**
   ```ini
   [Service]
   Environment=DB_USERNAME=leonardo_user
   Environment=DB_PASSWORD=your_actual_password
   Environment=SPRING_DATASOURCE_URL=jdbc:mysql://your-rds-endpoint:3306/leonardo_senasoft?useSSL=true&requireSSL=true&serverTimezone=UTC
   ```

### Step 6: Deploy Application

1. **Update deployment script**
   ```bash
   # Edit scripts/deploy-to-aws.sh
   EC2_HOST="your-ec2-public-ip"
   KEY_FILE="path/to/your-key.pem"
   ```

2. **Run deployment**
   ```bash
   ./scripts/deploy-to-aws.sh
   ```

---

## 🔧 Configuration Files

### application-aws.properties
Located at `src/main/resources/application-aws.properties`

Key configurations for AWS:
- Database connection with SSL
- Optimized connection pool for t2.micro
- Production logging levels
- Health checks for ALB

### Environment Variables
```bash
DB_USERNAME=leonardo_user
DB_PASSWORD=your_secure_password
SPRING_DATASOURCE_URL=jdbc:mysql://your-rds-endpoint:3306/leonardo_senasoft
```

---

## 📊 Monitoring & Maintenance

### Application Logs
```bash
# View real-time logs
sudo journalctl -u leonardo-backend -f

# View recent logs
sudo journalctl -u leonardo-backend -n 100
```

### Service Management
```bash
# Start/Stop/Restart
sudo systemctl start leonardo-backend
sudo systemctl stop leonardo-backend
sudo systemctl restart leonardo-backend

# Check status
sudo systemctl status leonardo-backend
```

### Health Checks
```bash
# Application health
curl http://your-ec2-ip:8080/actuator/health

# API endpoints
curl http://your-ec2-ip:8080/api/v1/metrics/scalar
```

---

## 🛡️ Security Best Practices

### 1. Database Security
- ✅ RDS instance not publicly accessible
- ✅ Security group restricts access to EC2 only
- ✅ SSL/TLS encryption enabled

### 2. EC2 Security
- ✅ Security group allows only necessary ports
- ✅ SSH access restricted to your IP
- ✅ Application runs as non-root user

### 3. Application Security
- ✅ No sensitive data in logs
- ✅ Health endpoints protected
- ✅ CORS configured appropriately

---

## 💲 Cost Optimization

### Free Tier Limits
- **EC2**: 750 hours/month (t2.micro)
- **RDS**: 750 hours/month (db.t3.micro)
- **Storage**: 30 GB EBS + 20 GB RDS

### Monitoring Usage
```bash
# Check AWS billing dashboard regularly
# Set up billing alerts at $1-5 to avoid surprises
```

---

## 🚨 Troubleshooting

### Common Issues

1. **Application won't start**
   ```bash
   # Check logs
   sudo journalctl -u leonardo-backend -n 50
   
   # Check Java memory
   free -h
   ```

2. **Database connection issues**
   ```bash
   # Test connectivity
   telnet your-rds-endpoint 3306
   
   # Check security groups
   # Verify RDS allows connections from EC2
   ```

3. **Out of memory (t2.micro has 1GB)**
   ```bash
   # Monitor memory usage
   htop
   
   # Adjust JVM settings in systemd service
   -Xmx768m -Xms256m
   ```

---

## 🔗 Post-Deployment

### Update Leonardo Schema
1. Update `src/main/java/com/alphanet/products/leonardobackend/openai.action.schema.json`
2. Replace localhost URLs with your EC2 public IP
3. Test with ChatGPT/Leonardo integration

### DNS (Optional - not Free Tier)
- Consider using Route 53 for custom domain
- Or use free DNS services like Cloudflare

---

## 📞 Support

If you encounter issues:
1. Check the troubleshooting section
2. Review AWS CloudWatch logs
3. Verify security group configurations
4. Ensure Free Tier limits aren't exceeded

---

## ✅ Deployment Checklist

- [ ] RDS MySQL instance created and configured
- [ ] EC2 t2.micro instance launched
- [ ] Security groups properly configured
- [ ] SSH key pair configured
- [ ] Application deployed and running
- [ ] Database connectivity verified
- [ ] API endpoints responding
- [ ] Health checks passing
- [ ] Leonardo schema updated with new URLs
- [ ] Monitoring and alerts configured

**🎉 Your Leonardo Backend is now running on AWS Free Tier!**
