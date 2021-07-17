# 21.04.2021

## Tagesziele
* Die Steuerung des Roboters ermöglichen

## Reflexion
Wir haben zuerst versucht mit einem MovePilot unsere Bewegungen auszuführen. Es war ein wenig kompliziert mit `pilot.arcForward` zu arbeiten. Die Funktionsweise von dieser Methode sehen sie [hier](http://www.lejos.org/ev3/docs/lejos/robotics/navigation/MovePilot.html#arcForward-double-). Wir haben uns danach dazu entschieden, dass wir einfach die Geschwindigkeit der einzelnen Motoren anpassen. Wir konnten diese Bewegungen-Methode mit dieser Variante viel einfacher Lösen.
