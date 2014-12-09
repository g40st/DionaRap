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
 * Initialiserung des ListenerMaus der auf Interaktionen mit der Maus reagiert, abgeleitet von <code>MouseAdapter<code>, implementiert <code>MouseListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class ListenerMaus extends MouseAdapter implements ActionListener {
    // Objekt des Hauptfensters
    Hauptfenster hauptfenster;
    JLabel[][] label;
    int x;
    int y;
    DionaRapController drcp;
    JPopupMenu popupMenu;
    JMenuItem dracula;
    JMenuItem spaceWars;
    JMenuItem squareHead;
    String img_source = Hauptfenster.getDirectory();
    String separator = Hauptfenster.getSeparator();

    /**
     * Konstruktor der Klasse ListenerMaus
     *
     * ist fuer das Popup-Menue zustaendig
     * @param das Hauptfenster wird uebergeben
     */
    ListenerMaus(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
        popupMenu = new JPopupMenu("Thema");
        popupMenu.add(dracula = new JMenuItem("Dracula", new ImageIcon(img_source + "image" + separator + "Dracula" + separator + "popup.gif")));
        dracula.setEnabled(false);
        dracula.addActionListener(this);
        popupMenu.add(spaceWars = new JMenuItem("SpaceWars", new ImageIcon(img_source + "image" + separator + "SpaceWars" + separator + "popup.gif")));
        spaceWars.addActionListener(this);
        popupMenu.add(squareHead = new JMenuItem("SquareHead", new ImageIcon(img_source + "image" + separator + "SquareHead" + separator + "popup.gif")));
        squareHead.addActionListener(this);

    }

    /**
     * Eventhandler fuer das Event <code>mouseClicked</code>
     * Event, erzeugt ein Popup-Menue bei Rechtsklick auf die Spielflaeche
     * Event, das bei Linksklick auf ein benachbartes Feld den Spieler bewegt bzw. schießt
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
                            drcp.shoot();
                        } else if(y - i == -1 && x - k == 1) {
                            hauptfenster.getSpielfeld().setLastDirection(1);
                            drcp.movePlayer(1);
                        } else if(y - i == -1 && x - k == 0) {
                            hauptfenster.getSpielfeld().setLastDirection(2);
                            drcp.movePlayer(2);
                        } else if(y - i == -1 && x - k == -1) {
                            hauptfenster.getSpielfeld().setLastDirection(3);
                            drcp.movePlayer(3);
                        } else if(y - i == 0 && x - k == -1) {
                            hauptfenster.getSpielfeld().setLastDirection(6);
                            drcp.movePlayer(6);
                        } else if(y - i == 1 && x - k == -1) {
                            hauptfenster.getSpielfeld().setLastDirection(9);
                            drcp.movePlayer(9);
                        } else if(y - i == 1 && x - k == 0) {
                            hauptfenster.getSpielfeld().setLastDirection(8);
                            drcp.movePlayer(8);
                        } else if(y - i == 1 && x - k == 1) {
                            hauptfenster.getSpielfeld().setLastDirection(7);
                            drcp.movePlayer(7);
                        } else if(y - i == 0 && x - k == 1) {
                            hauptfenster.getSpielfeld().setLastDirection(4);
                            drcp.movePlayer(4);
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