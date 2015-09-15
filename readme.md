Its Something.
==============
## MGD Project
### Christoph Kramer, Florian Porada
### Hochschule der Medien


Important Notes
---------------
- optimized for 1920 x 1080
- tested on LG G2 (d802) SDK 5.0.1

# How to Play
- touch on the bottom right or bottom left to move your thing
- touch the thing to shoot
- wait until the missile thing hit an object oder flies out of the screen
- shoot again
- don't fly into other things
- collect misteryboxes for power-ups! (yeah)
- profit

More Important Notes
--------------------
- After pressing Start Game wait a little bit. It takes time to render all the objects. 
- Problems with Emulator. Better test it on an actual device!


Some Infos about the code
-------------------------
- Die Boxarrays werden zufallsgeneriert und wenn BoxarrayA am unteren Rand ankommt wird Boxarray ausserhalb des Bildschirms generiert und nach unten geschickt. (vice versa)
- Die Gameobjects halten alle benötigten Daten für Matrix, Mesh, Hitboxen usw. 
- Durch einen Gametimecounter wird die Boxgeschwindigkeit immer weiter hochgesetzt um die Schwierigkeit zu erhöhen. 
- Es gibt drei verschiedene Power-ups (Schnellerer Jet, Schnellerer Schuss, Langsamere Gegner). Alle werden über das Powerupobject definiert.
- Der Mediaplayer bugt ein bisschen aber lässt das Spiel nicht abstürzen.
- Manchmal wird der MediaPlayer nicht richtig geladen, ein Neustart des Spiels hilft meistens.
- Der Highscore State gibt zurzeit leider nur die Daten der letzten Session aus. (sollte aber für den Punkt persistente Daten reichen. Proof of Concept)
- Control CheatSheet unter Docs/controls.jpg

####Update
- Highscore gibt es jetzt doch.
