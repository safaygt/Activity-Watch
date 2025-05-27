# 🚀 Activity Watch Backend

Bu proje, masaüstü ve web uygulamalarının kullanıcı aktivitelerini kaydeden ve raporlayan **Activity Watch** uygulamasının **sunucu (backend)** tarafını oluşturmak için **Spring Boot** ile geliştirilmiştir.

---

## 📌 Proje Hakkında

**Activity Watch**, kullanıcıların günlük aktivitelerini—hangi uygulamada ve pencerede ne kadar süre geçirdiklerini—takip eden bir sistemdir.  
Bu backend projesi, loglanan aktiviteleri tarih bazlı sorgulama, aylık özet çıkarma ve toplam aktif süre hesaplama gibi işlevsellikler sunar.

---

## 🎯 Özellikler

✅ Günlük aktivite kayıtlarını listeleme (`/v1/report/log?date=YYYY-MM-DD`)  
✅ Aylık aktivite özetleri (`/v1/report/log/monthly?year=YYYY&month=MM`)  
✅ Günlük toplam aktif süre hesaplama (`/v1/report/active-time/{date}`)  
✅ Aylık toplam aktif süre hesaplama (`/v1/report/active-time/monthly?year=YYYY&month=MM`)  
✅ RESTful API yapısı ve versiyonlama (`/v1`)  
✅ Hata ve boş veri durumlarında uygun HTTP kodları

---

## 🛠️ Kullanılan Teknolojiler

- ☕ **Java 17**  
- 🌱 **Spring Boot**  
- 🕸 **Spring Web (REST API)**  
- 📦 **Spring Data JPA**  
- 🗄️ **PostgreSQL** (geliştirme için H2 desteği de eklenmiştir)  
- 📋 **Lombok**  
- 📦 **Maven** veya **Gradle**  
- 🛡 **Spring Boot Actuator** (opsiyonel)

---

## ⚙️ Kurulum

Aşağıdaki adımları izleyerek projeyi yerel ortamınızda çalıştırabilirsiniz:

1. 🔽 Depoyu klonlayın:
   ```bash
   git clone https://github.com/safaygt/Activity-Watch.git
   ```

2. 📁 Proje dizinine geçin:

   ```bash
   cd Activity-Watch
   ```

 3. 🛠️ Yapılandırma ayarlarını düzenleyin:
    src/main/resources/application.properties dosyasındaki değerleri kendinize göre güncelleyin:

<ul>
  <li>
    <strong>📦 Veritabanı Ayarları</strong>
    <ul>
      <li><code>spring.application.name=activitywatch</code></li>
      <li><code>spring.datasource.url=jdbc:postgresql://localhost:5432/activitywatch_db</code></li>
      <li><code>spring.datasource.username=postgres</code></li>
      <li><code>spring.datasource.password=postgres</code></li>
      <li><code>spring.datasource.driver-class-name=org.postgresql.Driver</code></li>
    </ul>
  </li>
  <li>
    <strong>🔄 Hibernate Ayarları</strong>
    <ul>
      <li><code>spring.jpa.hibernate.ddl-auto=update</code></li>
      <li><code>spring.jpa.show-sql=true</code></li>
    </ul>
  </li>
  <li>
  </li>
</ul>

4. Uygulamayı çalıştırın:

```bash
./gradlew bootRun
```

---



🤝 Katkıda Bulunma
Projeye katkıda bulunmak isterseniz aşağıdaki adımları izleyebilirsiniz:

Bu repoyu fork edin

Yeni bir branch oluşturun:
   ```bash
  git checkout -b feature/YeniOzellik
  ```

Geliştirmelerinizi yapın

Commit atın:
   ```bash
  git commit -m 'Yeni özellik eklendi'
   ```
Değişiklikleri gönderin:
   ```bash
  git push origin feature/YeniOzellik
  ```


---


> Geliştiren: [@safaygt](https://github.com/safaygt)  

> 💡 Sorularınız ya da önerileriniz için PR veya issue açabilirsiniz!

      
    
   

