import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import java.lang.Exception;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JDialog;
import de.fhwgt.dionarap.levelreader.LevelReader;

/**
 * Menueleiste
 * Initialisierung der Menueleiste abgeleitet von <code>JMenuBar</code>, implementiert <code>ActionListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class MenueLeiste extends JMenuBar implements ActionListener {
    Hauptfenster hauptfenster;
    // Flag, ob die Toolbar sichtbar ist
    boolean toolbar_view = true;
    // Flag für die Position der Toolbar (wenn North dann true)
    boolean north = true;
    // Flag, ob die Toolbar sichtbar ist
    boolean navigator = true;
    // Array indem alle Look and Feels gespeichert werden
    UIManager.LookAndFeelInfo [] lookandfeelinfo;
    // Fuer jedes Look and Feel ein JRadioButtonMenuItem
    JRadioButtonMenuItem lookandfeel [];
    // Anzahl der vorhanden Look and Feels
    int count;
    // zeigt immer auf den aktuell aktiven activeRadioButton
    int activeRadioButton = 0;

    // Menueleiste Elemente
    JMenu view;
    JMenu config;
    JMenu help;

    // view-Reiter
    JCheckBoxMenuItem view_toolbar;
    JMenu position_toolbar;
    JMenuItem toolbar_oben;
    JMenuItem toolbar_unten;
    JCheckBoxMenuItem view_navigator;
    JMenu look_and_feel;

    // config-Reiter
    JMenuItem read_level;

    // help-Reiter
    JMenuItem game_description;
    URL url;

    /**
     * Konstruktor der Klasse Toolbar
     *
     * ist fuer die Menueleiste zustaendig (Ansicht-Reiter, Konfiguration-Reiter und Hilfe-Reiter)
     * @param das Hauptfenster wird uebergeben
     */
    MenueLeiste(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
        view = new JMenu("Ansicht");
        config = new JMenu("Konfiguration");
        help = new JMenu("Hilfe");

        // Toolbar anzeigen
        view_toolbar = new JCheckBoxMenuItem("Toolbar anzeigen");
        view_toolbar.setState(true);
        view_toolbar.addActionListener(this);
        view.add(view_toolbar);

        // Toolbar positionieren
        position_toolbar = new JMenu("Position Toolbar");
        toolbar_oben = new JMenuItem("oben");
        toolbar_unten = new JMenuItem("unten");
        position_toolbar.add(toolbar_oben);
        toolbar_oben.setEnabled(false);
        position_toolbar.add(toolbar_unten);
        toolbar_oben.addActionListener(this);
        toolbar_unten.addActionListener(this);
        view.add(position_toolbar);

        // Navigator anzeigen
        view_navigator = new JCheckBoxMenuItem("Navigator anzeigen");
        view_navigator.setState(true);
        view_navigator.addActionListener(this);
        view.add(view_navigator);

        // Hilfe anzeigen
        game_description = new JMenuItem("Spielbeschreibung");
        help.add(game_description);
        game_description.addActionListener(this);

        // Look and Feel
        look_and_feel = new JMenu("Look and Feel");
        lookandfeelinfo = UIManager.getInstalledLookAndFeels();
        count = lookandfeelinfo.length;
        lookandfeel = new JRadioButtonMenuItem [count];
        for(int i = 0; i < count; i++) {
            lookandfeel[i] = new JRadioButtonMenuItem(lookandfeelinfo[i].getName());
            look_and_feel.add( lookandfeel [i]);
            lookandfeel[i].addActionListener(this);
            if(UIManager.getLookAndFeel().getName().equals(lookandfeelinfo[i].getName())) {
                lookandfeel[i].setSelected(true);
                activeRadioButton = i;
            }
        }
        view.add(look_and_feel);

        //Level Reader
        read_level = new JMenuItem("Level einlesen");
        read_level.addActionListener(this);
        config.add(read_level);

        // setzen Separator
        view.insertSeparator(2);
        view.insertSeparator(4);

        this.add(view);
        this.add(config);
        this.add(help);
    }

    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * Fuer jedes Element in der Menueleiste wird das jeweilige Event abgearbeitet
     * Events: Toolbar ein-/ausblenden, Toolbar Position oben/unten , Navigator ein-/ausblenden, Spielbeschreibung anzeigen, Look and Feel aendern
     *         Einlesen von XML-Dateien(Levelreader)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view_toolbar) { // Ein-/Ausblenden der Toolbar
            if(toolbar_view) {
                hauptfenster.getToolbar().setDisabledToolbar();
                toolbar_view = false;
                position_toolbar.setEnabled(false);
                hauptfenster.pack();
            }
            else {
                hauptfenster.getToolbar().setEnabledToolbar();
                toolbar_view = true;
                position_toolbar.setEnabled(true);
                hauptfenster.pack();
            }
        }

        if (e.getSource() == toolbar_oben) { // positionieren der toolbar am oberen Rand
                north = true;
                hauptfenster.setToolbarPosition(north);
                toolbar_unten.setEnabled(true);
                toolbar_oben.setEnabled(false);
                hauptfenster.pack();
        }

        if (e.getSource() == toolbar_unten) { // positionieren der toolbar am unteren Rand
                north = false;
                hauptfenster.setToolbarPosition(north);
                toolbar_unten.setEnabled(false);
                toolbar_oben.setEnabled(true);
                hauptfenster.pack();
        }

        if (e.getSource() == view_navigator) { // Ein-/Ausblenden des Navigators
            if(navigator) {
                hauptfenster.getNavigator().setDisabledNavigator();
                navigator = false;
                hauptfenster.pack();
            }
            else {
                hauptfenster.getNavigator().setEnabledNavigator();
                navigator = true;
                hauptfenster.pack();
            }
        }

        if (e.getSource() == read_level) { // XML Datei einlesen (LevelReader)
                // Filechooser fuer die XML Datei
                JFileChooser chooser = new JFileChooser(Hauptfenster.getDirectory() + "level");
                chooser.showOpenDialog(hauptfenster);
                // Alle Labels des bisherigen Spielfeldes loeschen
                hauptfenster.getSpielfeld().delAllLabels();
                // Levelreader ausfuehren
                LevelReader levelreader = new LevelReader(hauptfenster.getConf(), hauptfenster.getDionaRapModel());
                levelreader.readLevel(chooser.getSelectedFile().toString());
                // neues Spielfeld zeichnen
                hauptfenster.getSpielfeld().addJLabels();
                hauptfenster.getSpielfeld().delAllPawns();
                hauptfenster.getSpielfeld().drawAllPawns(hauptfenster.getPawns());
                hauptfenster.getToolbar().setAmmoIcons(hauptfenster.getDionaRapModel().getShootAmount());
                // neue Multithreading-Configuration setzen
                hauptfenster.getDionaRapController().setMultiThreaded(hauptfenster.getDionaRapModel().getActiveConfiguration());
                // den Navigator zum Hauptfenster positionieren
                hauptfenster.pack();
                ListenerFenster lis = hauptfenster.getListenerFenster();
                lis.componentMoved(new ComponentEvent(hauptfenster, ComponentEvent.COMPONENT_MOVED));

        }

        if (e.getSource() == game_description) { // Anzeigen der Spielbeschreibung (erzeugen des JDialog und JEditorPane)
            String home_source = Hauptfenster.getDirectory();
            String separator = Hauptfenster.getSeparator();
            JDialog dialog = new JDialog(hauptfenster, "Spielbeschreibung", true);
            JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            try {
                url = new URL("file://" + home_source + separator + "html" + separator + "Spielbeschreibung.html");
            }
            catch (MalformedURLException ex) {
                System.err.println("File kann nicht gelesen werden: " + url);
            }
            try {
                editorPane.setPage(url);
            }
            catch (IOException ex) {
                System.err.println("File kann nicht gelesen werden: " + url);
            }
            JScrollPane editorScrollPane = new JScrollPane(editorPane);
            editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            dialog.add(editorScrollPane);
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(hauptfenster);
            dialog.setVisible(true);
        }

        for (int i = 0; i < count; i++) { // Abarbeiten aller JRadioButtons und setzen des gewuenschten Look and Feel
            if(e.getSource() == lookandfeel[i]) {
                lookandfeel[activeRadioButton].setSelected(false);
                activeRadioButton = i;
                try {
                    UIManager.setLookAndFeel(lookandfeelinfo[i].getClassName());
                    SwingUtilities.updateComponentTreeUI(hauptfenster);
                }
                catch (Exception ex) {
                    System.out.println("Exeception: " + ex);
                }
                hauptfenster.pack();
            }
        }
    }
}