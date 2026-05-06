# Trendyol Shopping Cart Case Study

Bu proje, bir e-ticaret sepetinin (shopping cart) iş kurallarını, ürün yönetimini ve promosyon (indirim) hesaplamalarını yöneten **Java 21** tabanlı bir konsol uygulamasıdır. 

Uygulama baştan sona **Test-Driven Development (TDD)** yaklaşımıyla, **Domain-Driven Design (DDD)** ve **Hexagonal Architecture** prensiplerine sadık kalınarak geliştirilmiştir.

## 🚀 Teknolojiler ve Araçlar
* **Java 21** (Records, Sealed Classes, Pattern Matching)
* **Maven** (Bağımlılık Yönetimi ve Build)
* **JUnit 5 & AssertJ & Mockito** (Birim ve Entegrasyon Testleri)
* **Jackson (fasterxml)** (JSON Okuma/Yazma İşlemleri)
* **JaCoCo** (Test Kapsam / Coverage Raporlaması)

## 🏗 Mimari Yaklaşım (Hexagonal / Clean Architecture)
Proje, bağımlılıkların dışarıdan içeriye doğru (Infrastructure -> Application -> Domain) ilerlediği üç ana katmandan oluşur:

1. **Domain Layer (`com.trendyol.shoppingcart.domain`)**: 
   Dış dünyadan (JSON, Veritabanı vb.) tamamen izoledir. Sadece iş mantığını (Business Logic) barındırır.
   * `Cart` sınıfı bir **Aggregate Root** olarak sepetin limitlerini ve kurallarını yönetir.
   * Ürünler (DefaultItem, DigitalItem, VasItem) nesne yönelimli olarak modellenmiştir.
   * `PromotionEngine` promosyon kurallarını uygular.
2. **Application Layer (`com.trendyol.shoppingcart.application`)**: 
   Dış dünyadan gelen komutların Domain nesnelerine çevrilip işlendiği Orkestrasyon katmanıdır (Use Case).
3. **Infrastructure Layer (`com.trendyol.shoppingcart.infrastructure`)**: 
   Sistemin dış dünya ile iletişim kurduğu katmandır (JSON Parsing ve File I/O işlemleri).

## 🧠 Kullanılan Tasarım Şablonları (Design Patterns)
* **Factory Method:** `ItemFactory` aracılığıyla, gelen parametrelere (Kategori vb.) göre doğru ürün tipinin yaratılması.
* **Strategy Pattern:** `Promotion` arayüzü sayesinde, farklı promosyon kurallarının (`TotalPricePromotion`, `SameSellerPromotion` vb.) birbirinin yerine kullanılabilmesi.
* **Template Method:** `Item` soyut sınıfında ortak sepet davranışlarının tanımlanıp, tipe özel davranışların (`getMaxQuantity` vb.) alt sınıflara bırakılması.
* **Chain of Responsibility:** `PromotionEngine` içinde sepetin tüm indirim stratejilerinden geçirilip en avantajlısının seçilmesi.

## 💼 İş Kuralları (Business Rules)
Sistem aşağıdaki tüm iş kurallarını sıkı bir şekilde uygular:
- Sepetteki bir ürünün miktarı normal ürünler için max **10**, dijital ürünler için max **5** olabilir.
- Aynı sepette en fazla **10 farklı** (unique) ürün bulunabilir.
- Sepetteki toplam ürün sayısı (quantity) en fazla **30** olabilir.
- Sepet tutarı maksimum **500.000 TL** olabilir.
- **VasItem (Katma Değerli Servis - Örn: Garanti)** sadece Furniture (1001) ve Electronics (3004) kategorisindeki ana ürünlere eklenebilir. 
- Bir ana ürüne en fazla 3 VasItem eklenebilir ve VasItem fiyatı ana ürün fiyatını geçemez.
- İndirim motoru (Promotion Engine), uygulanabilir tüm indirimleri hesaplar ve **müşteri için en avantajlı olan** indirimi sepete yansıtır.

## 🧪 Testler (TDD)
Proje sıfırdan TDD felsefesiyle yazılmıştır. Tüm sınır durumları (edge-cases), hata durumları (exceptions) ve başarı senaryoları test edilmiştir. 
Toplamda **73 adet** Unit ve Integration testi bulunmaktadır. Testleri çalıştırmak için:
```bash
mvn clean test
```

## 🛠 Kurulum ve Çalıştırma

### 1. Build İşlemi
Projeyi derlemek ve çalıştırılabilir bir JAR dosyası oluşturmak için:
```bash
mvn clean package
```
Bu komut sonucunda `target/` klasörü altında `shopping-cart.jar` dosyası oluşacaktır.

### 2. Uygulamayı Çalıştırma
Uygulama komut satırından 2 argüman bekler: `<input-dosyasi.json>` ve `<output-dosyasi.json>`

Örnek kullanım:
```bash
java -jar target/shopping-cart.jar input.json output.json
```

### Örnek `input.json` Satırları
```json
{"command":"addItem","payload":{"itemId":1,"categoryId":1001,"sellerId":1,"price":100.0,"quantity":2}}
{"command":"addVasItemToItem","payload":{"itemId":1,"vasItemId":100,"categoryId":3242,"sellerId":5003,"price":50.0,"quantity":1}}
{"command":"displayCart"}
{"command":"resetCart"}
```

---
*Geliştirme, yazılım standartlarına (SOLID) ve kurumsal kod kalitesine sadık kalınarak tamamlanmıştır.*
