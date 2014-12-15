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
import de.fhwgt.dionarap.model.data.MTConfiguration;
import de.fhwgt.dionarap.model.data.Grid;
import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Player;
import de.fhwgt.dionarap.model.objects.Obstacle;
import de.fhwgt.dionarap.model.objects.Ammo;
import de.fhwgt.dionarap.model.objects.Opponent;
import de.fhwgt.dionarap.controller.DionaRapController;

/**
 * Hauptfenster
 * Initialiserung des Hauptfensters
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class Hauptfenster extends JFrame {
    // ein Spielfeld anlegen
    static Grid grid = new Grid(10, 16);
    // Anzahl der Gegner
    static int opponents = 0;
    // Anzahl der Hindernisse
    static int obstacles = 4;
    // Multithreading-Configuration
    static MTConfiguration conf = new MTConfiguration();
    // ausgewaehltes Theme zu beginn
    private static String theme = "Dracula";
    private DionaRapModel dionaRapModel;
    private DionaRapController dionaRapController;
    private Navigator navigator;
    private Spielfeld spielfeld;
    private Toolbar toolbar;
    private MenueLeiste menubar;
    private Sounds sounds;
    // Flag falls die Spieleinstellungen angepasst wurden
    private static boolean game_settings = false;
    // fuer die Position des Fensters
    private static int pos_x = 0;
    private static int pos_y = 0;
    // Component Listener
    private ListenerFenster lis_component;
    // Thread fuer das Blinken der Munitionsanzeige
    private Thread t1;
    // Thread fuer das Blinken der benachbarten Felder
    private Thread2 t2;

    /**
     * Standard Konstruktor der Klasse Hauptfenster
     *
     * Setzt die Fenstereingenschaften: Titel, Groesse, Sichtbarkeit, Verhalten des Exit-Knopfes, erzeugen des Spielfeldes(JPanel), erzuegen des Navigators (JWindow)
     * @param String aus der public Methode wird uebergeben
     */
    Hauptfenster(String text) {
        super(text);

        // Klasse fuer die Sounds
        sounds = new Sounds(this);

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
        this.setJMenuBar(menubar = new MenueLeiste(this));

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
        // Ein Ammo-Objekt auf dem Spielfeld anlegen
        dionaRapModel.addAmmo(new Ammo(4,5));
        dionaRapModel.addObstacle(new Obstacle(5,7));
        // Spielfeld hinzufuegen
        spielfeld = new Spielfeld(this);
        this.add(spielfeld, BorderLayout.CENTER);
        spielfeld.setLastDirection(0);
        dionaRapModel.addModelChangedEventListener(new ListenerModel(this));
        spielfeld.drawAllPawns(getPawns());
        dionaRapController = new DionaRapController(dionaRapModel);
        // Anlegen der Multithreading-Configuration, wird nur aufgerufen wenn die Spieleinstellungen nicht angepasst wurden
        if(!game_settings) addMTConfiguration();
        dionaRapController.setMultiThreaded(conf);
    }

    /**
     * Methode, die die MultiThreading-Variablen setzt
     *
     */
    private void addMTConfiguration() {
        conf.setAlgorithmAStarActive(true);
        conf.setAvoidCollisionWithObstacles(true);
        conf.setAvoidCollisionWithOpponent(true);
        conf.setMinimumTime(800);
        conf.setShotGetsOwnThread(true);
        conf.setOpponentStartWaitTime(5000);
        conf.setShotWaitTime(300);
        conf.setRandomOpponentWaitTime(false);
        conf.setOpponentWaitTime(1500);
        conf.setDynamicOpponentWaitTime(false);
    }

    /**
     * Methode, setzt das Spieleinstellungen Flag auf true, wenn diese geändert wurden
     *
     */
    public void setGameSettings() {
        game_settings = true;
    }

    /**
     * get-Methode, gibt die Menuebar zurueck
     *
     */
    public MenueLeiste getMenubar() {
        return menubar;
    }

    /**
     * get-Methode, gibt die Klasse Sounds zurueck
     *
     */
    public Sounds getSounds() {
        return sounds;
    }

    /**
     * get-Methode, gibt die Multithreading-Configuration zurueck
     *
     */
    public MTConfiguration getConf() {
        return conf;
    }

    /**
     * get-Methode, gibt das Grid zurueck
     *
     */
    public Grid getGrid() {
        return grid;
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
     * get-Methode, gibt den Thread fuer das Blinken der Munitionsanzeige zurueck
     *
     */
    public Thread getThread1() {
        return t1;
    }

    /**
     * set-Methode, startet den Thread fuer das Blinken der Munitionsanzeige
     *
     */
    public void createNewThread1() {
        t1 = new Thread1(this);
        t1.start();
    }

    /**
     * get-Methode, gibt den Thread fuer das Blinken der benachbarten Felder zurueck
     *
     */
    public Thread getThread2() {
        return t2;
    }

    /**
     * Methode, startet den Thread fuer das Blinken der benachbarten Felder
     *
     */
    public void createNewThread2() {
        t2 = new Thread2(this);
        t2.start();
    }

    public void stopThread2Run() {
        t2.stopRun();
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
    }
}

/**
 * Thread1
 * ist fuer das Blinken das Munitionsanzeige zustaendig
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class Thread1 extends Thread {
    private Hauptfenster hauptfenster;
    private static final long blinkDelay = 500;

    /**
     * Konstruktor des Thread vom Typ <code>Thread</code>.
     * Zuweisen der Hauptfensters
     *
     * @param bekommt das Hauptfenster uebergeben
     */
    Thread1(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
    }

    /**
     * run-Methode, ist fuer das Blinken(3x) der Munitionsanzeige zustaendig
     */
    public void run() {
        for (int i = 0; (i < 6) && (hauptfenster.getDionaRapModel().getShootAmount() == 0); i++) {
            JLabel[] ammo = hauptfenster.getToolbar().getArrAmmo();
            for (int k = 0; k < 3; k++) {
                if ((i % 2) == 0) {
                    ammo[k].setBorder(BorderFactory.createLineBorder(Color.RED));
                } else {
                    ammo[k].setBorder(BorderFactory.createEmptyBorder());
                }
                hauptfenster.getToolbar().getAmmo().updateUI();
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException ex) { }
        }
    }
}


/**
 * Thread2
 * Blinken der Felder wenn der Spieler auf ein nicht benachbartes Feld klickt
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class Thread2 extends Thread {
    private Hauptfenster hauptfenster;
    private boolean run = true;
    private static final long blinkDelay = 500;
    private JLabel[][] label;
    private int y;
    private int x;

    /**
     * Konstruktor des Thread vom Typ <code>Thread</code>.
     * Zuweisen der Hauptfensters
     *
     * @param bekommt das Hauptfenster uebergeben
     */
    Thread2(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
    }

    /**
     * run-Methode, ist fuer das Blinken(4x) der benachbarten Felder zustaendig
     */
    public void run() {
        label = new JLabel [hauptfenster.getDionaRapModel().getGrid().getGridSizeY()][hauptfenster.getDionaRapModel().getGrid().getGridSizeX()];
        label = hauptfenster.getSpielfeld().getLabel();
        // aktuelle Position des Spielers
        y = hauptfenster.getPlayer().getY();
        x = hauptfenster.getPlayer().getX();

        for (int i = 0; (i < 8) && (run); i++) {
            if ((i % 2) == 0) {
                if(!isObstacle(y + 1, x - 1)) { // Richtung 1
                    label[y+1][x-1].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
                if(!isObstacle(y + 1, x)) { // Richtung 2
                    label[y+1][x].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
                if(!isObstacle(y + 1, x + 1)) { // Richtung 3
                    label[y+1][x+1].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
                if(!isObstacle(y, x - 1)) { // Richtung 4
                    label[y][x-1].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
                if(!isObstacle(y, x + 1)) { // Richtung 6
                    label[y][x+1].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
                if(!isObstacle(y - 1, x - 1)) { // Richtung 7
                    label[y-1][x-1].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
                if(!isObstacle(y - 1, x)) { // Richtung 8
                    label[y-1][x].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
                if(!isObstacle(y - 1, x + 1)) { // Richtung 9
                    label[y-1][x+1].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.RED));
                }
            } else {
                label[y+1][x-1].setBorder(BorderFactory.createEmptyBorder());
                label[y+1][x].setBorder(BorderFactory.createEmptyBorder());
                label[y+1][x+1].setBorder(BorderFactory.createEmptyBorder());
                label[y][x-1].setBorder(BorderFactory.createEmptyBorder());
                label[y][x+1].setBorder(BorderFactory.createEmptyBorder());
                label[y-1][x-1].setBorder(BorderFactory.createEmptyBorder());
                label[y-1][x].setBorder(BorderFactory.createEmptyBorder());
                label[y-1][x+1].setBorder(BorderFactory.createEmptyBorder());
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException ex) { }
        }
        // Als letzten Schritt den Border wieder entfernen
        if(!run) {
            label[y+1][x-1].setBorder(BorderFactory.createEmptyBorder());
            label[y+1][x].setBorder(BorderFactory.createEmptyBorder());
            label[y+1][x+1].setBorder(BorderFactory.createEmptyBorder());
            label[y][x-1].setBorder(BorderFactory.createEmptyBorder());
            label[y][x+1].setBorder(BorderFactory.createEmptyBorder());
            label[y-1][x-1].setBorder(BorderFactory.createEmptyBorder());
            label[y-1][x].setBorder(BorderFactory.createEmptyBorder());
            label[y-1][x+1].setBorder(BorderFactory.createEmptyBorder());
        }
    }
    /**
     * Methode, prueft ob sich ein Hinderniss an den uebergebenen Koordinaten befindet
     * true: wenn sich ein Hindernis an den uebergebenen Koordinaten befindet
     * false: wenn sich kein Hindernis an den uebergebenen Koordinaten befindet
     */
    public boolean isObstacle(int y, int x) {
        AbstractPawn[] spielfiguren = hauptfenster.getPawns();
        for(int i = 0; i < (int) spielfiguren.length; i++) {
            if (spielfiguren[i] instanceof Obstacle) {
                if((spielfiguren[i].getY() == y) && (spielfiguren[i].getX() == x)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Methode, setzt das run-Flag auf false, dadurch wird die for-Schleife in der run-Methode beendet
     */
    public void stopRun() {
        run = false;
    }
}
