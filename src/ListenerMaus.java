import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import de.fhwgt.dionarap.controller.DionaRapController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * ListenerMaus
 * Initialisierung des ListenerMaus der auf Interaktionen mit der Maus reagiert, abgeleitet von <code>MouseAdapter<code>, implementiert <code>ActionListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class ListenerMaus extends MouseAdapter implements ActionListener {
    // Objekt des Hauptfensters
    private Hauptfenster hauptfenster;
    private JLabel[][] label;
    private int x;
    private int y;
    private DionaRapController drcp;
    private JPopupMenu popupMenu;
    private JMenuItem dracula;
    private JMenuItem spaceWars;
    private JMenuItem squareHead;
    private String img_source = Hauptfenster.getDirectory();
    private String separator = Hauptfenster.getSeparator();

    /**
     * Konstruktor der Klasse ListenerMaus
     * setzt die Default-Werter des Popup-Menues
     * @param hauptfenster Instanz des <code>Hauptfenster</code>
     */
    ListenerMaus(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
        popupMenu = new JPopupMenu("Thema");
        popupMenu.add(dracula = new JMenuItem("Dracula", new ImageIcon(img_source + "image" + separator + "Dracula" + separator + "popup.gif")));
        dracula.addActionListener(this);
        popupMenu.add(spaceWars = new JMenuItem("SpaceWars", new ImageIcon(img_source + "image" + separator + "SpaceWars" + separator + "popup.gif")));
        spaceWars.addActionListener(this);
        popupMenu.add(squareHead = new JMenuItem("SquareHead", new ImageIcon(img_source + "image" + separator + "SquareHead" + separator + "popup.gif")));
        squareHead.addActionListener(this);
        if(hauptfenster.getTheme().equals("Dracula")) { // das aktuelle Theme deaktivieren
            dracula.setEnabled(false);
        } else if (hauptfenster.getTheme().equals("SpaceWars")) {
            spaceWars.setEnabled(false);
        } else if (hauptfenster.getTheme().equals("SquareHead")) {
            squareHead.setEnabled(false);
        }
    }

    /**
     * Eventhandler fuer das Event <code>mouseClicked</code>
     * Event, erzeugt ein Popup-Menue bei Rechtsklick auf die Spielflaeche
     * Event, das bei Linksklick auf ein benachbartes Feld den Spieler bewegt bzw. schießt
     * @param MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 3) { // Anzeigen des Popup-Menues bei Rechtsklick
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        } else if (e.getButton() == 1) { // bewegt den Spieler bei Linksklick auf ein benachbartes Feld
            label = new JLabel [hauptfenster.getDionaRapModel().getGrid().getGridSizeY()][hauptfenster.getDionaRapModel().getGrid().getGridSizeX()];
            label = hauptfenster.getSpielfeld().getLabel();
            y = hauptfenster.getPlayer().getY();
            x = hauptfenster.getPlayer().getX();
            drcp = (DionaRapController) hauptfenster.getDionaRapController();
            for (int i = 0; i < hauptfenster.getDionaRapModel().getGrid().getGridSizeY(); i++) {
                for (int k = 0; k < hauptfenster.getDionaRapModel().getGrid().getGridSizeX(); k++) {
                    if(e.getSource().equals(label[i][k])) {
                        if(i == y && k == x) {
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
                            break;
                        } else if(y - i == -1 && x - k == 1) {
                            hauptfenster.getSpielfeld().setLastDirection(1);
                            drcp.movePlayer(1);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run(); // Blinken beenden falls es aktiv ist, gueltige Bewegung ausgefuehrt
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2(); // Blinken starten, da keine Positionsaenderung
                            else hauptfenster.getSounds().playMove(); // wenn die Position geaendert wurde -> Sound
                        } else if(y - i == -1 && x - k == 0) {
                            hauptfenster.getSpielfeld().setLastDirection(2);
                            drcp.movePlayer(2);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run();
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2();
                            else hauptfenster.getSounds().playMove();
                        } else if(y - i == -1 && x - k == -1) {
                            hauptfenster.getSpielfeld().setLastDirection(3);
                            drcp.movePlayer(3);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run();
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2();
                            else hauptfenster.getSounds().playMove();
                        } else if(y - i == 0 && x - k == -1) {
                            hauptfenster.getSpielfeld().setLastDirection(6);
                            drcp.movePlayer(6);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run();
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2();
                            else hauptfenster.getSounds().playMove();
                        } else if(y - i == 1 && x - k == -1) {
                            hauptfenster.getSpielfeld().setLastDirection(9);
                            drcp.movePlayer(9);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run();
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2();
                            else hauptfenster.getSounds().playMove();
                        } else if(y - i == 1 && x - k == 0) {
                            hauptfenster.getSpielfeld().setLastDirection(8);
                            drcp.movePlayer(8);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run();
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2();
                            else hauptfenster.getSounds().playMove();
                        } else if(y - i == 1 && x - k == 1) {
                            hauptfenster.getSpielfeld().setLastDirection(7);
                            drcp.movePlayer(7);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run();
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2();
                            else hauptfenster.getSounds().playMove();
                        } else if(y - i == 0 && x - k == 1) {
                            hauptfenster.getSpielfeld().setLastDirection(4);
                            drcp.movePlayer(4);
                            if((hauptfenster.getThread2() != null) && (hauptfenster.getThread2().isAlive())) hauptfenster.stopThread2Run();
                            if((x == hauptfenster.getPlayer().getX()) && y == (hauptfenster.getPlayer().getY())) hauptfenster.createNewThread2();
                            else hauptfenster.getSounds().playMove();
                        } else {
                            if(hauptfenster.getThread2() == null) { // Uberpruefen, ob ein Objekte von Thread2 existiert
                                hauptfenster.createNewThread2();
                            } else if(!(hauptfenster.getThread2().isAlive())) { // falls es ein ein Objekt gibt, dieses aber nicht mehr Aktiv ist
                                    hauptfenster.createNewThread2();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * Fuer jedes Element im Popup-Menue
     * Events: setze Theme: Dracula, Spacewars und Sqarehead
     * @param ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == dracula) { // setzen des Themes "Dracula"
            hauptfenster.setTheme("Dracula");
            dracula.setEnabled(false);
            spaceWars.setEnabled(true);
            squareHead.setEnabled(true);
        }

        if(e.getSource() == spaceWars) { // setzen des Themes "Spacewars"
            hauptfenster.setTheme("SpaceWars");
            spaceWars.setEnabled(false);
            dracula.setEnabled(true);
            squareHead.setEnabled(true);
        }

        if(e.getSource() == squareHead) { // setzen des Themes "Squarehead"
            hauptfenster.setTheme("SquareHead");
            squareHead.setEnabled(false);
            dracula.setEnabled(true);
            spaceWars.setEnabled(true);
        }
    }
}