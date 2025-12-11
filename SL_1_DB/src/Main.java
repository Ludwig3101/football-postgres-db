import java.sql.*;


// Datenbank für Speicherung von Ligen, Vereinen, Spielen und Spielern im Fußball

public class Main {
    static Connection connection;

    static void main() {

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/Studienleistung";
            String nutzer = "postgres";
            String pw = "blubb";

            // Verbindung herstellen
            connection = DriverManager.getConnection(url, nutzer, pw);

            // Aufrufen von den Methoden
            System.out.println("connection hat geklappt");
            structureDB();
            addData();
            datenAbfrage();

            // Verbindung beenden
            connection.close();

        } catch (ClassNotFoundException | SQLException E) {
            System.out.println("Fehler: " + E.getMessage());
        }

    }

    // Strukturierung der einzelnen Tabellen
    static void structureDB() {
        try {
            Statement statement = connection.createStatement();
            // Alte Tabellen löschen/aufräumen
            statement.executeUpdate("DROP TABLE IF EXISTS Liga CASCADE");
            // [ LigaID | Name | LigaHöhe ]
            statement.executeUpdate("""
                        CREATE TABLE Liga (
                            LigaID integer PRIMARY KEY,
                            Name text NOT NULL,
                            LigaHöhe integer NOT NULL
                        )
                    """);
            System.out.println("Liga Tabelle erstellt");


            // Alte Tabellen löschen/aufräumen
            statement.executeUpdate("DROP TABLE IF EXISTS Verein CASCADE");
            // [ VereinID | Name | Stadt | Gründungsjahr | LigaID ]
            // Stadt als eigene Tabelle wäre optimaler, aber aufwand zu groß und unnötig
            // für nur eine Spalte
            statement.executeUpdate("""
                        CREATE TABLE Verein (
                            VereinID integer PRIMARY KEY,
                            Name text NOT NULL,
                            Stadt text NOT NULL,
                            Gründungsjahr integer,
                            LigaID integer REFERENCES Liga(LigaID)
                        )
                    """);
            System.out.println("Tabelle Verein erstellt");

            // Alte Tabellen löschen/aufräumen
            statement.executeUpdate("DROP TABLE IF EXISTS Spieler CASCADE");
            // [ SpielerID | Vorname | Nachname | Rückennummer | Marktwert | VereinID ]
            statement.executeUpdate("""
                        CREATE TABLE Spieler (
                            SpielerID integer PRIMARY KEY,
                            Vorname text,
                            Nachname text NOT NULL,
                            Rückennummer integer,
                            Marktwert integer,
                            VereinID integer NOT NULL REFERENCES Verein(VereinID)
                        )
                    """);
            System.out.println("Tabelle Spieler erstellt");

            // Alte Tabellen löschen/aufräumen
            statement.executeUpdate("DROP TABLE IF EXISTS Spiel CASCADE");
            // [ SpielID | Spieltag | Datum | HeimID | GastID | ToreHeim | ToreGast ]
            statement.executeUpdate("""
                        CREATE TABLE Spiel (
                            SpielID integer PRIMARY KEY,
                            Spieltag integer NOT NULL,
                            Datum text,
                            HeimID integer NOT NULL REFERENCES Verein(VereinID),
                            GastID integer NOT NULL REFERENCES Verein(VereinID),
                            ToreHeim integer,
                            ToreGast integer
                        )
                    """);
            System.out.println("Tabelle Spiel erstellt");

            statement.close();


        } catch (SQLException E) {
            System.out.println("Exception: " + E.getMessage());
        }
    }

    // Befüllen der Tabellen mit Daten
    // Spieler und Vereinsinfos sind von https://www.transfermarkt.de/
    static void addData() {
        try {
            Statement statement = connection.createStatement();

            // Hinzufügen der 1. bis 4. Ligen
            statement.addBatch("INSERT INTO Liga VALUES (1, '1. Bundesliga', 1)");
            statement.addBatch("INSERT INTO Liga VALUES (2, '2. Bundesliga', 2)");
            statement.addBatch("INSERT INTO Liga VALUES (3, '3. Liga', 3)");
            statement.addBatch("INSERT INTO Liga VALUES (4, 'Regionalliga Bayern', 4)");
            statement.addBatch("INSERT INTO Liga VALUES (5, 'Regionalliga Südwest', 4)");
            statement.addBatch("INSERT INTO Liga VALUES (6, 'Regionalliga West', 4)");
            statement.addBatch("INSERT INTO Liga VALUES (7, 'Regionalliga Nord', 4)");
            statement.addBatch("INSERT INTO Liga VALUES (8, 'Regionalliga Nordost', 4)");
            statement.executeBatch();

            // Hinzufügen von Vereinen
            statement.addBatch("INSERT INTO Verein VALUES (101, 'FC Bayern München', 'München', 1900, 1)");
            statement.addBatch("INSERT INTO Verein VALUES (102, 'Borussia Dortmund', 'Dortmund', 1909, 1)");
            statement.addBatch("INSERT INTO Verein VALUES (103, 'Hamburger SV', 'Hamburg', 1887, 1)");
            statement.addBatch("INSERT INTO Verein VALUES (104, 'Hertha BSC', 'Berlin', 1892, 2)");
            statement.addBatch("INSERT INTO Verein VALUES (105, 'FC Nürnberg', 'Nürnberg', 1900, 2)");
            statement.addBatch("INSERT INTO Verein VALUES (106, 'Dynamo Dresden', 'Dresden', 1953, 2)");
            statement.addBatch("INSERT INTO Verein VALUES (107, 'SSV Jahn Regensburg', 'Regensburg', 1889, 3)");
            statement.addBatch("INSERT INTO Verein VALUES (108, 'FC Hansa Rostock', 'Rostock', 1965, 3)");
            statement.addBatch("INSERT INTO Verein VALUES (109, 'MSV Duisburg', 'Duisburg', 1902, 3)");
            statement.executeBatch();

            // Hinzufügen von Spielern
            // Spieler FC Bayern
            statement.addBatch("INSERT INTO Spieler VALUES (1, 'Harry', 'Kane', 9, 75000000, 101)");
            statement.addBatch("INSERT INTO Spieler VALUES (2, 'Manuel', 'Neuer', 1, 4000000, 101)");
            statement.addBatch("INSERT INTO Spieler VALUES (3, 'Jamal', 'Musiala', 42, 140000000, 101)");
            // Spieler BVB
            statement.addBatch("INSERT INTO Spieler VALUES (4, 'Gregor', 'Kobel', 1, 40000000, 102)");
            statement.addBatch("INSERT INTO Spieler VALUES (5, 'Julian', 'Brandt', 10, 25000000, 102)");
            // Spieler HSV
            statement.addBatch("INSERT INTO Spieler VALUES (6, 'Robert', 'Glatzel', 9, 2000000, 103)");
            statement.addBatch("INSERT INTO Spieler VALUES (7, 'Ransford', 'Königsdörffer', 11, 6000000, 103)");
            // Spieler Hertha
            statement.addBatch("INSERT INTO Spieler VALUES (8, 'Fabian', 'Reese', 11, 5000000, 104)");
            // Spieler FCN
            statement.addBatch("INSERT INTO Spieler VALUES (9, 'Berkay', 'Yilmaz', 21, 2500000, 105)");
            // Spieler Dynamo
            statement.addBatch("INSERT INTO Spieler VALUES (10, 'Stefan', 'Kutschke', 30, 150000, 106)");
            // Spieler Jahn
            statement.addBatch("INSERT INTO Spieler VALUES (11, 'Felix', 'Gebhardt', 1, 450000, 107)");
            // Spieler Hansa
            statement.addBatch("INSERT INTO Spieler VALUES (12, 'Emil', 'Holten', 11, 600000, 108)");
            // Spieler MSV
            statement.addBatch("INSERT INTO Spieler VALUES (13, 'Rasim', 'Bulic', 6, 400000, 109)");

            statement.executeBatch();

            // Hinzufügen von Spielen
            // Bayern gegen Dortmund
            statement.addBatch("INSERT INTO Spiel VALUES (5001, 1, '2025-01-19', 101, 102, 5, 2)");
            // HSV gegen Bayern
            statement.addBatch("INSERT INTO Spiel VALUES (5002, 2, '2025-02-20', 103, 101, 0, 3)");
            // Hertha gegen Nürnberg
            statement.addBatch("INSERT INTO Spiel VALUES (5003, 1, '2025-03-22', 104, 105, 3, 3)");
            // Dresden gegen Hertha
            statement.addBatch("INSERT INTO Spiel VALUES (5004, 2, '2025-04-21', 106, 104, 1, 0)");
            // Regensburg gegen Duisburg
            statement.addBatch("INSERT INTO Spiel VALUES (5005, 1, '2025-05-23', 107, 109, 2, 1)");
            // Rostock gegen Regensburg
            statement.addBatch("INSERT INTO Spiel VALUES (5006, 2, '2025-06-24', 108, 107, 0, 0)");
            // Dortmund gegen Dresden (Pokal)
            statement.addBatch("INSERT INTO Spiel VALUES (5007, 99, '2025-07-18', 102, 106, 5, 0)");

            statement.executeBatch();

            statement.close();

        } catch (SQLException e) {
            System.out.println("Fehler: " + e);
        }
    }

    static void datenAbfrage() {
        System.out.println("--Datenabfrage--");

        try {
            Statement statement = connection.createStatement();
            ResultSet rs;

            // Alle Vereine aus der 2. Liga
            System.out.println("1. Alle Vereine der 2. Bundesliga:");
            rs = statement.executeQuery("SELECT Name, Stadt FROM Verein WHERE LigaID = 2");
            System.out.println(ResultToString(rs));
            rs.close();

            // Sortieren von allen Spielern nach Marktwert absteigend
            System.out.println("2. Sortieren von allen Spielern nach Marktwert absteigend:");
            rs = statement.executeQuery("SELECT Nachname, Marktwert FROM Spieler ORDER BY Marktwert DESC");
            System.out.println(ResultToString(rs));
            rs.close();

            // Verknüpfung der Spieler mit ihrem Verein statt nur ID
            System.out.println("3. Verknüpfen von Spielern mit ihrem Verein:");
            rs = statement.executeQuery("""
                        SELECT Spieler.Nachname, Verein.Name
                        FROM Spieler
                        JOIN Verein ON Spieler.VereinID = Verein.VereinID
                    """);
            System.out.println(ResultToString(rs));
            rs.close();

            // Verknüpfung von Vereinen mit ihrer Liga statt nur ID
            System.out.println("4. Verknüpfen von Vereinen mit ihrer Liga;");
            rs = statement.executeQuery("""
                        SELECT Verein.Name, Liga.Name
                        FROM Verein
                        JOIN Liga ON Verein.LigaID = Liga.LigaID
                    """);
            System.out.println(ResultToString(rs));
            rs.close();

            // Berechnung des durchschnittlichen Marktwerts von allen Spielern
            System.out.println("5. Durchschnittlicher Marktwert aller Spieler:");
            rs = statement.executeQuery("SELECT AVG(Marktwert) AS Durchschnittswert FROM Spieler");
            System.out.println(ResultToString(rs));
            rs.close();

            // Darstellung der einzelnen Spiele mit Vereinsnamen und Ergebnis
            // Doppelter Join für Heim und Gastmannschaft
            System.out.println("6. Darstellung der einzelnen Spiele mit Vereinsnamen und Ergebnis:");
            rs = statement.executeQuery("""
                        SELECT Datum, H.Name AS Heimverein, G.Name AS Gastverein, ToreHeim, ToreGast
                        FROM Spiel
                        JOIN Verein H ON Spiel.HeimID = H.VereinID
                        JOIN Verein G ON Spiel.GastID = G.VereinID
                    """);
            System.out.println(ResultToString(rs));
            rs.close();

            // Finden von allen Unentschieden
            System.out.println("7. alle Spiele mit Unentschieden:");
            rs = statement.executeQuery("""
                        SELECT SpielID, ToreHeim, ToreGast FROM Spiel
                        WHERE ToreHeim = ToreGast
                    """);
            System.out.println(ResultToString(rs));
            rs.close();

            // Vereine, wo die Städte mit D anfangen
            System.out.println("8. Vereine wo die Städte mit D anfangen:");
            rs = statement.executeQuery("SELECT Name, Stadt FROM Verein WHERE Stadt LIKE 'D%'");
            System.out.println(ResultToString(rs));
            rs.close();

            // Tordifferenz von jedem Spiel (Tore heim - gast)
            System.out.println("9. Tordifferenz bei jedem Spiel:");
            rs = statement.executeQuery("""
                        SELECT SpielID, (ToreHeim - ToreGast) AS Tordifferenz FROM Spiel
                    """);
            System.out.println(ResultToString(rs));
            rs.close();

            // Spieler und in welcher Liga sie spielen (Hier nur liga 1)
            // dreifach JOIN: Spieler → Verein → Liga
            System.out.println("10. Spieler und in welcher Liga sie spielen:");
            rs = statement.executeQuery("""
                        SELECT Spieler.Nachname, Liga.Name
                        FROM Spieler
                        JOIN Verein ON Spieler.VereinID = Verein.VereinID
                        JOIN Liga ON Verein.LigaID = Liga.LigaID
                        WHERE Liga.LigaHöhe = 1
                    """);
            System.out.println(ResultToString(rs));
            rs.close();

            statement.close();

        } catch (SQLException E) {
            System.out.println("Exception: " + E.getMessage());
        }
    }

    // Funktion für Umwandlung von Resultset in String für die Ausgabe
    static String ResultToString(ResultSet RS) {
        StringBuilder SB = new StringBuilder();
        try {
            // Metadaten für Spalteninfos laden
            ResultSetMetaData Meta = RS.getMetaData();
            int SpaltenAnzahl = Meta.getColumnCount();

            // Kopfzeile mit Spaltennamen
            for (int i = 1; i <= SpaltenAnzahl; i++) {
                SB.append(Meta.getColumnLabel(i)).append("\t| ");
            }
            // Trennlinie zwischen Kopf und Inhalt
            SB.append("\n------------------------------------------------\n");

            // Durch alle Datenzeilen durchiterieren
            while (RS.next()) {
                for (int i = 1; i <= SpaltenAnzahl; i++) {
                    String wert = RS.getString(i);

                    // Wenn ein Feld leer ist, ist es "null"
                    if (wert == null) wert = "null";

                    SB.append(wert);
                    if (wert.length() < 8) SB.append("\t"); // Extra Tab bei kurzen Wörtern
                    SB.append("\t| ");
                }
                SB.append("\n"); // Zeilenumbruch nach jedem Datensatz
            }
        } catch (SQLException E) {
            return "Fehler: " + E.getMessage();
        }
        return SB.toString();
    }
}