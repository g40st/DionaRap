import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.fhwgt.dionarap.controller.DionaRapController;


/**
 * ListenerBewegung
 * Initialiserung des ListenerBewegung, implementiert <code>ActionListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class ListenerBewegung implements ActionListener {

    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * Event zu den Buttons 1-4 / 6-9 zum Bewegen der Spielfigur
     * @param ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if(Integer.parseInt(e.getActionCommand()) > 0 && Integer.parseInt(e.getActionCommand()) < 10) {
            JButton btn = (JButton) e.getSource();
            Hauptfenster hauptfenster = (Hauptfenster) btn.getTopLevelAncestor().getParent();
            DionaRapController drcp = (DionaRapController) hauptfenster.getDionaRapController();
            // fuer die aktuelle Spielerposition
            int x = hauptfenster.getPlayer().getX();
            int y = hauptfenster.getPlayer().getY();

            hauptfenster.getSpielfeld().setLastDirection(Integer.parseInt(e.getActionCommand()));
            drcp.movePlayer(Integer.parseInt(e.getActionCommand()));
            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) { // Sobald eine gueltige Bewegung vollzogen wird, wird der Thread 2 beendet
                hauptfenster.stopThread2Run();
            } else { // wenn die Position geaendert wurde -> Sound
                hauptfenster.getSounds().playMove();
            }

            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) { // die Spielerposition hat sich zur vorherigen Position nicht geaendert
                hauptfenster.createNewThread2();
            }

            hauptfenster.requestFocus();
        }
    }
}
