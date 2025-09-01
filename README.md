# FINT Flyt Web Gateway Template

## Introduksjon
Dette repoet fungerer som en mal ("template") for å sette opp en ny *instance gateway* i FINT Flyt-plattformen.
Applikasjonen inkluderer eksempler på både enkel og avansert bruk:
- **Enkel**: prosesserer en enkelt instans uten collections og uten filopplasting.
- **Avansert**: demonstrerer bruk av collections og støtte for filopplasting.

## Teknologi
* Spring Boot 3.4
* Java 21
* Kotlin
* ktlint
* Blocking-arkitektur (ikke reaktiv)

## Avhengigheter
Prosjektet er bygget på følgende avhengigheter:
* `fint-kafka` v5.x
* `fint-kafka-request-reply` v5.x
* `fint-flyt-web-instance-gateway`
* `fint-flyt-web-resource-server`

## ktlint
`ktlint` brukes for å sikre enhetlig kodeformattering og stil i prosjektet.

### Bruk i IntelliJ
For å aktivere `ktlint` i IntelliJ:
1. Installer `ktlint`-pluginet via *Settings → Plugins*.
2. Gå til *Settings → Tools → ktlint* for konfigurasjon.
3. Aktiver *Distract Free Mode* for en ryddig arbeidsflate.
   I denne modusen brukes `ktlint`-formattering automatisk ved lagring.
