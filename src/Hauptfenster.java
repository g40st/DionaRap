import java.awt.Rectangle;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;

import de.fhwgt.dionarap.levelreader.LevelReader;
import de.fhwgt.dionarap.model.data.DionaRapModel;
import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Player;
import de.fhwgt.dionarap.model.objects.Opponent;
import de.fhwgt.dionarap.controller.DionaRapController;
import de.fhwgt.dionarap.model.data.MTConfiguration;
import de.fhwgt.dionarap.model.data.Grid;

/**
 * Hauptfenster
 * Initialiserung des Hauptfensters
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class Hauptfenster extends JFrame {
    // ein Spielfeld anlegen
    Grid grid = new Grid(10, 16);
    // ausgewaehltes Theme zu beginn
    String theme = "Dracula";
    DionaRapModel dionaRapModel;
    DionaRapController dionaRapController;
    Navigator navigator;
    Spielfeld spielfeld;
    Toolbar toolbar;
    // Anzahl der Gegner
    int opponents = 0;
    // Anzahl der Hindernisse
    int obstacles = 4;
    // Multithreading-Configuration
    MTConfiguration conf;
    // fuer die Position des Fensters
    static int pos_x = 0;
    static int pos_y = 0;
    // Component Listener
    ListenerFenster lis_component;
    // Flag ob ein Level mit dem Levelreader geladen wurde
    static boolean level_read = false;
    String str_level;

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

        // Initialisierung Controller und Model
        newDionaRap();

        // Navigator hinzufuegen
        navigator = new Navigator(this);

        // Toolbar hinzufuegen
        toolbar = new Toolbar(this);
        this.add(toolbar, BorderLayout.NORTH);

        // Menueleiste hinzufuegen
        this.setJMenuBar(new MenueLeiste(this));

        // Listener hinzufuegen
        this.addComponentListener(lis_component = new ListenerFenster(this, navigator, navigator.nav_pos_const));
        this.addKeyListener(new ListenerKeyEvents());
        this.addMouseListener(new ListenerMaus(this));
        this.setFocusable(true);

        this.pack();
        // Hauptfenster in der Mitte platzieren
        if(pos_y == 0 && pos_x == 0) {
            this.setLocationRelativeTo(null);
        } else {
            this.setLocation(pos_x, pos_y);
        }
        this.setVisible(true);
        this.requestFocusInWindow();
    }

    /**
     * Methode, die die Spielelogik und das Spielfeld initialisiert (DionaRapModel,DionaRapController und Spielfeld)
     *
     */
    public void newDionaRap() {
        dionaRapModel = new DionaRapModel(grid.getGridSizeY(), grid.getGridSizeX(), opponents, obstacles);
        // Anzahl der Munition zu beginn des Spiels
        dionaRapModel.setShootAmount(5);
        // Spielfeld hinzufuegen
        spielfeld = new Spielfeld(this);
        this.add(spielfeld, BorderLayout.CENTER);
        spielfeld.setLastDirection(0);
        dionaRapModel.addModelChangedEventListener(new ListenerModel(this));
        spielfeld.drawAllPawns(getPawns());
        dionaRapController = new DionaRapController(dionaRapModel);
        // Anlegen der Multithreading-Configuration
        addMTConfiguration();
        dionaRapController.setMultiThreaded(conf);
    }

    /**
     * Methode, die die MultiThreading-Variablen setzt
     *
     */
    private void addMTConfiguration() {
        conf = new MTConfiguration();
        conf.setAlgorithmAStarActive(true);
        conf.setAvoidCollisionWithObstacles(true);
        conf.setAvoidCollisionWithOpponent(false);
        conf.setMinimumTime(800);
        conf.setShotGetsOwnThread(true);
        conf.setOpponentStartWaitTime(5000);
        conf.setOpponentWaitTime(2000);
        conf.setShotWaitTime(300);
        conf.setRandomOpponentWaitTime(false);
        conf.setDynamicOpponentWaitTime(false);
    }

    /**
     * get-Methode, gibt die Multithreading-Configuration zurueck
     *
     */
    public MTConfiguration getConf() {
        return conf;
    }

    /**
     * get-Methode, gibt den ListenerBewegung zurueck
     *
     */
    public ListenerFenster getListenerFenster() {
        return lis_component;
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

    /**
     * set-Methode, vertauscht das Spielfeld mit der Toolbar
     *
     * @param falls true wird die Toolbar im CENTER-Bereich positioniert / false dann im SOUTH-Bereich
     */
    public void setToolbarPosition(boolean north) {
        if(north) {
            this.remove(toolbar);
            this.add(toolbar, BorderLayout.NORTH);
            this.pack();

        }
        else {
            this.remove(toolbar);
            this.add(toolbar, BorderLayout.SOUTH);
            this.pack();
        }
    }

    /**
     * set-Methode, setzt den LevelReader auf aktiv
     * @param der Pfad zur ausgewaehlten XML-Datei wird uebergeben
     */
    public void setLevelRead(String str_level) {
        level_read = true;
        this.str_level = str_level;
    }

    /**
     * Methode, erzeugt ein neues Spiel
     */
    public void newGame() {
        pos_y = this.getY();
        pos_x = this.getX();
        this.dispose();
        new Hauptfenster("DionaRap");
    }

    /**
     * Main-Methode erzeugt das Hauptfenster
     *
     * @param args Kommandozeilenparameter (nicht verwendet)
     */
    public static void main(String[] args) {
        Hauptfenster h = new Hauptfenster("DionaRap");
        Thread t1 = new Thread1(h);
        t1.start();
    }

}

class Thread1 extends Thread {
    private Hauptfenster hauptfenster;

    Thread1(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
    }

    public void run() {
        while(true) {
            try{
                if(hauptfenster.getDionaRapModel().getShootAmount() == 0) {
                    JLabel[] arr_ammo = hauptfenster.getToolbar().getArrAmmo();
                    for (int i = 0; i < 100; i++) {
                        arr_ammo[0].setBorder(BorderFactory.createLineBorder(Color.RED));
                        arr_ammo[1].setBorder(BorderFactory.createLineBorder(Color.RED));
                        arr_ammo[2].setBorder(BorderFactory.createLineBorder(Color.RED));
                        hauptfenster.getToolbar().getAmmo().updateUI();
                        Thread.sleep(1000);
                        arr_ammo[0].setBorder(null);
                        arr_ammo[1].setBorder(null);
                        arr_ammo[2].setBorder(null);
                        hauptfenster.getToolbar().getAmmo().updateUI();
                    }
                }
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.err.println("Thread Sleep: " + ex);
            }
        }
    }
}
