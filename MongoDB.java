1// ekranı temizler
cls

2// products isminde DB oluşturalım
use products

3// hangi Db deyim
db

4// databaseleri gösterme
show dbs

5//  electronics isminde collection oluşturma
db.createCollection("electronics")

6//mevcut collectionları görelim
db.getCollectionNames()

7//aktif collectionı silmek istersem
db.electronics.drop()

8//Collectiona 1 tane döküman ekleyelim
db.electronics.insertOne({"price" : 230 , "name" : "TV"})
// Trick1 : collection  oluşturmadan doğrudan insertOne() metodu ile data girilebilir
// Trick2 : collectiona data insert edilirken field lara tek tırnak, çift tırnak kullanılabilir.

9 // Birden fazla döküman girelim
db.electronics.insertMany([{"name":"ipod","price" : 111},{"name":"iphone14" },{"name":"radio","price": 53,"tax":10}])
// Trick : insertMany komudu ile data girilirken elementler "[]" ile dopalanır ve her bir element "{}" içine eklenir ve "," ile ayrılır.

10 // tek veya birden fazla döküman girelim
db.electronics.insert({"name" : "iron" , "tax" :12})
db.electronics.insert([{"price" :123},{"name" : "radio" ,"tax":8}])

11 // collectionı silmek istersem 
db.electronics.drop()

12// Database i silmek istersem
db.dropDatabase()

13// bütün dökümanları okumak istersem
db.electronics.find() 
veya 
db.electronics.find().pretty()

14 // ilk 2 dökümanı getirsin
db.electronics.find().limit(2)

15 // 3.dökümanı istersem 
db.electronics.find().skip(2).limit(1)

16 // 5. dökümandan itibaren ilk 2 dökümana
db.electronics.find().skip(4).limit(2)

17 // ismi ( name) TV olanları getir
db.electronics.find({"name":"TV"})

18 // ismi TV ve price bilgisi 230 olan dökümanı getirelim
db.electronics.find({$and:[{"name":"TV"},{"price":230}]})

19 // ismi Tv veya radio olan dökümanları getirelim

db.electronics.find({$or:[{"name":"TV"},{"name":"radio"}]})

20 // TV lerin sadece fiyatlarını görelim
db.electronics.find({"name":"TV"},{"price":1})

21// yukardaki taskin çıktısında id bilgisi olmasın
db.electronics.find({"name":"TV"},{"price":1, "_id":0})

22// Dökümanların sadece price ve name bilgileri gelsin
db.electronics.find({},{"name":1,"price":1,"_id":0})

23// yukardaki soruyu price değerine göre sıralamak istersem
db.electronics.find({},{"name":1,"price":1,"_id":0}).sort({"price":1})
veya 
db.electronics.find({},{"name":1,"price":1,"_id":0}).sort("price")

24// ilk dökümanı okumak istersem
db.electronics.find().limit(1)
veya 
db.electronics.findOne()

25// TV leri azalan fiyata göre getirelim
db.electronics.find({"name":"TV"},{"name":1,"price":1,"_id":0}).sort({"price":-1})

NOTE : 

Comparison Operators
    Eşitlik     ==> $eq
    Küçüktür    ==> $lt
    Büyüktür    ==> $gt
    Küçük eşit  ==> $lte
    Büyük eşit  ==> $gte
    Eşit değil  ==> $ne
    Dizi içinde ==> $in
    Dizi değil  ==> $nin

26 // fiyatı 230 olan TV yi getirelim
db.electronics.find({"name":"TV","price":230})

27// fiyatı 450 olan dökümanı bulalım
db.electronics.find({"price":450})
veya 
db.electronics.find({"price":{$eq:450}}) 
// TRICK : "$" her zaman süslü parantez ister

28 // fiyatı 300 den az veya eşit olan dökümanları bulalım
db.electronics.find({"price":{$lte:300}})

29// yukardaki soruda id çıktıda görünmesin
db.electronics.find({"price":{$lte:300}},{"name":1,"price":1,"_id":0})

30// fiyatı 43 den büyük veya eşit olan dökümanları getirin
db.electronics.find({"price":{$gte:43}})

31// fiyatı 230, 45, 34 den biri olan dökümanları getirin
db.electronics.find({$or:[{"price":230},{"price":45},{"price":34}]})
  // -->2.yol
db.electronics.find({"price" : {$in:[230,45,34]}})

32// fiyatı 230,450,34 olmayan dökümanları getirin
db.electronics.find({"price":{$nin:[230,450,34]}})


//=================================================================
//            findOneAndUpdate - findOneAndReplace
//=================================================================

// A - findOneAndReplace() 
//----------------------------
//   1-) belirtilen koşullara uyan ilk sorguyu bulur ve degistirir. 
//   2-) Komut icerisinde belirtilen kisimlari guncellerken bos birakilan 
//       alanlari kaldirir. (API'lerdeki PUT metoduna benzetilebilir).
//   3-) Islem sonunda ilgili dokumanin guncellenmemiş halini gosterir.yani ben bunu değiştirdim diye haber veriyor

//       
// B - findOneAndUpdate() 
//----------------------------
///  1-) Belirtilen koşullara uyan ilk sorguyu bulur ve degistirir. 
//   2-) Komut icerisinde belirtilen kisimlari guncellerken bos birakilan 
//       alanlari modifiye etmez  (API'lerdeki PACTH metoduna benzetilebilir).
//   3-) komutun kosul kismindan sonra degislikileri gerceklestirmek icin bir 
//       atomic operator kullanilir. ($set (direk değer verilirse), $inc(arttırma azaltma), $mul (çarpma)vb.)
//   4)  Islem sonunda ilgili dokumanin guncellenMEmiş halini gosterir.

//=================================================================
// ÖNEMLİ :   bu 2 kod çalıştığında dökümanın update olmamış hali ekrana gelir.
 

33// Fiyatı 200 tl den az olan dökümanı bulup ismini "Car" olarak, fiyatını da "2000" olarak değiştirin
db.electronics.findOneAndReplace({"price": {$lt:200}},{"name":"Car","price":2000})

34// Fiyatı 100 den büyük olan dökümanlar içinde fiyatı en düşük olanın ismini "En Ucuz" olarak değiştirin
db.electronics.findOneAndReplace({"price" : {$gt:100}},{"name":"En Ucuz"},{sort:{"price":1}})

// Açıklama-1 : findOneAndReplace metodu şartı sağlayan ilk dökümanı bulup sadece onu değiştirir , task de bizden istenen "en düşük" olduğu için dökümanları sort ile fiyatı küçükten büyüğe doğru sıraladık.

// Açıklama -2 : bu kod çalışınca PUT metodu olduğu için dökümanın "name" dışındaki bütün field lar null olarak atanır

35// Fiyatı 100 den büyük olan dökümanlar içinde fiyatı en büyük olanın ismini "En Pahalı" olarak değiştirin
db.electronics.findOneAndReplace({"price":{$gt:100}},{"name":"En Pahali"},{sort:{"price":-1}}) 

36// Fiyatı 100 den küçük olan dökümanın ismini"Güncellendi" olarak değiştirin, diğer fieldlar silinmesin
db.electronics.findOneAndUpdate({"price":{$lt:100}},{$set:{"name":"Guncellendi"}})

// findOneAndUpdate metodu PATCH gibi çalıştığı için set edilmeyen fieldlar değişmez, fakat update edilecek parametre başına $set operatörü kullanılması gerekir

37// Fiyatı 100 den küçük olan dökümanlar içinde fiyatı en küçük olanın ismini "En Ucuz" olarak değiştirin.
db.electronics.findOneAndUpdate({"price":{$lt:100}},{$set:{"name":"en ucuz"}},{sort:{"price":1}})

38// Fiyatını 200 den küçük olan dökümanlar içinde fiyatı en büyük olanın simini "En Pahalı" olarak değiştirin
db.electronics.findOneAndUpdate({"price":{$lt:200}},{$set:{"name":"En Pahali"}},{sort:{"price":-1}})

39// Fiyatı 230 a eşit olan dökümanı bulup fiyatını 250 yapıp, ekran çıktısını güncel haliyle gösterelim
db.electronics.findOneAndUpdate({"price":{$eq:230}},{$set:{"price":250}},{returnNewDocument :true})

40// Fiyatı 230 a eşit olan dökümanı bulup name ini "Gold" , fiyatını 250 yapıp konsol çıktısında update edilmiş sonucu gösterin, eğer döküman yoksa yeni oluşturun
db.electronics.findOneAndUpdate({"price":{$eq:230}},{$set:{"price":250,"name":"Gold"}},{returnNewDocument:true,upsert:true})

41// Birden fazla dökümanı aynı anda değiştirmek istersem ?
//Fiyatı 300 den düşük olan bütün dökümanların ismini "Woww" olarak değiştirin
db.electronics.update({"price":{$lt:300}},{$set:{"name":"Woww"}},{multi:true})

42// Fiyatı 200 den yüksek olan bütün dökümanların ismini "Yüksek" olarak değiştirin
db.electronics.update({"price":{$gt:200}},{$set:{"name":"Yüksek"}},{multi:true})
//2.yol 
db.electronics.updateMany({"price":{$gt:200}},{$set:{"name":"yuksek"}})

// Açıklama : updateMany() metodu güncel olduğu için , depricated edilen update() metodundaki gibi "multi:true" yazmamıza gerek kalmadı.

//==============================

// Bir dökümanı nasıl silebiliriz ?
// ==> deleteOne() / deleteMany() / remove()


43// ismi "En Ucuz" olan dökümanı siliniz
db.electronics.deleteOne({"name":"En Ucuz"})
// aynı isim de birden fazla döküman varsa
//ilk bulduğunu siler

44// ismi "Woww" olan bütün dökümanları silelim
db.electronics.deleteMany({"name":"Woww"})
// 2.yol
db.electronics.delete({"name":"Woww"})

45// olmayan bir dökümanı silmeye çalışırsam ne olur ?
db.electronics.deleteMany({"name":"XYZ"})
// Açıklama : olmayan dökümanı silmeye çalışılınca hata vermez ancak çıktıda deletedCount: 0 gözükür

46// bütün dökümanı silmak istersem 
db.electronics.deleteMany({})
//2.yol 
db.electronics.delete({}) 
// Açıklama : deleteMany() metodunun ilk parametresi olan filtre kısmını boş geçersem, bütün dökümanları seçmiş olurum.


// döküman eklemek için 
// ==> insertOne() / insertMany() / insert()

47// name : TV , price :123, mark:Samsung olan bir döküman ekleyin 
db.electronics.insertOne({"name":"TV","model":"Samsung","price":123})

48// 3 döküman eklemek istersem 
//(TV.123,Samsung) , (Radio,32, Panasonic),(ipod,150,Apple)
db.electronics.insertMany([{"name":"TV","price" : 123,"model":"Samsung"},{"name":"Radio","price":32, "model":"Panasonic" },{"name":"ipod","price": 150,"model":"Apple"}])


49// electronics collectiondan bütün kayıtları silelim
db.electronics.remove({})


