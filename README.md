# Opis projektu

Celem projektu miało być stworzenie aplikacji udsotępniąjącej i kontrolującej informacje o produktach przechowywanych w
zautomatyzowanym magazynie typu Autostore, paczkomaty itp. Cała aplikacja budowana jest jako pojedynczy FatJAR, może być
uruchomiona prze docker-compose, lub przez IntelliJ (ewentualnie `gradle bootRun`) po wcześniejszym uruchomieniu
zależności przez docker-compose (można użyć skryptu `./services.sh run-deps`, analogicznie cały stack można uruchomić
przez `./services.sh run`).

## Backend

* Zrealizowany w technologiach: Java, Spring Boot, Spring Security, PostgreSQL, S3 (MinIO kompatybilne z interfejsem S3)

* Umożliwia tworzenie produktów i odpytywanie o nie. Można zapytać o konkretne `productId`, pobrać wszystkie produkty,
  albo wyszukiwać po kodzie kreskowym.

* Autoryzacja użytkowników w oparciu o role i protokół BasicAuth.
    * Testowych użytkowników można stworzyć przez `./services.sh create-test-users`. Zapytania POST są wysyłane curlem
      na endpoint `/register`
    * `jan.kowalski_ro`:`husky1` ma uprawnienia tylko do czytania produktów
    * `jan.kowalski_rw`:`hunter2` ma uprawnienia do czytania i tworzenia produktów
    * Nazwy wszystkich użytkowników można podejrzeć na endpoincie `/users`
    * Jest też dostępny endpoint `/auth`, w zamyśle ma służyć tylko do sprawdzenia czy dane uwierzytelniania są poprawne

* Stworzenie przykładowego produktu:

```
curl -vv -X PUT -H "Content-Type: application/json" --user jan.kowalski_rw:hunter2 -d '{"productId": "TEST-101", "description": "Can of cola", "storageTemperature": "AMBIENT", "heightInMillimeters": "101", "widthInMillimeters": 60, "lengthInMillimeters": "60", "weightInKilograms": "0.33", "barcodes": ["123", "0001", "110012"]}' localhost:8080/v1/products/TEST-101
```

* Zapytanie o wszystkie produkty

```
curl -vv -X GET -H "Content-Type: application/json" --user jan.kowalski_ro:husky1 localhost:8080/v1/products
```

* Zapytanie o konkretny produkt

```
curl -vv -X GET -H "Content-Type: application/json" --user jan.kowalski_ro:husky1 localhost:8080/v1/products/TEST-101
```

* Wyszukiwanie po barkodzie

```
curl -vv -X GET -H "Content-Type: application/json" --user jan.kowalski_ro:husky1 localhost:8080/v1/barcodes/123
```

* Aplikacja umożliwia też zapisywanie obrazków produktów - są przechowywane w lokalnej instancji serwisu kompatybilnego
  z Amazon S3, na wszystkich produktowych GETach są zwracane URLe do tych obrazków. Dodanie obrazka (cola.png z repozytorium)

```
curl -vv -X PUT -H "Content-Type: image/png" --data-binary "@cola.png" --user jan.kowalski_rw:hunter2 localhost:8080/v1/products/TEST-101/image
```

Obrazki można podejrzeć po zwróconych URLach, albo w interfejsie S3 - powinien być dostępny na `localhost:7000` (
credentiale: `local.s3`:`local.s3`)

## Frontend

* Zrealizowany w React i JavaScript, serwowany bezpośrednio z backendu przez Spring Boota

* ReactRouter użyty do wyświetlania kilku podstron jako SPA

* Axios zaciąga informacje o wszystkich produktach z backendu (`v1/products`) i pokazuje je w accordionie. Przed
  pierwszym pobraniem przeglądarka powinna poprosić o użytkownika i hasło (patrz: użytkownicy z sekcji backendowej). Nie
  udało mi się bardziej zintegrować autoryzacji z frontendem (np. jako `LoginComponent`).
* Accordion jest generowany dynamicznie, pokazuje obrazki z S3, formatuje dane produktów. Niestety zabrakło czasu, żeby
  zrobić niektóre elementy lepiej/zrobić inne komponenty z opisu projektów.
