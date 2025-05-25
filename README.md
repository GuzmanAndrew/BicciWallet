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
- Angular 17
- AWS

---

## 🧩 OWASP API Risk Coverage per Microservice

| OWASP Risk                          | ms-users | ms-accounts | ms-transactions |
|------------------------------------|:--------:|:-----------:|:---------------:|
| A01 – Broken Object Level Auth     | ❌       | ✅          | ✅              |
| A02 – Broken Authentication        | ✅       | ❌          | ❌              |
| A03 – Broken Object Property Auth  | ✅       | ❌          | ❌              |
| A04 – Unrestricted Resource Usage  | ✅       | ✅          | ✅              |
| A05 – Broken Function Level Auth   | ✅       | ❌          | ❌              |
| A06 – Access to Sensitive Data     | ✅       | ✅          | ✅              |
| A07 – Server Side Request Forgery  | ❌       | ❌          | ✅              |
| A08 – Security Misconfiguration    | ✅       | ✅          | ✅              |
| A09 – Improper Inventory Mgmt      | ❌       | ✅          | ❌              |
| A10 – Unsafe API Consumption       | ❌       | ✅          | ❌              |

> ✅ = Vulnerability detected in this microservice  
> ❌ = Not detected or not applicable in semi-passive phase

---

## 🛡️ OWASP API Security Top 10 (2023) – Consolidated Coverage

| #    | OWASP API Risk                              | Implemented In                             | Description                                                                                 |
|------|---------------------------------------------|---------------------------------------------|---------------------------------------------------------------------------------------------|
| A01  | Broken Object Level Authorization           | ms-accounts                                 | `/accounts/create?username=` allows creation of accounts for other users.                  |
| A01  | Broken Object Level Authorization           | ms-transactions                             | `/transactions/transfer?receiverUsername=` enables transfers to arbitrary users.            |
| A01  | Broken Object Level Authorization           | ms-accounts                                 | `/accounts/v1/balance?username=` exposes data from any account without access control.     |
| A02  | Broken Authentication                       | ms-users                                    | Login via query string (`username`, `password`), lacking HTTPS and proper validation.       |
| A03  | Broken Object Property Level Authorization  | ms-users                                    | `role` field can be set during registration.                                                |
| A03  | Broken Object Property Level Authorization  | ms-users                                    | `password` field accepted in PATCH `/users/update` (Mass Assignment risk).                 |
| A04  | Unrestricted Resource Consumption           | ms-users                                    | User registration lacks CAPTCHA or flood protection.                                       |
| A04  | Unrestricted Resource Consumption           | ms-transactions                             | `/transactions/history/all?page=&size=` has no enforced limit on `size`.                   |
| A05  | Broken Function Level Authorization         | ms-users                                    | `DELETE /users/delete?username=...` allows deletion of any user without authorization.     |
| A06  | Unrestricted Access to Sensitive Data       | ms-accounts                                 | `/accounts/v2/balance` exposes `username`, `balance`, and `accountType`.                   |
| A06  | Unrestricted Access to Sensitive Data       | ms-transactions                             | `/transactions/history` reveals the JWT (`rawToken`) used in the transaction.              |
| A06  | Unrestricted Access to Sensitive Data       | ms-users                                    | `/users/profile` returns full user data with no minimization.                              |
| A07  | Server Side Request Forgery (SSRF)          | ms-transactions                             | `/proxy?url=` allows arbitrary requests to internal or external URLs.                      |
| A08  | Security Misconfiguration                   | all services                                | Missing security headers (`CSP`, `HSTS`, `X-Frame-Options`, etc.).                          |
| A08  | Security Misconfiguration                   | ms-users                                    | Login sends credentials via URL.                                                            |
| A09  | Improper Inventory Management               | ms-accounts                                 | Deprecated endpoint `/v1/balance` is still accessible.                                     |
| A10  | Unsafe Consumption of APIs                  | ms-accounts                                 | `/accounts` consumes and trusts external API responses without validation/schema.          |

---

## 🧪 Installation & Usage

### 1. Clone the project

```bash
git clone https://github.com/your-org/BicciWallet.git
cd BicciWallet
```

### 2.  Build the backend

```bash
cd Backend/ms-users
./mvnw clean package -DskipTests

cd ../ms-accounts
./mvnw clean package -DskipTests

cd ../ms-transactions
./mvnw clean package -DskipTests
```

### 3.  Build the frontend

```bash
cd Frontend
npm install
ng build --configuration production
```


### 4. Test APIs

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
