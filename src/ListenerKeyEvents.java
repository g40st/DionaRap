import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

import de.fhwgt.dionarap.controller.DionaRapController;

/**
 * ListenerKeyEvents
 * Klasse realisiert den Listener fuer Tastatur-Ergeignisse und implementiert
 * das Interface <code>KeyListener</code>. Events, die die Ziffern 1-9 betreffen werden behandelt.
 * Copyright (c) 2014
 * @author Christian Hoegerle und Thomas Buck
 * @version 1.0
 */
public class ListenerKeyEvents implements KeyListener {
    private int x;
    private int y;

    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    /**
     * Eventhandler fuer das Event <code>keyTyped</code>,
     * Events zu den Zifferntasten 1-4 und 6-9 fuer die Bewegung,
     * Event zu Zifferntaste 5 zum Schießen der Spielfigur
     * @param e Event
     */
    public void keyTyped(KeyEvent e) {
        Hauptfenster hauptfenster = (Hauptfenster) e.getSource();
        DionaRapController drcp = hauptfenster.getDionaRapController();

        if(e.getKeyChar() != '5' &&  ('1' <= e.getKeyChar() && e.getKeyChar() <= '9')){
            x = hauptfenster.getPlayer().getX();
            y = hauptfenster.getPlayer().getY();
            hauptfenster.getSpielfeld().setLastDirection(Character.getNumericValue(e.getKeyChar()));
            drcp.movePlayer(Character.getNumericValue(e.getKeyChar()));
            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) { // Sobald eine gueltige Bewegung vollzogen wird, wird der Thread 2 beendet
                hauptfenster.stopThread2Run();
            }
            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) { // die Spielerposition hat sich zur vorherigen Position nicht geaendert
                hauptfenster.createNewThread2();
            } else { // wenn die Position geaendert wurde -> Sound
                hauptfenster.getSounds().playMove();
            }
        }
        else if((e.getKeyChar() == '5')) {
            if (hauptfenster.getDionaRapModel().getShootAmount() == 0) { // Erzuegen des "Blink"-Threads, falls Munition = 0
                if(hauptfenster.getThread1() == null) {
                    hauptfenster.createNewThread1();
                } else if(!(hauptfenster.getThread1().isAlive())) {
                    hauptfenster.createNewThread1();
                }
            }
            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) { // Sobald eine gueltige Bewegung vollzogen wird, wird der Thread 2 beendet
                hauptfenster.stopThread2Run();
            }
            if (hauptfenster.getDionaRapModel().getShootAmount() != 0) { // Sobald die Muntion leer ist wird keine Sound mehr abgespielt
                hauptfenster.getSounds().playShoot();
            }
            drcp.shoot();
        }
        else {
            System.out.println(e.getKeyChar());
        }
    }
}
