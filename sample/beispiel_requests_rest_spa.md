# Beispiel-Requests und Responses (REST Backend + SPA Frontend)

Annahmen:
- Base URL: `https://api.zauberbedarf.test/v1`
- Auth via Bearer Token: `Authorization: Bearer <token>`
- JSON als Datenformat

## UC-01: Kategorien durchsuchen

### Request
```http
GET /v1/categories HTTP/1.1
Host: api.zauberbedarf.test
Accept: application/json
```

### Response (200 OK)
```json
{
  "data": [
    {
      "id": "cat_wands",
      "name": "Zauberstaebe",
      "description": "Zauberstaebe fuer Hexen und Zauberer"
    },
    {
      "id": "cat_books",
      "name": "Buecher",
      "description": "Magische Lehr- und Fachbuecher"
    }
  ],
  "meta": {
    "count": 2
  }
}
```

---

## UC-02: Artikel suchen und filtern

### Request
```http
GET /v1/products?search=zauberstab&category=cat_wands&minPrice=3&maxPrice=15&inStock=true&page=0&size=20 HTTP/1.1
Host: api.zauberbedarf.test
Accept: application/json
```

### Response (200 OK)
```json
{
  "content": [
    {
      "id": "prod_wand_holly_001",
      "name": "Stechpalme-Zauberstab",
      "price": 7.0,
      "currency": "GALLEON",
      "inStock": true,
      "categoryId": "cat_wands"
    },
    {
      "id": "prod_book_potions_001",
      "name": "Fortgeschrittene Zaubertrankkunst",
      "price": 5.0,
      "currency": "GALLEON",
      "inStock": true,
      "categoryId": "cat_books"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```

---

## UC-03: Artikeldetails anzeigen

### Request
```http
GET /v1/products/prod_wand_holly_001 HTTP/1.1
Host: api.zauberbedarf.test
Accept: application/json
```

### Response (200 OK)
```json
{
  "data": {
    "id": "prod_wand_holly_001",
    "name": "Stechpalme-Zauberstab",
    "description": "11 Zoll, Phoenixfederkern, passend fuer praezise Zauber",
    "price": 7.0,
    "currency": "GALLEON",
    "inStock": true,
    "stock": 14,
    "categoryId": "cat_wands",
    "imageUrl": "https://cdn.zauberbedarf.test/products/prod_wand_holly_001.jpg"
  }
}
```

---

## UC-04: Anmelden

### Request
```http
POST /v1/auth/login HTTP/1.1
Host: api.zauberbedarf.test
Content-Type: application/json
Accept: application/json

{
  "email": "harry.potter@hogwarts.test",
  "password": "Expelliarmus123!"
}
```

### Response (200 OK)
```json
{
  "data": {
    "accessToken": "eyJhbGciOi...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": "usr_harry",
      "name": "Harry Potter",
      "role": "buyer"
    }
  }
}
```

## UC-04: Abmelden

### Request
```http
POST /v1/auth/logout HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Accept: application/json
```

### Response (204 No Content)
```json
{}
```

---

## UC-05: Artikel in Warenkorb legen

### Request
```http
POST /v1/cart/items HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Content-Type: application/json
Accept: application/json

{
  "productId": "prod_wand_holly_001",
  "quantity": 2
}
```

### Response (201 Created)
```json
{
  "data": {
    "itemId": "item_abc123",
    "productId": "prod_wand_holly_001",
    "quantity": 2,
    "unitPrice": 7.0,
    "lineTotal": 14.0
  }
}
```

---

## UC-06: Warenkorb anzeigen

### Request
```http
GET /v1/cart HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Accept: application/json
```

### Response (200 OK)
```json
{
  "data": {
    "items": [
      {
        "itemId": "item_abc123",
        "productId": "prod_wand_holly_001",
        "name": "Stechpalme-Zauberstab",
        "quantity": 2,
        "unitPrice": 7.0,
        "lineTotal": 14.0
      }
    ],
    "subtotal": 14.0,
    "currency": "GALLEON"
  }
}
```

## UC-06: Menge im Warenkorb aendern

### Request
```http
PATCH /v1/cart/items/item_abc123 HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Content-Type: application/json
Accept: application/json

{
  "quantity": 3
}
```

### Response (200 OK)
```json
{
  "data": {
    "itemId": "item_abc123",
    "quantity": 3,
    "unitPrice": 7.0,
    "lineTotal": 21.0
  }
}
```

## UC-06: Artikel aus Warenkorb entfernen

### Request
```http
DELETE /v1/cart/items/item_abc123 HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Accept: application/json
```

### Response (204 No Content)
```json
{}
```

---

## UC-07: Checkout durchfuehren

### Request
```http
POST /v1/checkout HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Content-Type: application/json
Accept: application/json

{
  "shippingAddress": {
    "fullName": "Harry Potter",
    "street": "Ligusterweg 4",
    "city": "Little Whinging",
    "postalCode": "12345",
    "country": "UK"
  },
  "paymentMethod": "invoice"
}
```

### Response (201 Created)
```json
{
  "data": {
    "orderId": "ord_2026_0001",
    "status": "placed",
    "total": 21.0,
    "currency": "GALLEON",
    "placedAt": "2026-05-25T10:15:30Z"
  }
}
```

---

## UC-08: Bestellbestaetigung anzeigen

### Request
```http
GET /v1/orders/ord_2026_0001 HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Accept: application/json
```

### Response (200 OK)
```json
{
  "data": {
    "orderId": "ord_2026_0001",
    "status": "placed",
    "items": [
      {
        "productId": "prod_wand_holly_001",
        "name": "Stechpalme-Zauberstab",
        "quantity": 3,
        "unitPrice": 7.0,
        "lineTotal": 21.0
      },
      {
        "productId": "prod_book_hogwarts_001",
        "name": "Hogwarts: Eine Geschichte",
        "quantity": 1,
        "unitPrice": 4.0,
        "lineTotal": 4.0
      }
    ],
    "shippingAddress": {
      "fullName": "Harry Potter",
      "street": "Ligusterweg 4",
      "city": "Little Whinging",
      "postalCode": "12345",
      "country": "UK"
    },
    "paymentMethod": "invoice",
    "total": 25.0,
    "currency": "GALLEON"
  }
}
```

---

## UC-09: Bestellhistorie anzeigen

### Request
```http
GET /v1/orders?page=0&size=20 HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Accept: application/json
```

### Response (200 OK)
```json
{
  "content": [
    {
      "orderId": "ord_2026_0001",
      "status": "placed",
      "total": 25.0,
      "currency": "GALLEON",
      "createdAt": "2026-05-25T10:15:30Z"
    },
    {
      "orderId": "ord_2026_0002",
      "status": "shipped",
      "total": 8.0,
      "currency": "GALLEON",
      "createdAt": "2026-05-20T08:02:10Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```

---

## Optional: Fehlerbeispiel (Validierung)

### Request
```http
POST /v1/cart/items HTTP/1.1
Host: api.zauberbedarf.test
Authorization: Bearer <token>
Content-Type: application/json
Accept: application/json

{
  "productId": "prod_wand_holly_001",
  "quantity": 0
}
```

### Response (400 Bad Request)
```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "quantity must be greater than 0",
    "fields": [
      {
        "name": "quantity",
        "message": "must be >= 1"
      }
    ]
  }
}
```
