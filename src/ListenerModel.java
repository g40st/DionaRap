import de.fhwgt.dionarap.model.listener.DionaRapListener;
import de.fhwgt.dionarap.model.events.DionaRapChangedEvent;
import de.fhwgt.dionarap.model.events.GameStatusEvent;

/**
 * ListenerModel
 * Initialiserung des ListenerModel der auf die Events im Spiel reagiert, implementiert <code>DionaRapListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class ListenerModel implements DionaRapListener {
    // Instanz des Hauptfensters
    Hauptfenster hauptfenster;
    // Instanz des Spielfeldes
    Spielfeld feld;
    // Spielende verloren
    boolean gameWon = false;
    // Spielende gewonnen
    boolean gameRunning = true;

    /**
     * Konstruktor der Klasse ListenerFenster
     *
     * den uebergebenen Parametern werden Hilfsvariablen zugeordnet
     * @param das Spieldfeld und das Spielfeld wird uebergeben
     */
    ListenerModel(Hauptfenster hauptfenster, Spielfeld feld) {
        this.hauptfenster = hauptfenster;
        this.feld = feld;
    }

    /**
     * Event, dass bei einer Spielaenderung alle Spielfiguren loescht und wieder neu setzt
     */
    public void modelChanged(DionaRapChangedEvent e) {
        hauptfenster.getToolbar().setScore(hauptfenster.getDionaRapModel().getScore());
        hauptfenster.getToolbar().setProgress(hauptfenster.getProgress());
        hauptfenster.setRender(true, gameRunning, gameWon);
    }

    /**
     * Event, dass bei Spielende aufgerufen wird
     */
    public void statusChanged(GameStatusEvent e) {
        hauptfenster.getToolbar().setScore(hauptfenster.getDionaRapModel().getScore());
        gameRunning = false;
        if(e.isGameOver()) {
            gameWon = false;
        } else if(e.isGameWon()) {
            gameWon = true;
        }
        hauptfenster.setRender(true, gameRunning, gameWon);
    }
}
