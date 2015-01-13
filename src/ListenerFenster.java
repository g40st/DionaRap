import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

/**
 * ListenerFenster
 * Initialisierung des FensterListeners, implementiert <code>ComponentListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class ListenerFenster implements ComponentListener {
    private Hauptfenster hauptfenster;
    private Navigator navigator;
    private int nav_pos_const;

    /**
     * Konstruktor der Klasse ListenerFenster
     * den uebergebenen Parametern werden Hilfsvariablen zugeordnet
     * @param hauptfenster Instanz des <code>Hauptfenster</code>
     * @param navigator Instanz des <code>Navigator</code>
     * @param nav_pos_const Abstand zwischen <code>Hauptfenster</code> und <code>Navigator</code>
     */
    ListenerFenster(Hauptfenster hauptfenster, Navigator navigator, int nav_pos_const) {
        this.hauptfenster = hauptfenster;
        this.navigator = navigator;
        this.nav_pos_const = nav_pos_const;
    }
    public void componentHidden(ComponentEvent e){}
    public void componentResized(ComponentEvent e){}
    public void componentShown(ComponentEvent e) {}

    /**
     * Eventhandler fuer das Event <code>componentMoved</code>
     * Event, dass das Navigatorfenster relativ zum Hauptfenster platziert
     * @param e Event wenn das <code>Hauptfenster</code> verschoben wird
     */
    public void componentMoved(ComponentEvent e){
        navigator.setLocation(((int)hauptfenster.getBounds().getX()) + hauptfenster.getWidth() + nav_pos_const, ((int) hauptfenster.getBounds().getY()));
    }
}
