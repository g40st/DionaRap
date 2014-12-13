import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

import de.fhwgt.dionarap.model.objects.*;
import de.fhwgt.dionarap.controller.DionaRapController;

/**
 * ListenerWaffe
 * Initialiserung des ListenerWaffe der auf Schuss reagiert, implementiert <code>ActionListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class ListenerWaffe implements ActionListener {

    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * Event zu Button 5 zum Schießen der Spielfigur
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

        drcp.shoot();

        hauptfenster.requestFocus();
    }
}
