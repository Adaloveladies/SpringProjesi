# 🚀 Spring Projesi API - Postman Kullanım Kılavuzu

## 📋 İçindekiler
1. [Kurulum](#kurulum)
2. [Koleksiyon İçeriği](#koleksiyon-içeriği)
3. [Test Senaryoları](#test-senaryoları)
4. [Önemli Notlar](#önemli-notlar)

## 🛠️ Kurulum

### 1. Postman'e Koleksiyon Yükleme
1. Postman'i açın
2. **Import** butonuna tıklayın
3. `SpringProjesi_API_Collection.json` dosyasını seçin
4. Koleksiyon başarıyla yüklenecek

### 2. Environment Kurulumu
1. **Import** butonuna tıklayın
2. `SpringProjesi_Environment.json` dosyasını seçin
3. Sağ üst köşeden "Spring Projesi Environment" seçin

### 3. Uygulamayı Başlatma
```bash
mvn spring-boot:run
```

## 📚 Koleksiyon İçeriği

### 🔐 Kimlik Doğrulama
- **Kullanıcı Kaydı**: Yeni kullanıcı hesabı oluşturur
- **Kullanıcı Girişi**: Mevcut kullanıcı ile giriş yapar (Token otomatik kaydedilir)

### 👥 Kullanıcı Yönetimi
- **Tüm Kullanıcıları Listele**: Sistemdeki tüm kullanıcıları getirir
- **Kullanıcı Detayı Getir**: Belirli bir kullanıcının bilgilerini getirir
- **Kullanıcı Güncelle**: Kullanıcı bilgilerini günceller
- **Kullanıcıya Puan Ekle**: Kullanıcıya puan ekler

### 📋 Görev Yönetimi
- **Yeni Görev Oluştur**: Kullanıcı için yeni görev oluşturur
- **Görevleri Listele**: Kullanıcının tüm görevlerini listeler
- **Görev Detayı Getir**: Belirli bir görevin detaylarını getirir
- **Görev Güncelle**: Mevcut görevi günceller
- **Görevi Tamamla**: Görevi tamamlandı olarak işaretler
- **Görev Sil**: Görevi siler

### 🏙️ Şehir Yönetimi
- **Şehir Oluştur**: Yeni şehir oluşturur
- **Şehirleri Listele**: Tüm şehirleri listeler

### 🏗️ Bina Yönetimi
- **Bina Oluştur**: Yeni bina oluşturur
- **Binaları Listele**: Tüm binaları listeler

### 📊 İstatistikler
- **Kullanıcı İstatistikleri**: Kullanıcının istatistiklerini getirir

### 🏆 Liderlik Tablosu
- **Liderlik Tablosu**: En yüksek puanlı kullanıcıları listeler

### 🔔 Bildirimler
- **Bildirimleri Listele**: Kullanıcının bildirimlerini listeler
- **Bildirimi Okundu İşaretle**: Bildirimi okundu olarak işaretler

### 📝 Audit Log
- **Audit Logları Listele**: Sistem loglarını listeler

### 🔧 Sistem Durumu
- **Health Check**: Sistem sağlığını kontrol eder
- **Sistem Bilgileri**: Sistem bilgilerini getirir
- **Metrikler**: Performans metriklerini getirir

## 🧪 Test Senaryoları

### Senaryo 1: Temel Kullanıcı İşlemleri
1. **Kullanıcı Kaydı** → Yeni kullanıcı oluştur
2. **Kullanıcı Girişi** → Token al (otomatik kaydedilir)
3. **Kullanıcı Detayı Getir** → Kullanıcı bilgilerini kontrol et
4. **Kullanıcıya Puan Ekle** → Puan ekle ve kontrol et

### Senaryo 2: Görev Yönetimi
1. **Yeni Görev Oluştur** → Görev oluştur
2. **Görevleri Listele** → Görevleri kontrol et
3. **Görev Detayı Getir** → Görev detaylarını kontrol et
4. **Görev Güncelle** → Görevi güncelle
5. **Görevi Tamamla** → Görevi tamamla
6. **Görev Sil** → Görevi sil

### Senaryo 3: Şehir ve Bina Yönetimi
1. **Şehir Oluştur** → Şehir oluştur
2. **Bina Oluştur** → Bina oluştur (şehir ID'si gerekli)
3. **Şehirleri Listele** → Şehirleri kontrol et
4. **Binaları Listele** → Binaları kontrol et

### Senaryo 4: Sistem Kontrolü
1. **Health Check** → Sistem sağlığını kontrol et
2. **Sistem Bilgileri** → Sistem bilgilerini kontrol et
3. **Metrikler** → Performans metriklerini kontrol et

## ⚠️ Önemli Notlar

### 🔑 Token Yönetimi
- Login işleminden sonra token otomatik olarak environment'a kaydedilir
- Diğer isteklerde `{{token}}` değişkeni kullanılır
- Token süresi dolduğunda tekrar login yapmanız gerekir

### 📝 Test Sırası
1. Önce **Kullanıcı Kaydı** yapın
2. Sonra **Kullanıcı Girişi** yapın (token alın)
3. Diğer işlemleri sırayla test edin

### 🔄 ID Yönetimi
- Oluşturulan kayıtların ID'leri response'da döner
- Bu ID'leri diğer isteklerde kullanabilirsiniz
- Environment'da `userId`, `gorevId`, `sehirId`, `buildingId` değişkenleri var

### 🚨 Hata Kodları
- **200**: Başarılı
- **400**: Geçersiz istek
- **401**: Yetkilendirme hatası
- **404**: Kayıt bulunamadı
- **409**: Çakışma (örn: kullanıcı adı zaten var)

### 📊 Swagger UI
- API dokümantasyonu: http://localhost:8081/swagger-ui.html
- Tüm endpoint'leri ve parametreleri görebilirsiniz

## 🎯 Örnek Test Akışı

```bash
# 1. Uygulamayı başlat
mvn spring-boot:run

# 2. Postman'de test et
# - Kullanıcı Kaydı
# - Kullanıcı Girişi
# - Görev Oluştur
# - Görevleri Listele
# - Görevi Tamamla
```

## 🔧 Environment Değişkenleri

| Değişken | Açıklama | Varsayılan Değer |
|----------|----------|------------------|
| `baseUrl` | API base URL | http://localhost:8081 |
| `token` | JWT Token | (Login'den sonra otomatik) |
| `userId` | Kullanıcı ID | (Manuel set edilir) |
| `gorevId` | Görev ID | (Manuel set edilir) |
| `sehirId` | Şehir ID | (Manuel set edilir) |
| `buildingId` | Bina ID | (Manuel set edilir) |

## 🎉 Başarılı Test Sonrası

Tüm testler başarılı olduktan sonra:
- ✅ API'leriniz çalışıyor
- ✅ Veritabanı bağlantısı aktif
- ✅ JWT authentication çalışıyor
- ✅ Rate limiting aktif
- ✅ Audit logging çalışıyor

**İyi testler! 🚀** 