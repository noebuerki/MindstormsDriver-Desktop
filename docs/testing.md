# Testkonzept

## Testprotokolle

Alle Testprotokolle befinden sich in folgendem [Ordner](/docs/assets/testprotocols).

## Allgemeine Vorbedingungen
In jeder Testumgebung muss folgendes vorausgesetzt sein:
- Der Mindstorms EV3 (mit leJOS) ist gestartet.
- Auf dem Mindstorms ist unsere Software installiert.
- Die Desktopapplikation ist gestartet.
- Der PC ist mit dem Mindstorms über Bluetooth verbunden.

Bei Fragen einfach das [Benutzerhandbuch](/docs/usermanual.md) durchlesen.

## Tests

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-01
Getestete User Story | [#1](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/1)
Vorbedingungen       | -
Ablauf               | 1. Der Anwender startet unsere Software auf dem Mindstorms im Menu `Programs`.<br>2. Der Anwender wartet eine halbe Minute.
Erwartetes Resultat  | Nachdem die Software vollständig geladen ist wird die IP-Adresse des Mindstorms und dessen Name angezeigt.

Abschnitt            | Inhalt
---------------------|--------
ID                   | ST-02
Getestete User Story | [#2](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/2)
Vorbedingungen       | - Der Anwender kennt die IP-Adresse des Mindstorms (siehe ST-01)
Ablauf               | 1. Der Anwender gibt die IP-Adresse welche auf dem Mindstorms erscheint in das Textfeld auf der Desktopapplikation ein.
Erwartetes Resultat  | Man kann die IP-Adresse in das Textfeld eingeben.

Abschnitt            | Inhalt
---------------------|--------
ID                   | ST-03
Getestete User Story | [#3](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/3)
Vorbedingungen       | - Die Software auf dem Mindstorms ist gestartet (Siehe ST-01).<br>- Die IP-Adresse des Mindstorms ist in der Desktopapplikation eingetragen (Siehe ST-02).
Ablauf               | 1. Der Anwender klickt auf Verbinden.
Erwartetes Resultat  | Nachdem auf den Verbinden-Knopf geklickt wurde, erscheint einen Ladebildschirm welcher anzeigt wie es um die Verbindung steht. Sobald diese Aufgebaut ist wird der Benutzer in die Fahransicht geleitet. Diese kann daran erkannt werden, dass oben links `Fahransicht` steht.

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-04
Getestete User Story | [#6](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/6), [#7](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/7),  [#8](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/8)
Vorbedingungen       | - Die Software des Mindstorms und die Desktopapplikation sind miteinander verbunden und der Benutzer befindet sich in der Fahransicht (Siehe ST-03).<br>- Ein Team-Mitglied hat die Steuerung erklärt.
Ablauf               | 1. Der Benutzer hält `W` gedrückt. <br>2. 5 Sekunden später drückt der Benutzer zusätzlich noch `D`.<br>3. Nach weiteren 3 Sekunden lässt der Benutzer alle Tasten los.
Erwartetes Resultat  | Der Roboter bewegt sich für 5 Sekunden gerade aus. Danach fährt der Roboter für 3 Sekunden eine Rechtskurve. Anschliessend bleibt der Roboter stehen.

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-05
Getestete User Story | [#4](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/4)
Vorbedingungen       | - Die Desktopapplikation wurde neu gestartet.
Ablauf               | 1. Der Benutzer gibt eine beliebige IP-Adresse ein. <br>2. Der Benutzer klickt auf Verbinden.
Erwartetes Resultat  | Nach 4 Sekunden erscheint folgender Text: "Die Verbindung konnte nicht hergestellt werden". Kurz darauf wird man zum Startbildschirm zurückgeworfen.

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-06
Getestete User Story | [#10](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/10)
Vorbedingungen       | - ST-03 wurde erfolgreich abgeschlossen<br> - Der Mindstorms ist mit der Desktopapplikation verbunden<br> - Der Benutzer befindet sich wieder in der Fahransicht
Ablauf               | 1. Der Benutzer klickt auf den Knopf mit der Beschriftung "Aufnehmen". <br>2. Der Benutzer wiederholt den Inhalt des Abschnittes ST-04.
Erwartetes Resultat  | Der Button ändert seine Farbe auf rot und der Text ändert sich zu "Beenden"

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-07
Getestete User Story | [#11](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/11)
Vorbedingungen       | - ST-06
Ablauf               | 1. Der Benutzer klickt auf "Beenden".
Erwartetes Resultat  | Bei erneutem Klicken wird der ändert sich die Farbe des Buttons auf blau und der Text ändert sich zu "Aufnehmen"

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-08
Getestete User Story | [#12](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/12)
Vorbedingungen       | - ST-07 wurde erfolgreich abgeschlossen
Ablauf               | 1. Der Benutzer klickt auf "Bibliothek".
Erwartetes Resultat  | Die vorher erstellte Aufnahme wird mit Datum und Zeit angezeigt.

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-09
Getestete User Story | [#13](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/13)
Vorbedingungen       | - ST-08 wurde erfolgreich abgeschlossen
Ablauf               | 1. Der Benutzer klickt bei der soeben erstellten Aufnahme auf den blauen Knopf mit einem Play-Icon
Erwartetes Resultat  | Der Roboter bewegt sich für 5 Sekunden gerade aus. Danach fährt der Roboter für 3 Sekunden eine Rechtskurve. Anschliessend bleibt der Roboter stehen.

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-10
Getestete User Story | [#15](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/15)
Vorbedingungen       | - ST-07 wurde erfolgreich abgeschlossen
Ablauf               | 1. Der Benutzer klickt auf den roten Knopf mit einem Abfalleimer-Icon
Erwartetes Resultat  | Die Aufnahme wird gelöscht und ist nun nicht mehr sichtbar.

Abschnitt            | Inhalt
---------------------|-------
ID                   | ST-11
Getestete User Story | [#10](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/10), [#11](https://git.bbcag.ch/inf-bl/be/2020/pm/mindstorms-driver/app/-/issues/11)
Vorbedingungen       | - ST-03 wurde erfolgreich abgeschlossen
Ablauf               | 1. Der Benutzer klickt auf "Aufnehmen".<br> 2. Der Benutzer klickt auf "Beenden" ohne eine Steuerungseingabe zu tätigen.
Erwartetes Resultat  | Die Aufnahme wird nicht gespeichert und ist somit beim Klick auf `Bibliothek` nicht sichtbar.
