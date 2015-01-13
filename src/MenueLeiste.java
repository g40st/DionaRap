import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.GridLayout;

import javax.swing.filechooser.FileFilter;
import java.lang.Exception;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

import javax.swing.*;

import de.fhwgt.dionarap.levelreader.LevelReader;
import de.fhwgt.dionarap.model.data.Grid;
import de.fhwgt.dionarap.model.data.MTConfiguration;

/**
 * MenueLeiste
 * Initialisierung der Menueleiste abgeleitet von <code>JMenuBar</code>, implementiert <code>ActionListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class MenueLeiste extends JMenuBar implements ActionListener {
    private Hauptfenster hauptfenster;
    // Dialog fuer die Spieleinstellungen
    private JDialog settings;
    // Flag, ob die Toolbar sichtbar ist
    private boolean toolbar_view = true;
    // Flag für die Position der Toolbar (wenn North dann true)
    private boolean north = true;
    // Flag, ob die Toolbar sichtbar ist
    private boolean navigator = true;
    // Array indem alle Look and Feels gespeichert werden
    private UIManager.LookAndFeelInfo[] lookandfeelinfo;
    // Fuer jedes Look and Feel ein JRadioButtonMenuItem
    private JRadioButtonMenuItem lookandfeel[];
    // Anzahl der vorhanden Look and Feels
    private int count;
    // zeigt immer auf den aktuell aktiven activeRadioButton
    private int activeRadioButton = 0;

    // Menueleiste Elemente
    private JMenu view;
    private JMenu config;
    private JMenu help;

    // view-Reiter
    private JCheckBoxMenuItem view_toolbar;
    private JMenu position_toolbar;
    private JMenuItem toolbar_oben;
    private JMenuItem toolbar_unten;
    private JCheckBoxMenuItem view_navigator;
    private JMenu look_and_feel;

    // config-Reiter
    private JMenuItem read_level;
    private JMenuItem game_settings;
    private MTConfiguration conf;
    private JTextField [] text = new JTextField[7];
    private JCheckBox box1;
    private JCheckBox box2;
    private JCheckBox box3;

    // help-Reiter
    private JMenuItem game_description;
    private URL url;

    /**
     * Konstruktor der Klasse Toolbar
     * ist fuer die Menueleiste zustaendig (Ansicht-Reiter, Konfiguration-Reiter und Hilfe-Reiter)
     * @param hauptfenster Instanz des <code>Hauptfenster</code>
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

        // Level Reader
        read_level = new JMenuItem("Level einlesen");
        read_level.addActionListener(this);
        config.add(read_level);

        // Spieleinstellungen
        game_settings = new JMenuItem("Spieleinstellungen");
        game_settings.addActionListener(this);
        game_settings.setEnabled(false);
        config.add(game_settings);

        // setzen Separator
        view.insertSeparator(2);
        view.insertSeparator(4);
        config.insertSeparator(1);

        this.add(view);
        this.add(config);
        this.add(help);
    }

    /**
     * Methode, setzt das "Spieleinstellungen-Element" auf aktiv
     */
    public void setGameSettingsEnabled() {
        game_settings.setEnabled(true);
    }

    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * Fuer jedes Element in der Menueleiste wird das jeweilige Event abgearbeitet
     * Events: Toolbar ein-/ausblenden, Toolbar Position oben/unten , Navigator ein-/ausblenden, Spielbeschreibung anzeigen, Look and Feel aendern
     *         Einlesen von XML-Dateien(Levelreader), Aendern der Spieleinstellungen, Hilfe anzeigen
     *@param e
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

        if (e.getSource() == toolbar_oben) { // positionieren der Toolbar am oberen Rand
                north = true;
                hauptfenster.setToolbarPosition(north);
                toolbar_unten.setEnabled(true);
                toolbar_oben.setEnabled(false);
                hauptfenster.pack();
        }

        if (e.getSource() == toolbar_unten) { // positionieren der Toolbar am unteren Rand
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
                chooser.setFileFilter(new ExtensionFileFilter("XML", new String[]{"xml"}));
                int returnValue = chooser.showOpenDialog(hauptfenster);
                if(returnValue == JFileChooser.APPROVE_OPTION) {
                    // Alle Labels des bisherigen Spielfeldes loeschen
                    hauptfenster.getSpielfeld().delAllLabels();
                    // Levelreader ausfuehren
                    LevelReader levelreader = new LevelReader(hauptfenster.getConf(), hauptfenster.getDionaRapModel());
                    levelreader.readLevel(chooser.getSelectedFile().toString());
                    // neues Spielfeld zeichnen
                    hauptfenster.getSpielfeld().addJLabels();
                    hauptfenster.getSpielfeld().drawNew();
                    hauptfenster.getToolbar().updateToolbar(hauptfenster.getDionaRapModel().getShootAmount(), hauptfenster.getDionaRapModel().getScore(), hauptfenster.getProgress());
                    // neue Multithreading-Configuration setzen
                    hauptfenster.getDionaRapController().setMultiThreaded(hauptfenster.getDionaRapModel().getActiveConfiguration());
                    // den Navigator zum Hauptfenster positionieren
                    hauptfenster.pack();
                    ListenerFenster lis = hauptfenster.getListenerFenster();
                    lis.componentMoved(new ComponentEvent(hauptfenster, ComponentEvent.COMPONENT_MOVED));
            }
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

        if (e.getSource() == game_settings) { // erzeugen des Spieleinstellung-Dialoges
            // panel fuer den Dialog
            JPanel panel = new JPanel();
            conf = hauptfenster.getConf();
            for (int i = 0; i < 7; i++) {
                text[i] = new JTextField();
            }
            box1 = new JCheckBox("Zufällige Wartezeiten der Gegner");
            box2 = new JCheckBox("Gegner meiden Kollisionen mit Hindernis");
            box3 = new JCheckBox("Gegner meiden Kollisionen mit anderen Gegnern");
            JButton button1 = new JButton("Übernehmen");
            button1.addActionListener(this);
            JButton button2 = new JButton("Abbrechen");
            button2.addActionListener(this);
            panel.setLayout(new GridLayout(11, 2));
            // setzen der Defaultwerte
            setDefaults(conf);
            // die einzelnen Elemente hinzufuegen
            panel.add(new JLabel("Wartezeit der Gegner zu Beginn: "));
            panel.add(text[0]);
            panel.add(new JLabel("Verzögerung eines Schusses: "));
            panel.add(text[1]);
            panel.add(new JLabel("Wartezeit eines Gegners vor jedem Schritt:    "));
            panel.add(text[2]);
            panel.add(panel.add(new JLabel()));
            panel.add(box1);
            box1.addActionListener(this);
            panel.add(panel.add(new JLabel()));
            panel.add(box2);
            panel.add(panel.add(new JLabel()));
            panel.add(box3);
            panel.add(new JLabel("Anzahl Zeilen des Spielfeldes: "));
            panel.add(text[3]);
            panel.add(new JLabel("Anzahl Spalten des Spielfeldes: "));
            panel.add(text[4]);
            panel.add(new JLabel("Anzahl Hindernisse: "));
            panel.add(text[5]);
            panel.add(new JLabel("Anzahl Gegner: "));
            panel.add(text[6]);
            panel.add(button1);
            panel.add(button2);

            // JDialog erzeugen
            settings = new JDialog(hauptfenster, true);
            settings.add(panel);
            settings.setLocationRelativeTo(hauptfenster);
            settings.setResizable(false);
            settings.pack();
            settings.setVisible(true);
        }

        if(e.getActionCommand() == "Abbrechen") { // Abfrage fuer den "Abbrechen"-Button in den Spieleinstellungen
            settings.dispose();
        }

        if(e.getActionCommand() == "Übernehmen") { // Abfrage fuer den "Uebernehmen"-Button in den Spieleinstellungen
            conf.setOpponentStartWaitTime(Integer.parseInt(text[0].getText()));
            conf.setShotWaitTime(Integer.parseInt(text[1].getText()));
            conf.setOpponentWaitTime(Integer.parseInt(text[2].getText()));
            conf.setRandomOpponentWaitTime(box1.isSelected());
            conf.setAvoidCollisionWithOpponent(box3.isSelected());
            conf.setAvoidCollisionWithObstacles(box2.isSelected());
            hauptfenster.setGrid((Integer.parseInt(text[3].getText())), Integer.parseInt(text[4].getText()));
            hauptfenster.setOpponents((Integer.parseInt(text[6].getText())));
            hauptfenster.setObstacles(Integer.parseInt(text[5].getText()));
            hauptfenster.setGameSettings(); // Flag, dass die Spieleinstellungen geaendert wurden
            JOptionPane.showMessageDialog(settings, "Änderungen werden bei Neustart der Partie übernommen!");
            settings.dispose(); // Spieleinstellungen schließen
            hauptfenster.requestFocus();
        }

        if(e.getSource() == box1) { // setzt das Textfeld auf aktiv und inaktiv (abhängig von der Checkbox)
            if(!((JCheckBox) e.getSource()).isSelected()) {
                text[2].setEnabled(true);
            } else {
                text[2].setEnabled(false);
            }
        }

        for (int i = 0; i < count; i++) { // Abarbeiten aller JRadioButtons und setzen des gewuenschten Look-and-Feels
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


    /**
     * Methode, die die einzelnen Elemente in den Spieleinstellungen mit den aktuellen Werten aus der Multithreading-Config fuellt
     * @param conf aktuelle Multithreading-Konfigurtion
     */
    private void setDefaults(MTConfiguration conf) {
        text[0].setText(String.valueOf(conf.getOpponentStartWaitTime()));
        text[1].setText(String.valueOf(conf.getShotWaitTime()));
        text[2].setText(String.valueOf(conf.getOpponentWaitTime()));
        text[3].setText(String.valueOf(hauptfenster.getGrid().getGridSizeY()));
        text[4].setText(String.valueOf(hauptfenster.getGrid().getGridSizeX()));
        text[5].setText(String.valueOf(hauptfenster.getObstacles()));
        text[6].setText(String.valueOf(hauptfenster.getOpponents()));

        if(conf.isRandomOpponentWaitTime()) {
            box1.setSelected(true);
            text[2].setEnabled(false);
        }
        if(conf.isAvoidCollisionWithObstacles()) {
            box2.setSelected(true);
        }
        if(conf.isAvoidCollisionWithOpponent()) {
            box3.setSelected(true);
        }
    }
}

/**
 * ExtensionFileFilter
 * Erstellen eines FileFilters abgeleitet von <code>FileFilter</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class ExtensionFileFilter extends FileFilter {
    private String description;
    private String extensions[];

    /**
     * Konstruktor der Klasse Toolbar
     * ist fuer die Bereitstellung des FileFilter verantwortlich
     * @param description die Beschreibung des Filters
     * @param extensions[] die verwendeten Dateiendungen
     */
    public ExtensionFileFilter(String description, String extensions[]) {
        this.description = description;
        this.extensions = extensions;
    }

    /**
     * get-Methode, gibt die Beschreibung zurueck
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Methode, erstellt den gewuenschten FileFilter
     * @param file
     * @return boolean
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        } else {
            String path = file.getAbsolutePath();
            for (int i = 0; i < extensions.length; i++) {
                String extension = extensions[i];
                if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
                    return true;
                }
            }
        }
        return false;
    }
}
