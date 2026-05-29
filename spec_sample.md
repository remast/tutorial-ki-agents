# Feature: Push einer einzelnen Policy kann manuell ausgelöst werden

Einen Button auf der Policen-Seite hinzufügen, um diese einzelne Police an Schaden Service zu übertragen. Der Policy-Push kann mit einer Policynummer (z. B. AXG0HF2BB2S5) ausgelöst werden und gibt HTTP-Status 200 zurück, wenn erfolgreich. Bei erfolgreichem Push wird der Benutzer z. B. mit einer Growl-Benachrichtigung informiert.


### Request

```
POST http://localhost:9090/api/v1/claim-info/policy/AXG0HF2BB2S5/push
```

### Response
```
HHTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Length: 0
Date: Thu, 15 Jan 2026 13:34:24 GMT

<Response body is empty>

Response code: 200; Time: 84ms (84 ms); Content length: 0 bytes (0 B)
```
