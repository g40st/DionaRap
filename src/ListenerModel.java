import de.fhwgt.dionarap.model.listener.DionaRapListener;
import de.fhwgt.dionarap.model.events.DionaRapChangedEvent;
import de.fhwgt.dionarap.model.events.GameStatusEvent;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

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
    ListenerModel(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
    }

    /**
     * Event, dass bei einer Spielaenderung alle Spielfiguren loescht und wieder neu setzt
     */
    public void modelChanged(DionaRapChangedEvent e) {
        if(gameRunning == true) {
            hauptfenster.getToolbar().updateToolbar(hauptfenster.getDionaRapModel().getShootAmount(), hauptfenster.getDionaRapModel().getScore(), hauptfenster.getProgress());
            hauptfenster.getSpielfeld().drawNew();
        }
    }

    /**
     * Event, dass bei Spielende aufgerufen wird
     */
    public void statusChanged(GameStatusEvent e) {
        gameRunning = false;
        if(e.isGameOver()) {
            gameWon = false;
        } else if(e.isGameWon()) {
            gameWon = true;
        }
        hauptfenster.getSpielfeld().drawNew(); // Spielfeld neu zeichnen
        hauptfenster.getToolbar().updateToolbar(hauptfenster.getDionaRapModel().getShootAmount(), hauptfenster.getDionaRapModel().getScore(), hauptfenster.getProgress());
        hauptfenster.getSpielfeld().gameStatusEnd(hauptfenster.getPlayer(), gameWon); // das Icon bei gewonnen oder verloren setzen
        hauptfenster.getToolbar().setEnabled(); // den "Neues Spiel"-Button auf aktiv setzen
        addDialog(gameWon); // den Dialog zeichnen
    }

    /**
     * Methode, die das JDialogPane zeichnet und nach "Neues Spiel / Abbrechen" fraegt
     *
     *@param boolean ob das Spiel gewonnen oder verloren, Reaktion auf den Button "Neues Spiel"
     */
    private void addDialog(boolean gameWon) {
        String separator = Hauptfenster.getSeparator();
        String img_source = Hauptfenster.getDirectory() + "image" + separator + hauptfenster.getTheme();
        String[] choices = {"Neues Spiel", "Abbrechen"};
        ImageIcon lose = new ImageIcon(img_source + separator + "gameover.gif");
        ImageIcon win = new ImageIcon(img_source + separator + "win.gif");
        int result;
        if(gameWon) {
            result = JOptionPane.showOptionDialog(hauptfenster, "Sie haben das Spiel gewonnen!", "Win", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, win, choices, "Neues Spiel");
        }
        else {
            result = JOptionPane.showOptionDialog(hauptfenster, "Sie haben das Spiel verloren!", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, lose, choices, "Neues Spiel");
        }
        if(result == 0) {
            hauptfenster.getSpielfeld().drawNew();
            hauptfenster.newGame();
        }
    }
}
