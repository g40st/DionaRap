import de.fhwgt.dionarap.model.listener.DionaRapListener;
import de.fhwgt.dionarap.model.events.DionaRapChangedEvent;
import de.fhwgt.dionarap.model.events.GameStatusEvent;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import java.io.IOException;
import de.fhwgt.dionarap.view.HighScoreFile;

/**
 * ListenerModel
 * Initialisierung des ListenerModel der auf die Events im Spiel reagiert, implementiert <code>DionaRapListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class ListenerModel implements DionaRapListener {
    // Instanz des Hauptfensters
    private Hauptfenster hauptfenster;
    // Instanz des Spielfeldes
    private Spielfeld feld;
    // Spielende verloren
    private boolean gameWon = false;
    // Spielende gewonnen
    private boolean gameRunning = true;

    /**
     * Konstruktor der Klasse ListenerModel
     * Zuordnung des Hauptfensters
     * @param hauptfenster Instanz des <code>Hauptfenster</code>
     */
    ListenerModel(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
    }

    /**
     * Event, dass bei einer Spielaenderung alle Spielfiguren loescht und wieder neu setzt
     * @param e Event wenn sich im Spielablauf etwas aendert
     */
    public void modelChanged(DionaRapChangedEvent e) {
        if(gameRunning == true) {
            hauptfenster.getToolbar().updateToolbar(hauptfenster.getDionaRapModel().getShootAmount(), hauptfenster.getDionaRapModel().getScore(), hauptfenster.getProgress());
            hauptfenster.getSpielfeld().drawNew();
        }
    }

    /**
     * Event, dass bei Spielende aufgerufen wird
     * @param e Event wenn das Spiel gewonnen oder verloren wurde
     */
    public void statusChanged(GameStatusEvent e) {
        gameRunning = false;
        if(e.isGameOver()) {
            gameWon = false;
            hauptfenster.getSounds().playGameOver(); // abspielen der Sounds
        } else if(e.isGameWon()) {
            gameWon = true;
            hauptfenster.getSounds().playGameWon(); // abspielen der Sounds
        }
        hauptfenster.getSpielfeld().drawNew(); // Spielfeld neu zeichnen
        hauptfenster.getToolbar().updateToolbar(hauptfenster.getDionaRapModel().getShootAmount(), hauptfenster.getDionaRapModel().getScore(), hauptfenster.getProgress());
        hauptfenster.getSpielfeld().gameStatusEnd(hauptfenster.getPlayer(), gameWon); // das Icon bei gewonnen oder verloren setzen
        hauptfenster.getToolbar().setEnabled(); // den "Neues Spiel"-Button auf aktiv setzen
        hauptfenster.getMenubar().setGameSettingsEnabled(); // den "Spieleinstellungen"-Button auf aktiv setzen

        int end_position = HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE;
        try{
            end_position = HighScoreFile.getInstance().getScorePosition(hauptfenster.getDionaRapModel().getScore()); // auslesen der Position
        } catch (IOException ex) {
            System.err.println("File kann nicht gelesen werden: " + ex);
        }
        if(end_position != HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE && gameWon) {
            addBestlist(); // Bestenliste Dialog zeichnen
        }
        addDialog(gameWon); // den Dialog zeichnen
    }

    /**
     * Methode, die das JDialogPane zeichnet und nach "Neues Spiel / Abbrechen" fraegt
     * @param gameWon ob das Spiel gewonnen oder verloren
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
        hauptfenster.getSounds().stopPlaying(); // beenden aller aktiven Soundwiedergaben
        if(result == 0) {
            hauptfenster.getSpielfeld().drawNew();
            hauptfenster.newGame();
        }
    }

    /**
     * Methode, die einen JDialog zeichnet und anschließend den Spieler an die richtige Stelle in der Bestenliste eintraegt
     */
    private void addBestlist() {
        String separator = Hauptfenster.getSeparator();
        String img_source = Hauptfenster.getDirectory() + "image" + separator + hauptfenster.getTheme();
        String[] choices = {"Eintragen", "Abbrechen"};
        int position = -1;
        ImageIcon win = new ImageIcon(img_source + separator + "win.gif");

        try{
            position = HighScoreFile.getInstance().getScorePosition(hauptfenster.getDionaRapModel().getScore());
        } catch (IOException ex) {
            System.err.println("File kann nicht gelesen werden: " + ex);
        }
        System.out.println("Position: " + position);

        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage("Sie können sich in die Bestenliste auf Platz " + position + " eintragen. \n\nBitte geben Sie ihren Name ein:");
        optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        optionPane.setOptionType(JOptionPane.YES_NO_OPTION);
        optionPane.setIcon(win);
        optionPane.setWantsInput(true);
        optionPane.setOptions(choices);
        JDialog dialog = optionPane.createDialog(hauptfenster, "Sie haben das Spiel gewonnen!");
        dialog.setVisible(true);

        if(optionPane.getValue().equals("Eintragen")) {
            String playername;
            if(optionPane.getInputValue().toString().length() != 0) {
                playername = optionPane.getInputValue().toString();
            } else {
                playername = "NoName";
            }
            try{
                HighScoreFile.getInstance().writeScoreIntoFile(playername, hauptfenster.getDionaRapModel().getScore());
            } catch (IOException ex) {
                System.err.println("File kann nicht gelesen werden: " + ex);
            }
        }
    }
}
