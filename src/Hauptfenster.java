import javax.swing.JFrame;

import java.awt.Rectangle;
import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import de.fhwgt.dionarap.model.data.DionaRapModel;
import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Player;
import de.fhwgt.dionarap.model.objects.Opponent;
import de.fhwgt.dionarap.controller.DionaRapController;
import de.fhwgt.dionarap.model.data.MTConfiguration;

/**
 * Hauptfenster
 * Initialiserung des Hauptfensters
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class Hauptfenster extends JFrame {
    // gibt die Anzahl der Felder in x-Richtung an
    static final int max_x = 16;
    // gibt die Anzahl der Felder in y-Richtung an
    static final int max_y = 10;
    // ausgewaehltes Theme zu beginn
    String theme = "Dracula";
    DionaRapModel dionaRapModel;
    DionaRapController dionaRapController;
    Navigator navigator;
    Spielfeld spielfeld;
    Toolbar toolbar;
    int opponents = 3;
    boolean displayFlag = false;
    boolean runningFlag = true;
    boolean wonFlag = false;

    /**
     * Standard Konstruktor der Klasse Hauptfenster
     *
     * Setzt die Fenstereingenschaften: Titel, Groesse, Sichtbarkeit, Verhalten des Exit-Knopfes, erzeugen des Spielfeldes(JPanel), erzuegen des Navigators (JWindow)
     * @param String aus der public Methode wird uebergeben
     */
    Hauptfenster(String text) {
        super(text);

        // Frame-Definition
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false); // verhindert Groeßenaenderung des Fensters
        this.setLayout(new BorderLayout());

        // Spielfeld hinzufuegen
        spielfeld = new Spielfeld(this);
        this.add(spielfeld, BorderLayout.SOUTH);

        // Initialisierung Controller und Model
        newDionaRap();

        // Navigator hinzufuegen
        navigator = new Navigator(this);

        // Toolbar hinzufuegen
        toolbar = new Toolbar(this);
        this.add(toolbar, BorderLayout.CENTER);

        // Menueleiste hinzufuegen
        this.add(new MenueLeiste(this), BorderLayout.NORTH);

        // Listener hinzufuegen
        this.addComponentListener(new ListenerFenster(this, navigator, navigator.nav_pos_const));
        this.addKeyListener(new ListenerKeyEvents());
        this.addMouseListener(new ListenerMaus(this));
        this.setFocusable(true);

        this.pack();
        // Hauptfenster in der Mitte platzieren
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocusInWindow();
    }

    /**
     * Methode, die die Spielelogik initialisiert (DionaRapModel und DionaRapController)
     *
     */
    public void newDionaRap() {
        dionaRapModel = new DionaRapModel(max_y, max_x, opponents, 4);
        spielfeld.setLastDirection(0);
        dionaRapModel.addModelChangedEventListener(new ListenerModel(this, spielfeld));
        spielfeld.drawAllPawns(getPawns());
        dionaRapController = new DionaRapController(dionaRapModel);
        //MTConfiguration c = new MTConfiguration();
        //c.setShotGetsOwnThread(true);
        //c.setShotWaitTime(200);
        //c.setOpponentStartWaitTime(10000000);

        dionaRapController.deactivateMultiThreading();
    }

    /**
     * get-Methode, gibt das Array mit den Spielfiguren zurueck
     *
     */
    public AbstractPawn[] getPawns() {
        return dionaRapModel.getAllPawns();
    }

    /**
     * get-Methode, gibt die Instanz von DionaRapController zurueck
     *
     */
    public DionaRapController getDionaRapController() {
        return dionaRapController;
    }

    /**
     * get-Methode, gibt die Instanz von DionaRapController zurueck
     *
     */
    public DionaRapModel getDionaRapModel() {
        return dionaRapModel;
    }

    /**
     * get-Methode, gibt die Instanz vom Spielfeld zurueck
     *
     */
    public Spielfeld getSpielfeld() {
        return spielfeld;
    }

    /**
     * get-Methode, gibt den Player zurueck
     *
     */
    public Player getPlayer() {
        return dionaRapModel.getPlayer();
    }

    /**
     * statische get-Methode, gibt den System-Separator zurueck
     *
     */
    public static String getSeparator() {
        String separator = System.getProperty("file.separator");
        return separator;
    }

    /**
     * statische get-Methode, gibt das Verzeichnis indem das Spiel gestartet wurde zurueck
     *
     */
    public static String getDirectory() {
        String home = System.getProperty("user.dir");
        return (home + getSeparator());
    }

    /**
     * get-Methode, gibt den Navigator zurueck
     *
     */
    public Navigator getNavigator() {
        return navigator;
    }

    /**
     * get-Methode, gibt die Toolbar zurueck
     *
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * get-Methode, gibt den Spielfortschritt als Ganzzahl zurueck
     *
     */
    public int getProgress() {
        float d = ((opponents - (float) dionaRapModel.getOpponentCount()) / opponents) * 100;
        return (int) d;
    }

    /**
     * get-Methode, gibt das aktuelle Theme als String zurueck
     *
     */
    public String getTheme() {
        return theme;
    }

    /**
     * set-Methode, das aktuelle Theme setzen und das Spielfeld neu zeichnen
     *
     */
    public void setTheme(String theme) {
        this.theme = theme;
        spielfeld.setImgSrc();
        spielfeld.drawAllPawns(getPawns());
    }


    public void setRender(boolean f, boolean running, boolean won) {
        displayFlag = f;
        runningFlag = running;
        wonFlag = won;
    }

    /**
     * set-Methode, vertauscht das Spielfeld mit der Toolbar
     *
     * @param falls true wird die Toolbar im CENTER-Bereich positioniert / false dann im SOUTH-Bereich
     */
    public void setToolbarPosition(boolean center) {
        if(center) {
            this.remove(spielfeld);
            this.remove(toolbar);
            this.add(spielfeld, BorderLayout.SOUTH);
            this.add(toolbar, BorderLayout.CENTER);
            this.pack();

        }
        else {
            this.remove(spielfeld);
            this.remove(toolbar);
            this.add(spielfeld, BorderLayout.CENTER);
            this.add(toolbar, BorderLayout.SOUTH);
            this.pack();
        }
    }

    /**
     * Main-Methode erzeugt das Hauptfenster
     *
     * @param args Kommandozeilenparameter (nicht verwendet)
     */
    public static void main(String[] args) {
        Hauptfenster h = new Hauptfenster("DionaRap");

        // "Render loop" Proof-of-Concept
        while (true) {
            if (h.displayFlag) {
                h.spielfeld.delAllPawns();
                h.spielfeld.drawAllPawns(h.getPawns());
                h.displayFlag = false;
            }

            if (!h.runningFlag) {
                h.spielfeld.gameStatusEnd(h.getPlayer(), h.wonFlag);
                h.getToolbar().setEnabled();
                h.addDialog(h.wonFlag);
                h.runningFlag = true;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Methode, die das JDialogPane zeichnet und nach "Neues Spiel / Abbrechen" fraegt
     *
     *@param boolean ob das Spiel gewonnen oder verloren, Reaktion auf den Button "Neues Spiel"
     */
    private void addDialog(boolean gameWon) {
        String separator = getSeparator();
        String img_source = getDirectory() + "image" + separator + getTheme();
        String[] choices = {"Neues Spiel", "Abbrechen"};
        ImageIcon lose = new ImageIcon(img_source + separator + "gameover.gif");
        ImageIcon win = new ImageIcon(img_source + separator + "win.gif");
        int result;
        if(gameWon) {
            result = JOptionPane.showOptionDialog(this, "Sie haben das Spiel gewonnen!", "Win", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, win, choices, "Neues Spiel");
        }
        else {
            result = JOptionPane.showOptionDialog(this, "Sie haben das Spiel verloren!", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, lose, choices, "Neues Spiel");
        }
        if(result == 0) {
            spielfeld.delAllPawns();
            newDionaRap();
            getToolbar().setDefault();
        }
    }
}
