# Testprotokoll CalmBreeze-Timer

## Datum: 19.03.2024

## Tester: Castor Manuel Fernández Lado

## Zusammenfassung
Dieses Dokument enthält die Ergebnisse der manuellen Tests für die CalmBreeze-Timer Applikation.

## Funktionen und Szenarien

### 1. Login
- [ ] Teste das Login mithilfe des dafür vorgesehenen Buttons.

### 2. Animation
- [ ] Warte einige Sekunden, um zu überprüfen, ob die Anweisungen der Applikation funktionieren. Es sollten 4 Texte ein- und ausgeblendet werden. Die Texte lauten wie folgt: "To Enjoy the experience, place your smartphone on your chest and start your experience".

### 3. Countdown
- [ ] Überprüfe, ob ein 10-minütiger Countdown gestartet wird.
- [ ] Teste, ob der Countdown jede Sekunde aktualisiert wird.
- [ ] Überprüfe, ob der Countdown stets 4 Ziffern anzeigt.

### 4. Vibration
- [ ] Überprüfe, ob beim Stoppen des Countdowns eine 3 Sekunden lange Vibration erfolgt.

### 5. Benachrichtigung
- [ ] Teste, ob beim Beenden des Countdowns eine Benachrichtigung gesendet wird, in der steht, dass die Meditation erfolgreich beendet wurde.
- [ ] Teste, ob beim Klicken der Benachrichtigung eine Seite geöffnet wird, auf der in der Mitte der Seite "You Finished" steht.
- [ ] Überprüfe, ob die Benachrichtigung auch gesendet wird, wenn die Applikation nicht im Vordergrund läuft.

## Kommentare
In diesem Fall konnte ich die Atemfrequenzmessung nicht machen, da ich kein Smartphone zur verfügung hatte und dies aus dem Emulator aus, nicht Möglich war.

## Ergebnisse

### 1. Login
- Fake Login, funktioniert ohne Problem, beim klicken des Buttons wird man auf die nächste Seite weitergeleitet.

### 2. Animation
- Nach dem Warten von 10 Sekunden, konnte man den verlauf der Anweisungen 2 mal sehen, das ein und ausblenden der Texte funktioniert einwandfrei, die werden in guter geschwindigkeit und angezeigt, der Text war richtig.

### 3. Countdown
- 10 Minütiger Countdown wird gestartet.
- Countdown zeigt alle Sekunden an.
- Es werden in jedem Fall 4 ziffern angezeigt, im fall dass zum beispiel nurnoch eine Minute fehlt wird es folgendermassen angezeigt: 01:00.

### 4. Vibration
- Das Vibrieren des Smartphones sobald der Counter bei 00:00 ist, funktioniert wie erwartet, 3 Sekunden lang.

### 5. Benachrichtigung
- Benachrichtigung wird mit richtigen Text gesendet.
- Beim clicken der Benachrichtigung, wird die App mit einer neuen page geöffnet, bei der folgender Text angezeigt wird: "You Finished"
- Benachrichtigung wird auch gemacht, wenn die Applikation nicht im Vordergrund läuft.