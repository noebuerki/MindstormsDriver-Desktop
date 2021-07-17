# Benutzerhandbuch

## Benötigte Materialien und Programme

* Mindstorms EV3
* Computer mit Tastatur, Bluetooth, JRE 11 und JavaFX
* [Rufus](https://github.com/pbatard/rufus/releases/download/v3.14/rufus-3.14.exe)
* Micro-SD-Karte mit 8 bis 32 GB
* [LeJOS Betriebssystem-Image](/assets/mindstorms/LeJOS.7z)
* [Desktopapplikation](/assets/desktop/Mindstorms-Driver.jar)
* [Mindstorms Applikation](/assets/mindstorms/Mindstorms-Driver.jar)
* [Setup-Script](/assets/mindstorms/setup-script.bat);

### Optionale Materialien und Programme

#### USB Verbindung → nicht empfohlen
* [USB-A zu USB-Mini-B Kabel](https://www.bricklink.com/v2/catalog/catalogitem.page?P=10916#T=C&C=11)
* [RNDIS-Treiber](/assets/mindstorms/RNDIS)
  
#### Eingabegeräte
* Saitek X52 Joystick
* PlayStation DualShock 4 Gamepad

## Einmaliges Setup

### Zusammenbau Mindstorms

Der Mindstorms muss nach folgender [Anleitung](/assets/mindstorms/Mindstorms-Bauanleitung.pdf) zusammengebaut werden.
Dabei muss auf die genaue Verkabelung geachtet werden.

### Mindstorms Setup
1. Das LeJOS-Image muss mit Rufus auf die Micro-SD-Karte geladen werden. Dazu muss dieses zuerst mit 7Zip entpackt werden.
2. Die Micro-SD-Karte muss im auf der linken Seite des Mindstorms installiert sein.
3. Der Mindstorms muss mit einem Klick auf den zentralen Knopf gestartet werden.
4. Unter dem Menu-Punkt Bluetooth muss `Visibility` auf `ON` geändert werden.
  
### Verbindungs-Setup 
1. Auf dem Computer muss Bluetooth aktiviert werden.
2. Auf dem Computer müssen die Adapteroptionen in der Systemsteuerung geöffnet werden.
3. Mit einem rechts-Klick auf das Gerät `Bluetooth-Netzwerkverbindung` die Option `Bluetooth-Netzwerkgeräte anzeigen` angeklickt werden.
4. Im neuen Fenster muss danach der Punkt `Bluetooth Gerät hinzufügen` geklickt werden.
5. Danach kann einfach dem Setup-Assistenten gefolgt werden, wobei der PIN standardmässig `1234` ist.
6. Sobald der Assistent beendet ist, sollte ein neues Gerät erscheinen.
7. Tipp: Mit einem rechts-Klick auf dem Gerät erscheint die Option eine Verbindung auf dem Desktop zu erstellen.
8. Danach kann mit einem rechts-Klick und zwei weiteren Klicks auf `Verbindung herstellen über` → `Zugriffspunkt` die Verbindung hergestellt werden.

### Software Setup

1. Die Desktopapplikation kann einfach als `jar` mit einem Doppelklick ausgeführt werden.
2. Die `jar` für den Mindstorms muss zuerst auf den Roboter geladen werden.
3. Die Dateien [Mindstorms-Driver.jar](/assets/mindstorms/Mindstorms-Driver.jar), [ip-adresse.txt](/assets/mindstorms/ip-adresse.txt) und [setup-script.bat](/assets/mindstorms/setup-script.bat) sich im gleichen Verzeichniss befinden.
4. Zuerst muss in der Datei [IP-Adresse](/assets/mindstorms/ip-adresse.txt) die IP des Mindstorms gespeichert werden.
5. Danach muss das [Setup-Script](/assets/mindstorms/setup-script.bat) ausgeführt werden. Dieses basiert auf einem Befehl von SUEG 2018.
6. Im CMD Terminal muss anschliessend noch der PIN, standardmässig `1234`, des Mindstorms eingegeben werden.

## Wiederkehrendes Setup

1. Auf dem Desktop muss die `jar` gestartet werden.
2. Der Mindstorms muss mit einem Klick auf den mittleren Button gestartet werden.
3. Die Verbindung zum Mindstorms muss mit einem rechts-Klick auf die Verknüpfung und `Verbindung herstellen über` → `Zugriffspunkt` hergestellt werden.
4. Auf dem Roboter muss die `jar` im Menu `Programs` gestartet werden.
5. Auf dem Mindstorms wir nach dem start der `jar` die IP angezeigt welche auf dem Desktop eingeben werden muss.
6. Mit einem Klick auf `Verbinden` kann anschliessend der Verbindungsaufbau gestartet werden.

## Unterstützte Eingabegeräte

### Tastatur

Es wird jede Tastatur unterstützt. Dabei ist die Tastenbelegung wie folgt:
* `W` → Vorwärts
* `A` → Links
* `S` → Rückwärts
* `D` → Rechts
* `Space` 'Arm' senken oder heben

Diese Eingaben können beliebig kombiniert werden.

### Gamepad / Controller

Die volle Funktion und richtige Tastenbelegung wird nur beim DualShock 4 Controller
gewährleistet. Dabei sind die Tasten wie folgt belegt:

* `R2` → Nach Vorne
* `L2` → Nach Hinten
* `Linker Analog-Stick` → Links / Rechts mit der x-Achse
* `X` → 'Arm' senken oder heben

Diese Eingaben können beliebig kombiniert werden.

### Joystick

Die volle Funktion und richtige Tastenbelegung wird nur beim Saitek X52 Joystick
gewährleistet. Dabei funktioniert die Steuerung wie folgt:

* `X-Achse` → Links / Rechts
* `Y-Achse` → Vorwärts / Rückwärts
* `Z-Achse` → Links / Rechts (sehr langsam)
* `Fire-Knopf` → Arm heben oder senken


## Aufnahmen

### Aufnahmen erstellen
Um eine neue Aufnahme zu erstellen, muss der Computer mit dem Mindstorms verbunden sein.
Danach kann in der `Fahransicht` eine Aufnahme mit einem Klick auf den Knopf `Aufnehmen` gestartet werden.
Diese kann später mit einem Klick auf den gleichen Knopf wieder beendet werden.

### Aufnahmen verwalten
Um die gespeicherten Aufnahmen zu verwalten, muss in die Bibliothek gewechselt werden. Diese ist auch dann verfügbar, wenn kein 
Mindstorms verbunden ist, allerdings nur mit reduzierter Funktionalität. In dieser Ansicht werden alle Aufnahmen aufgelistet. 

### Aufnahmen abspielen
Um eine Aufnahme abzuspielen, kann in der Bibliothek bei der gewünschten Aufnahme einfach auf den `Play` Knopf geklickt werden.

### Karte anzeigen
Um die Karte zu einer Aufnahme anzuzeigen, kann in der Bibliothek bei der gewünschten Aufnahme einfach auf den `Karten` Knopf geklickt werden.

### Aufnahmen löschen
Um eine Aufnahme zu löschen, kann in der Bibliothek bei der gewünschten Aufnahme einfach auf den `Löschen` Knopf geklickt werden.