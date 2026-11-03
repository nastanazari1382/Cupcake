# Gruppekontrakt – Copenhagen Cupcakes

## Gruppe og formål

**Medlemmer:** Besima, Nasta, Rebecca og Louise

**Projekt:** Copenhagen Cupcakes, en webshop i Java, Javalin, Thymeleaf og PostgreSQL (2. semester, EK Lyngby).

**Vores mål:**

- Vi afleverer et projekt, der virker og opfylder US-1 til US-8.
- Alle i gruppen kan forklare hele løsningen til eksamen, både backend og frontend.
- Vi holder det simpelt: først en prototype, der virker, derefter design.
- Vi har det godt sammen og hjælper hinanden.

## Møder og kommunikation

- **Morgenmøde kl. 9:** hvad laver jeg i dag, og hvad venter jeg på?
- **Statusmøde kl. 13:** hvad er merget, og hvad mangler?
- På hjemmearbejdsdage holder vi møderne på Discord/Teams.
- Vi skriver i gruppechatten, hvis vi er forsinkede eller sidder fast. Vi svarer inden for et par timer på hverdage.
- Vi siger det med det samme, hvis vi ikke kan nå vores opgave, så andre kan hjælpe.
- Beslutninger tages i fællesskab. Er vi uenige, stemmer vi, og flertallet bestemmer.

## Arbejdsfordeling

- Vi arbejder i **par**: Besima + Rebecca og Nasta + Louise. Torsdag arbejder alle fire sammen på US-1.
- I hvert par laver den ene **backend** (entity, mapper og SQL) og den anden **frontend** (HTML, controller og Thymeleaf).
- Vi **bytter roller hver dag**, så alle har prøvet begge dele.
- Opgaverne står på **kanban-boardet** med navn og dag, og vi flytter selv vores kort.
- Ingen arbejder alene om noget stort. Sidder man fast i mere end 30 minutter, spørger man gruppen.

## Roller

Vi følger kursets tre roller. Vi **skifter roller hver uge** og har **trukket lod** om, hvem der starter med hvad. Vi er fire og har tre roller, så to deler én rolle.

| Rolle | Uge 1 (2.–6. nov.) | Uge 2 (9.–12. nov.) |
| --- | --- | --- |
| Scrum master | Rebecca og Besima | Nasta og Louise |
| Product owner | Nasta | Rebecca |
| Tech lead | Louise | Besima |

**Scrum master** sørger for, at processen kører, og at vi arbejder godt sammen:

- Leder stand-up kl. 9 og statusmødet kl. 13 ud fra kanban-boardet.
- Sørger for, at gruppekontrakten overholdes, og indkalder til møde, hvis noget ikke fungerer.
- Sørger for, at alle bliver hørt, når vi diskuterer løsninger.
- Sørger for, at vi er forberedt til vejledning og ved, hvem der præsenterer.
- Har styr på ugens plan. Sygdom meldes til scrum master.
- Kontakter vejlederen ved problemer, vi ikke selv kan løse.

**Product owner** er kundens forlængede arm:

- Agerer kunde, når vi er i tvivl om kravene.
- Prioriterer vores opgaver på kanban-boardet.
- Tager beslutninger på kundens vegne og skriver dem i beslutningsloggen, så de kommer med i rapporten.
- Kontakter vejlederen ved større beslutninger.

**Tech lead** har det sidste ord om det tekniske og ansvaret for kodens kvalitet:

- Godkender eller afviser pull requests.
- Sørger for, at kodestandarder og Definition of Done overholdes.
- Sørger for, at der dokumenteres og testes som aftalt.
- Tager beslutningen ved uenighed om kodedesign, efter at alle er hørt, og skriver den i loggen.
- Kontakter vejlederen, hvis vi har brug for teknisk input.

## Git og kode

- Hver user story får en **hovedbranch** (fx `US4-logind`) og sub-branches til backend og frontend.
- Ingen committer direkte på `master`. Alt kommer ind via en **pull request**.
- Hver PR **reviewes af en anden** i gruppen, før den merges. Vi reviewer inden for en time i arbejdstiden.
- Vi bruger **squash and merge**.
- Vi **puller master hver morgen** og merger løbende, så vi undgår store konflikter.
- Commit-beskeder skrives i bydeform og forklarer kort, hvad der er gjort (fx "Tilføj UserMapper.login").
- Database og Java-kode er på **engelsk**, og siden og domænemodellen er på **dansk**.
- Vi følger kursets struktur: `entities`, `persistence`, `controllers`, `templates` og `public`.

## Deadlines og plan

| Dag | Hvad |
| --- | --- |
| Tir 3/11 | US-4 Log ind |
| Ons 4/11 (hjemme) | US-3 Kurv · US-2 Opret konto og dropdowns |
| Tor 5/11 | US-1 Bestil og betal (alle fire) |
| Fre 6/11 | US-7, US-8, US-5 og US-6. Om eftermiddagen tester og merger vi. **Koden er færdig.** |
| Man 9/11 | Rapport (alle fire) |
| Tor 12/11 kl. 12 | **Aflevering** |
| Man 16/11 | Prøveeksamen |

Kan en opgave ikke nås til tiden, siger vi det på statusmødet kl. 13, og så fordeler vi den om.

## Fravær og konflikter

- **Fravær og aftaler** (læge, arbejde og lignende) skrives i gruppekalenderen i god tid.
- Er man syg eller forsinket, skriver man i gruppechatten **før kl. 9**.
- Den, der har været væk, sætter sig selv ind i, hvad der er lavet, ved at læse status i chatten og på kanban-boardet.
- **Uenigheder** tages op hurtigt og direkte, men venligt, på næste møde og ikke bag om ryggen på nogen.
- Hvis en person gentagne gange ikke laver sine opgaver eller ikke dukker op:
  1. Vi tager en snak i gruppen.
  2. Hjælper det ikke, skriver vi det ned og aftaler en ny plan.
  3. Hjælper det stadig ikke, kontakter vi underviseren.

*Aftalt af hele gruppen, 3/11-2026.*
