# Copenhagen Cupcakes

Webshop til bageriet Copenhagen Cupcakes, lavet som 2. semesterprojekt på datamatikeruddannelsen (EK Lyngby).
Kunder kan sammensætte cupcakes af en bund og en top, lægge dem i kurven og betale med saldoen på deres konto.
Admin kan se alle ordrer og alle kunder med deres ordrer og slette en ordre. Beløb indsættes på en kundes saldo direkte i databasen via pgAdmin.


## Gruppe
- Besima
- Nasta
- Rebecca
- Louise

## Teknologier
- Java 21, Maven
- Javalin 7 og Thymeleaf
- PostgreSQL med JDBC og HikariCP
- HTML og CSS

## Kom i gang
1. Opret en database med navnet `cupcake` i pgAdmin.
2. Kør `docs/database/cupcake.sql` i Query Tool (F5). Scriptet opretter tabellerne, bunde, toppe og to testbrugere.
3. Tjek brugernavn og kode til databasen i `Main.java` (standard: `postgres` / `postgres`).
4. Kør `Main` og åbn http://localhost:7070

### Testbrugere
| Rolle | Email | Kode |
|---|---|---|
| Admin | admin@cupcake.dk | 1234 |
| Kunde | kunde@mail.dk | 1234 |

## Projektstruktur
```
src/main/java
├── configuration   ThymeleafConfig
├── controllers     routes og håndtering af requests
├── entities        Bottom, Topping, User, Order, Orderline
├── persistence     ConnectionPool og mappers (SQL)
├── services        OrderService (regler for kurv og betaling)
├── exceptions      DatabaseException
└── Main            starter Javalin
src/main/resources
├── public          CSS og billeder
└── templates       Thymeleaf-sider (.html)
src/test/java       unit tests og integrationstests af mapperne
docs                domænemodel, ERD, navigationsdiagram og SQL-script
rapport             rapport.pdf
```

## Dokumentation
- Domænemodel: `docs/diagrams/domaenemodel.puml`
- ERD: `docs/database/erd.mmd`
- Navigationsdiagram: `docs/diagrams`
- Rapport: `rapport/rapport.pdf`
- User stories og kanban: GitHub Projects

## Arbejdsgang
- Hver user story laves på sin egen branch, fx `feature/us-2-opret-konto`.
- Dokumentation laves på `docs/...`-branches.
- Alt kommer ind på `master` via en pull request, som et andet gruppemedlem reviewer.