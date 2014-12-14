import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

/**
 * Fensterlistener
 * Initialiserung des FensterListeners, implementiert <code>ComponentListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class ListenerFenster implements ComponentListener {
    private Hauptfenster hauptfenster;
    private Navigator navigator;
    private int nav_pos_const;

    /**
     * Konstruktor der Klasse ListenerFenster
     *
     * den uebergebenen Parametern werden Hilfsvariablen zugeordnet
     * @param das Spieldfeld, der Navigator und die Abstandskonstante wird uebergeben
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
     */
    public void componentMoved(ComponentEvent e){
        navigator.setLocation(((int)hauptfenster.getBounds().getX()) + hauptfenster.getWidth() + nav_pos_const, ((int) hauptfenster.getBounds().getY()));
    }
}
