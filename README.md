# UEK-223-Group-6-Backend


## Checkliste

### Punkte (Backend-relevant)
- Testing: ___ / 5
- Versionsverwaltung: ___ / 2
- Umsetzung / Code: ___ / 8

### Backend - Setup
- [ ] Spring Boot startet ohne Fehler
- [ ] PostgreSQL angebunden
- [ ] JPA Entities korrekt gemappt
- [ ] Schema oder Migration vorhanden

### API & Security
- [ ] JWT Auth aktiv
- [ ] User Rollen geprüft
- [ ] Admin Rollen geprüft
- [ ] Anonym -> 401
- [ ] User -> Admin Endpoint -> 403
- [ ] Admin -> Admin Endpoint -> 200

### Custom List - Use-Cases (Backend)

#### UC1 Create Listeneintrag
- [ ] POST Endpoint vorhanden
- [ ] Validierung Titel min 3
- [ ] Validierung Text max 500
- [ ] Wichtigkeit Enum (LOW/MEDIUM/HIGH)
- [ ] Erstellungsdatum automatisch
- [ ] 201 Created bei Erfolg

#### UC2 Update Listeneintrag
- [ ] PUT oder PATCH Endpoint vorhanden
- [ ] Nur Owner oder Admin erlaubt
- [ ] Validierung greift
- [ ] 200 OK bei Erfolg

#### UC3 Delete Listeneintrag
- [ ] DELETE Endpoint vorhanden
- [ ] Nur Owner oder Admin erlaubt
- [ ] 204 No Content bei Erfolg

#### UC4 User sieht eigene Einträge
- [ ] GET Endpoint nur eigene Einträge
- [ ] Pagination 10
- [ ] Sortierung Wichtigkeit Datum Titel
- [ ] Filter Wichtigkeit

#### UC5 Admin sieht alle Einträge
- [ ] GET Endpoint alle Einträge
- [ ] Pagination 10
- [ ] Sortierung User Wichtigkeit Datum
- [ ] Filter User oder Wichtigkeit

### Fehlerbehandlung
- [ ] 400 Validation Error
- [ ] 401 kein Token
- [ ] 403 keine Berechtigung
- [ ] 404 nicht gefunden
- [ ] 409 Konflikt
