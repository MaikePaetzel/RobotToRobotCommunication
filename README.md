RobotToRobotCommunication
=========================

Repository zur Bachelorarbeit "Spielerkoordination in RoboCup-Fußballspielen mittels gesprochener Sprache"


Programmbeispiele ausführen
---------------------------
Der Quelltext als Maven-Projekt kann unter
https://github.com/MaikePaetzel/RobotToRobotCommunication
heruntergeladen werden und steht unter der CC BY-NC-SA 3.0 DE Lizenz.

Konsolenbenutzung (unter Linux und Mac)
---------------------------------------
**Voraussetzung:** Eine Installation von Java JDK 1.7 und Maven 3 ist Voraussetzung für die Ausführung des Programms.

**Kompilierung:** Unter Linux und Mac kann der Quelltext über die Konsole durch die Eingabe der Zeile "mvn clean install" im Git-Verzeichnis kompiliert werden.
Ausführung: Im Hauptordner liegt nach der erfolgreichen Kompilierung ein zusättzlicher Ordner target. Die Ausfühhrung des Programms geschieht im Hauptordner des Git-Verzeichnisses, in dem die Datei BA.jar liegt, durch Eingabe der
Zeile "java -jar BA.jar -s #nummer", wobei #nummer durch eine natürliche Zahl zwischen Null und Sieben ersetzt werden muss. Die Bedeutung der einzelnen Szenariennummern ist im Abschnitt "Ausfühhrbare Klassen" erklärrt.

Einbindung in eine IDE am Beispiel Eclipse (für alle Betriebssysteme)
---------------------------------------------------------------------

**Voraussetzung:** Es wird vorausgesetzt, dass die Maven Integration für Eclipse installiert ist. Diese kann jederzeit über den Eclipse-Marketplace kostenlos hinzugefügt werden.

**Import:**
  1. Unter Eclipse das Import-Fenster  öffnen.
  2. Import-Quelle ist ein existierendes Maven-Projekt aus dem übergeordneten Ordner Maven.
  3. Das Wurzelverzeichnis ist der Ordner, in dem die Datei pom.xml liegt. Diese
muss auch als Projekt ausgewählt werden.

Ausführbare Klassen
-------------------

**StartUp:** In der Klasse StartUp können alle in der Bachelorarbeit beschriebenen statistischen Auswertungen gestartet werden. Dazu sind folgende Parameter bei
der Eingabe erlaubt:
  - -s 0 analysiert eine einzelne Originalaufnahme auf den Sprachinhalt. Welche Datei verwendet wird, kann in der entsprechenden Klasse manuell geändert werden. Beim initialen auschecken des Repositories wird stets eine Datei mit
dem Inhalt "Distance. 3. Point. 7. 3. Meter." gewählt.
  - -s 1 analysiert den Ordner Micro/Microphondaten mit 100 Originalaufnahmen auf den Sprachinhalt.
  - -s 2 analysiert eine einzelne eSpeak-Idealdatei auf den Sprachinhalt. Welche Datei verwendet wird, kann in der entsprechenden Klasse manuell geändert werden. Beim initialen auschecken des Repositories wird stets eine Datei mit
dem Inhalt Distance. "3. Point. 7. 3. Meter." gewählt.
  - -s 3 analysiert den Ordner eSpeak/eSpeakdaten mit 600 Idealdaten auf den Sprachinhalt.
  - -s 4 analysiert den Ordner Pitch/RoboterErkennungsdaten mit 600 Idealdaten verschiedner Pitches darauf, mit welchem Pitch die Aufnahme erzeugt
wurde.
  - -s 5 gibt eine Matrix mit den Distanzwerten zurück, die jede Datei im Referenzdatensatz auf Idealdaten im Vergleich zu jedem anderen Referenzdaten-
satz hat.
  - -s 6 gibt eine Matrix mit den Distanzwerten zurück, die jede Datei im Referenzdatensatz auf Originaldaten im Vergleich zu jedem anderen Referenzda-
tensatz hat.
  - -s 7 gibt eine Matrix mit den Distanzwerten zurück, die jede Datei im Referenzdatensatz auf Idealdaten im Vergleich zu jedem anderen Referenzdatensatz auf Originaldaten hat.

Bei den Szenarien 1, 3 und 4 werden am Ende der Analyse die Testergebnisse in die
Datei Testergebnis/ausgabe.txt geschrieben. Alle Pfade zu Sound- oder Textdateien
liegen immer im Ordner resources.

**CreateXMLFile:** Diese Klasse nimmt als Parameter den Pfad zu einem Ordner
mit Referenzdaten und den Namen für die zu erzeugende XML-Datei inklusive
relativ zum Projektverzeichnis liegender Pfadangabe entgegen und erzeugt eine
XML-Datei aus dem Referenzdatenordner.

