import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

import de.fhwgt.dionarap.model.objects.*;
import de.fhwgt.dionarap.controller.DionaRapController;

/**
 * ListenerWaffe
 * Initialisierung des ListenerWaffe der auf Schuss reagiert, implementiert <code>ActionListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class ListenerWaffe implements ActionListener {

    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * Event zu Button 5 zum Schießen der Spielfigur
     * @param e reagieren auf den Button 5 aus der <code>Tastatur</code>
     */
    public void actionPerformed(ActionEvent e) {
        System.out.print(e.getActionCommand());
        JButton btn = (JButton) e.getSource();
        Hauptfenster hauptfenster = (Hauptfenster) btn.getTopLevelAncestor().getParent();
        DionaRapController drcp = (DionaRapController) hauptfenster.getDionaRapController();

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

        hauptfenster.requestFocus();
    }
}
