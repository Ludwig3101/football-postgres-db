# Fußball-Verwaltung (Java/JDBC)

Dieses Projekt ist eine Java-Anwendung zur Verwaltung von Fußballdaten, entwickelt im Rahmen einer Studienleistung. Es demonstriert die Anbindung und Interaktion mit einer relationalen Datenbank über JDBC ohne den Einsatz von ORM-Frameworks.

## Funktionsumfang

Das Programm führt beim Start (`Main.java`) automatisch folgenden Workflow durch:

1.  **Verbindungsaufbau:** Connect zu einer lokalen PostgreSQL-Datenbank.
2.  **Datenbank-Setup:** Löschen alter Tabellen und Neuerstellung des Schemas (`Liga`, `Verein`, `Spieler`, `Spiel`) unter Berücksichtigung von Fremdschlüssel-Beziehungen.
3.  **Data Seeding:** Importieren von Beispieldaten mittels Batch-Processing.
4.  **Datenauswertung:** Ausführung definierter SQL-Abfragen und Ausgabe der Ergebnisse in der Konsole.
    * Verwendung von `JOIN`s zur Verknüpfung von Tabellen.
    * Aggregationen (z. B. Durchschnittswerte) und Sortierungen.

## Verwendete Technologien

* **Sprache:** Java (Temurin 25)
* **Datenbank:** PostgreSQL
* **Treiber:** postgresql-42.7.8
* **IDE:** IntelliJ IDEA 2025.3

## Nutzung

Voraussetzung ist eine laufende PostgreSQL-Instanz auf `localhost:5432` mit einer Datenbank namens `Studienleistung`. Die Zugangsdaten sind in der `Main`-Klasse konfiguriert.
