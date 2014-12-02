import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;

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
     *
     */
    public void mouseClicked(MouseEvent e) { // Anzeigen des Popup-Menues bei Rechtsklick
        if(e.getButton() == 3) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
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