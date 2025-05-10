<p align="center">
  <img src="wallet_logo.png" width="150" alt="BicciWallet logo"/>
</p>

# BicciWallet

**BicciWallet** is a deliberately vulnerable banking-style microservices system built with Java Spring Boot. It simulates a digital wallet platform that allows users to register, log in, manage their accounts, and perform peer-to-peer transfers. This platform was designed **for educational purposes only**, with realistic security flaws that match the **OWASP API Security Top 10 (2023)**.

---

## ⚠️ Warning: Intentionally Vulnerable

This application contains intentional vulnerabilities such as:

- Broken authentication logic
- Object/property-level access flaws
- Excessive data exposure
- SSRF and insecure consumption of external APIs
- Missing or misconfigured headers
- Obsolete API versions that are still active

---

## 🎯 Purpose

BicciWallet aims to:

- Educate developers about real-world API vulnerabilities
- Serve as a practical playground for red/blue team exercises
- Be used in YouTube demonstrations and workshops on secure coding and pentesting
- Highlight the consequences of misconfigurations and insecure coding

> BicciWallet is for **offline labs, demonstrations, and controlled environments only.**

---

## 🛠 Technologies

- Java 17
- Spring Boot 3
- Maven
- MySQL
- Docker / Docker Compose

---

## ✅ OWASP API Security Top 10 (2023) – Coverage

| #   | OWASP API Risk                                          | Implemented In     | Description                                                                 |
|-----|----------------------------------------------------------|---------------------|-----------------------------------------------------------------------------|
| A01 | Broken Object Level Authorization                       | ms-accounts         | `/find?username=` leaks any account data                                   |
| A02 | Broken Authentication                                   | ms-users            | Weak login validation, long-lived JWTs, no brute force protection          |
| A03 | Broken Object Property Level Authorization              | ms-users            | `/update-role` lets any user change roles without checks                   |
| A04 | Unrestricted Resource Consumption                       | ms-accounts, ms-transactions | No pagination limit in `/history/all`, account creation flood               |
| A05 | Broken Function Level Authorization                     | ms-users            | No role check in endpoint like `/delete-user`                              |
| A06 | Unrestricted Access to Sensitive Data                   | ms-accounts, ms-transactions | `TransactionDto` exposes token, accounts expose full balance and type      |
| A07 | Server Side Request Forgery (SSRF)                      | ms-transactions     | `/proxy?url=` calls arbitrary external/internal URLs                       |
| A08 | Security Misconfiguration                               | all services         | CSRF/Headers disabled, stacktraces exposed                                 |
| A09 | Improper Inventory Management                           | ms-accounts         | Old endpoint `/v1/balance` still active                                    |
| A10 | Unsafe Consumption of APIs                              | ms-transactions     | Consumes responses without schema/validation from `ms-accounts`            |

---

## 🧪 Installation & Usage

### Requirements

- Java 17
- Maven
- Docker & Docker Compose
- Postman (for testing)

### 1. Clone the project

```bash
git clone https://github.com/your-org/BicciWallet.git
cd BicciWallet/Backend
```

### 2. Run the application
#### Option 1: Using Docker Compose (Recommended)

```bash
cd Backend
docker-compose up --build
```
#### Option 2: Using the deploy script

```bash
# Make the script executable (only needed once)
chmod +x deploy.sh

# Run the deploy script
./deploy.sh
```

#### To stop all containers:

```bash
cd Backend
docker-compose down
```

### 3. Test APIs

The Postman collection is included in this repository under the name **BicciWallet_API.postman_collection.json**.

---

## 🧠 Educational Focus

All vulnerabilities are real and mapped to OWASP. Use this system to:

- Train secure coding & red teaming
- Explain attack vectors in YouTube videos

---

## 📢 Disclaimer

This project is intentionally vulnerable. **DO NOT DEPLOY TO PRODUCTION.** Use only in isolated, educational, or lab environments. Vulnerabilities are deliberately left in the codebase.

---

## 🙏 Credits & License

Created by @sul4m_col as a learning tool and lab project.

MIT License.
