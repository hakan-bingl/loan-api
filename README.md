# 💳 Loan API (Spring Boot)

Spring Boot ile geliştirilmiş bir **Kredi Yönetim Sistemi**.  
Banka çalışanları ve müşteriler, kredi oluşturabilir, görüntüleyebilir ve taksit ödeyebilir.

---

## 🛠 Teknolojiler
- **Java 21**
- **Spring Boot 3**
- Spring Web, Data JPA, Validation, Security
- **H2 In-Memory Database**
- JUnit 5 (Unit Test)

---


# 📦 Proje Yapısı: Hexagonal Architecture (Spring Boot)

```
│
├── application                 # Use cases (port implementasyonları ve servis katmanı)
│   └── port
│       ├── in                  # Giriş portları (kullanıcı, controller vs.)
│       └── out                 # Çıkış portları (veritabanı, mesajlaşma, dosya vs.)
│
├── domain                      # Core domain (iş kuralları ve modeller)
│   ├── model                   # Entity, Value Object, Enum vb.
│   └── component               # Domain service (saf iş kuralları, bağımsız)
│
├── infrastructure              # Adapter'lar: dış dünya ile etkileşim
│   ├── persistence             # Veritabanı (JPA, JDBC, Mongo vs.)
│   ├── rest                    # GRPC controller'lar
│   ├── messaging               # MQ sistemleri (Kafka, RabbitMQ)
│   └── config                  # Spring yapılandırmaları (Bean, Properties)
│
└── bootstrap                   # Main class ve uygulama başlatıcıları
```

---

## 📘 Açıklamalar

| Klasör                       | Açıklama |
|------------------------------|----------|
| `application.port.in`        | Use-case interface’leri (örneğin: `CreateOrderUseCase`) |
| `application.port.out`       | Dış dünya ile konuşmak için tanımlı port’lar (örneğin: `OrderRepositoryPort`) |
| `domain.model`               | `Entity`, `ValueObject` gibi temel domain yapıları |
| `domain.service`             | Domain seviyesinde iş kuralları (örneğin: `OrderValidator`) |
| `infrastructure.rest`        | Controller katmanı (adapter) |
| `infrastructure.persistence` | JPA/Mongo/Redis gibi veri kaynaklarıyla etkileşim |
| `infrastructure.messaging`   | Mesajlaşma sistemleri adapter’ları |
| `infrastructure.config`      | Spring'e özel ayarlar, bean tanımları |
| `bootstrap`                  | Spring Boot uygulama giriş noktası (`Application.kt`/`.java`) |

---

---

### ✅ Bağımlılık Yönü

* `domain` hiç kimseye bağımlı değildir.
* `application` sadece `domain`'e bağımlıdır.
* `infrastructure` hem `application` hem `domain`'e bağımlıdır.
* `rest`, `messaging`, `persistence` sadece port implement eder.

---

## 🚀 Çalıştırma

```bash
./gradlew bootRun
```

**H2 Console:**  
`http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:loan-db`

---

## 👥 Demo Kullanıcılar

| Kullanıcı Adı   | Şifre       | Rol           | customerId |
|-----------------|-------------|---------------|------------|
| admin           | Admin!?     | ROLE_ADMIN    | -          |
| customer.first  | Customer1!? | ROLE_CUSTOMER | 1          |
| customer.second | Customer2!? | ROLE_CUSTOMER | 2          |

> `application.yml` içinde `app.security.userCustomerMap` ile CUSTOMER kullanıcılarının `customerId` eşleşmeleri tanımlanır.

---

## 🔒 Yetkilendirme Kuralları
- **ADMIN** → tüm müşteriler üzerinde işlem yapabilir.
- **CUSTOMER** → yalnızca kendi `customerId` verisiyle çalışabilir.

---

## 📡 API Endpoints

Tüm endpoint’ler **Basic Auth** gerektirir.

---

### 1️⃣ Kredi Oluştur
**POST** `/api/loans`

**Request:**
```json
{
  "customerId": 1,
  "amount": 1000.00,
  "interestRate": 0.2,
  "numberOfInstallments": "6"
}
```

**Kurallar:**
- `numberOfInstallments` ∈ {6, 9, 12, 24}
- `interestRate` ∈ [0.1, 0.5]
- Toplam = `amount * (1 + interestRate)`
- İlk taksit → **bir sonraki ayın 1’i**
- Müşteri kredi limiti kontrol edilir

**Response:**
```json
{
  "loanId": 1,
  "totalAmount": 1200.00,
  "numberOfInstallments": 6,
  "createDate": "2025-08-14"
}
```

---

### 2️⃣ Kredileri Listele
**GET** `/api/loans?customerId=1`

**Response:**
```json
[
  {
    "id": 1,
    "customerId": 1,
    "loanAmount": 1200.00,
    "numberOfInstallment": 6,
    "createDate": "2025-08-14",
    "paid": false
  }
]
```

---

### 3️⃣ Taksitleri Listele
**GET** `/api/loans/{loanId}/installments`

**Response:**
```json
[
  {
    "id": 10,
    "amount": 200.00,
    "paidAmount": null,
    "dueDate": "2025-09-01",
    "paymentDate": null,
    "paid": false
  }
]
```

---

### 4️⃣ Ödeme Yap
**POST** `/api/loans/{loanId}/pay`

**Request:**
```json
{ "amount": 250.00 }
```

**Ödeme mantığı:**
- Taksit **tam** ödenir (kısmi ödeme yok)
- En erken taksitten başlar
- Sadece **şimdiden 3 ay içinde** olan vadeler ödenebilir  
  (örn. Ocak ayındaysak: Ocak–Şubat–Mart)
- **Bonus:**
    - Erken ödeme → `taksitTutarı * 0.001 * erkenGün` (indirim)
    - Geç ödeme → `taksitTutarı * 0.001 * geçGün` (ceza)

**Response:**
```json
{
  "installmentsPaid": 1,
  "totalAmountSpent": 198.40,
  "loanFullyPaid": false
}
```

---

## 🧪 Test Çalıştırma

```bash
./gradlew clean test
```

Testler:
- **CreateLoanServiceTest** → kredi oluşturma kuralları
- **PaymentServiceTest** → ödeme mantığı, indirim/ceza hesapları

---

## 📌 Notlar
- Tüm para hesaplamaları `BigDecimal` ile, scale(2) ve `HALF_UP` yuvarlama ile yapılır.
- İlk vade tarihi **bir sonraki ayın 1’i** olacak şekilde hesaplanır.
- `usedCreditLimit`, kredi oluşturulurken toplam tutar kadar artar, ödeme yapıldığında taksit ana tutarı kadar azalır.