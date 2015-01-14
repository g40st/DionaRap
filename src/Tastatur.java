import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.Insets;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.ComponentOrientation;

/**
 * Tastatur
 * Initialisierung der Tastatur, abgeleitet von <code>JPanel</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class Tastatur extends JPanel {
    // gibt die maximale Dimension der Tastaur an
    static final int key_static = 3;
    // gibt die maximale Anzahl der Tasten an
    static final int max_buttons = 9;
    // eine Instanz des ListenerBewegung
    private ListenerBewegung lis_move = new ListenerBewegung();
    // Stringarray fuer die verschiedenen Pfeilrichtungen
    private String [] button_direction = {"taste1.gif", "taste2.gif", "taste3.gif", "taste4.gif", "taste5.gif", "taste6.gif", "taste7.gif", "taste8.gif", "taste9.gif"};
    // String-Variable fuer den Navigator-Pfad
    private String nav_source = Hauptfenster.getDirectory();
    private String separator = Hauptfenster.getSeparator();

    /**
     * Konstruktor der Steuerflaeche vom Typ <code>JPanel</code>.
     * Legt den Layout-Manager fest und ruft die Methode addJButtons() auf
     */
    public Tastatur() {
        // definieren des Layout-Mangers
        this.setLayout(new GridLayout(key_static, key_static));
        this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        nav_source = nav_source + separator + "image" + separator + "navigator";
        addJButtons();
        this.setVisible(true);
    }

    /**
     * Methode ordnet die Steuertasten entprechend dem Num-Pad der Tastatur an.
     * Die Buttons 1-4 / 6-9 werden dem ListenerBewegung zugeordnet
     * Der Button 5 wird dem ListenerWaffe zugeordnet
     */
    private void addJButtons() {
        JButton [] buttons = new JButton [max_buttons];
        for (int i = 9; i > 0; i--) {
            buttons[i - 1] = new JButton();
            buttons[i - 1].setIcon(new ImageIcon(nav_source + separator + button_direction[i - 1]));
            buttons[i - 1].setActionCommand(String.valueOf(i));
            buttons[i - 1].setPreferredSize(new Dimension(50, 50));
            buttons[i - 1].setMargin(new Insets(0, 0, 0, 0));
            this.add(buttons[i - 1]);

            if(i == 5) {
                buttons[i-1].addActionListener(new ListenerWaffe());
            }
            else {
                buttons[i-1].addActionListener(lis_move);
            }
        }
    }

    /**
     * Methode gibt das Objekt des ListenerBewegung zurueck
     * @return ListenerBewegung
     */
    public ListenerBewegung getListenerBewegung() {
        return lis_move;
    }

}
