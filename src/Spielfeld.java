import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;

import de.fhwgt.dionarap.model.objects.*;

/**
 * Spielfeld
 * Initialiserung des Spielfeldes (10x10), abgeleitet von <code>JPanel</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class Spielfeld extends JPanel {
    int max_x = Hauptfenster.getMax_x();
    int max_y = Hauptfenster.getMax_y();
    // Array fuer das Spielfeld
    JLabel[][] label = new JLabel [max_y][max_x];
    // home-directory
    String img_source = Hauptfenster.getDirectory();
    // Separator
    String separator = Hauptfenster.getSeparator();
    // Stringarray für die verschiedenen Blickrichtungen
    String [] viewing_direction = {"player.gif", "player1.gif", "player2.gif", "player3.gif", "player4.gif", null, "player6.gif", "player7.gif", "player8.gif", "player9.gif", "loss.gif", "win.gif"};
    // Integer für die letzte Blickrichtung
    int viewing_direction_Player = 0;

    /**
     * Konstruktor des Spielfeldes vom Typ <code>JPanel</code>.
     * legt den Layout-Manager fest und ruft die Methode addJLabels() auf
     */
    Spielfeld() {
        super();
        this.setLayout(new GridLayout(max_y, max_x));
        // Theme "Dracula" setzen
        img_source = img_source + "image" + separator + "Dracula";
        addJLabels();
    }

    /**
     * Methode erzeugt das Spielfeld entsprechend der Vorgabe mit abwechselnder JLabel-Hintergrundfarbe
     */
    private void addJLabels() {
        for (int i = 0; i < max_y; i++) {
            for (int k = 0; k < max_x; k++) {
                label[i][k] = new JLabel();
                if((i+k) % 2 == 0) {    // Modulo-Operation fuer abwechselnde Felder
                    label[i][k].setBackground(Color.BLACK);
                }
                else {
                    label[i][k].setBackground(Color.WHITE);
                }
                label[i][k].setPreferredSize(new Dimension(50, 50));
                label[i][k].setOpaque(true);
                this.add(label[i][k]);
            }
        }
    }

    /**
     * Methode, um die letzte Bewegungsrichtung zu ermitteln
     *
     *@param erhaelt die letzte Richtungseingabe vom Event
     */
    public void setLastDirection(int lastDirection) {
        viewing_direction_Player = lastDirection;
    }

    /**
     * Methode, die alle Spielfiguren zeichnet
     *
     * @param Array vom Typ AbstractPawn, enthaelt alle Spielfiguren
     */
    public void drawAllPawns(AbstractPawn[] spielfiguren) {
        for(int i = 0; i < (int) spielfiguren.length; i++) {
            if ((spielfiguren[i].getX() < 0) || (spielfiguren[i].getY() < 0)) {

            } else if (spielfiguren[i] instanceof Player) {
                label[spielfiguren[i].getY()][spielfiguren[i].getX()].setIcon(new ImageIcon(img_source + separator + viewing_direction[viewing_direction_Player]));
            } else if(spielfiguren[i] instanceof Opponent) {
                label[spielfiguren[i].getY()][spielfiguren[i].getX()].setIcon(new ImageIcon(img_source + separator + "opponent.gif"));
            } else if(spielfiguren[i] instanceof Vortex) {
                label[spielfiguren[i].getY()][spielfiguren[i].getX()].setIcon(new ImageIcon(img_source + separator + "vortex.gif"));
            } else if(spielfiguren[i] instanceof Obstacle) {
                label[spielfiguren[i].getY()][spielfiguren[i].getX()].setIcon(new ImageIcon(img_source + separator + "obstacle.gif"));
            } else if(spielfiguren[i] instanceof Destruction) {
                label[spielfiguren[i].getY()][spielfiguren[i].getX()].setIcon(new ImageIcon(img_source + separator + "destruction.gif"));
            } else if(spielfiguren[i] instanceof Ammo) {
                label[spielfiguren[i].getY()][spielfiguren[i].getX()].setIcon(new ImageIcon(img_source + separator + "ammo.gif"));
            }
        }
    }

    /**
     * Methode, die den Player bei Ende des Spiels zeichnet (gewonnen oder verloren)
     *
     */
    public void gameStatusEnd(Player player, boolean gameWon) {
        if(!gameWon) {
            label[player.getY()][player.getX()].setIcon(null);
            label[player.getY()][player.getX()].setIcon(new ImageIcon(img_source + separator + viewing_direction[10]));
        } else if (gameWon) {
            label[player.getY()][player.getX()].setIcon(null);
            label[player.getY()][player.getX()].setIcon(new ImageIcon(img_source + separator + viewing_direction[11]));
        }
    }

    /**
     * Methode, die alle Spielfiguren auf dem Spielfeld loescht
     *
     */
    public void delAllPawns() {
        for(int i = 0; i < max_y; i++) {
            for(int k = 0; k < max_x; k++) {
                label[i][k].setIcon(null);
            }
        }
    }
}
