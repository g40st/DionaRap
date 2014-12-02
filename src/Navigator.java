import javax.swing.JWindow;
import javax.swing.JFrame;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Polygon;

/**
 * Navigator
 * Initialiserung des Kindfensters, abgeleitet von <code>JWindow</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class Navigator extends JWindow {
    // gibt den relativen Abstand zum Hauptfenster an (konstant)
    public static final int nav_pos_const = 30;
    // Polygon für die Form
    Polygon octagon;
    Tastatur tastatur;
    Hauptfenster hauptfenster;

    /**
     * Konstruktor der Steuerflaeche vom Typ <code>JWindow</code>.
     * Erzeugt das Kindfenster, dass relativ zum Hauptfenster plaziert wird.
     * Erzeugen eines roten Rands um das JWindow und hinzufuegen der Tastatur
     *
     * @param parent das Vaterfenster
     */
    Navigator(JFrame parent) {
        super(parent);
        hauptfenster = (Hauptfenster) parent;
        this.setLayout(new BorderLayout());
        addPolygon();
        Rectangle nav_pos = parent.getBounds();
        this.setLocation(((int)nav_pos.getX()) + parent.getWidth() + nav_pos_const, ((int) nav_pos.getY()));
        this.getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        this.setShape(octagon);
        tastatur = new Tastatur();
        this.add(tastatur, BorderLayout.NORTH);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Methode erzeugt das Polygon fuer das regelmaeßige Achteck
     *
     */
    private void addPolygon() {
        octagon = new Polygon();
        octagon.addPoint(50, 0);
        octagon.addPoint(100, 0);
        octagon.addPoint(150, 50);
        octagon.addPoint(150, 100);
        octagon.addPoint(100, 150);
        octagon.addPoint(50, 150);
        octagon.addPoint(0, 100);
        octagon.addPoint(0, 50);
    }

    /**
     * Methode gibt die Tastatur zurueck
     *
     */
    public Tastatur getTastatur() {
        return tastatur;
    }

    /**
     * Methode blendet den Navigator aus
     *
     */
    public void setDisabledNavigator() {
        this.setVisible(false);
    }

    /**
     * Methode blendet den Navigator ein
     *
     */
    public void setEnabledNavigator() {
        this.setVisible(true);
    }
}
