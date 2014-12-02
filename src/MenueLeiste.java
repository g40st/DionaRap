import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;

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

public class MenueLeiste extends JMenuBar implements ActionListener {
    Hauptfenster hauptfenster;
    // Flag das anzeigt, ob die Toolbar sichtbar ist
    boolean toolbar_view = true;
    // Flag für die Position der Toolbar (wenn Center dann true)
    boolean center = true;
    // // Flag das anzeigt, ob die Toolbar sichtbar ist
    boolean navigator = true;
    // Look and Feel
    UIManager.LookAndFeelInfo [] lookandfeelinfo;
    JRadioButtonMenuItem lookandfeel [];
    int count;
    int activeRadioButton = 0;

    // Menüleiste Elemente
    JMenu view;
    JMenu config;
    JMenu help;

    // view
    JCheckBoxMenuItem view_toolbar;
    JMenu position_toolbar;
    JMenuItem toolbar_oben;
    JMenuItem toolbar_unten;
    JCheckBoxMenuItem view_navigator;
    JMenu look_and_feel;

    // config
    JMenuItem read_level;

    // help
    JMenuItem game_description;
    URL URL;

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

        view.insertSeparator(2);
        view.insertSeparator(4);
        this.add(view);
        this.add(config);
        this.add(help);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view_toolbar) {
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

        if (e.getSource() == toolbar_oben) {
                center = true;
                hauptfenster.setToolbarPosition(center);
                toolbar_unten.setEnabled(true);
                toolbar_oben.setEnabled(false);
                hauptfenster.pack();
        }

        if (e.getSource() == toolbar_unten) {
                center = false;
                hauptfenster.setToolbarPosition(center);
                toolbar_unten.setEnabled(false);
                toolbar_oben.setEnabled(true);
                hauptfenster.pack();
        }

        if (e.getSource() == view_navigator) {
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

        if (e.getSource() == game_description) {
            String home_source = Hauptfenster.getDirectory();
            String separator = Hauptfenster.getSeparator();
            JDialog dialog = new JDialog(hauptfenster, "Spielbeschreibung", true);
            JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            try {
                URL = new URL("file://" + home_source + separator + "html" + separator + "Spielbeschreibung.html");
            }
            catch (MalformedURLException ex) {
                System.err.println("File kann nicht gelesen werden: " + URL);
            }
            try {
                editorPane.setPage(URL);
            }
            catch (IOException ex) {
                System.err.println("File kann nicht gelesen werden: " + URL);
            }
            JScrollPane editorScrollPane = new JScrollPane(editorPane);
            editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            dialog.add(editorScrollPane);
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(hauptfenster);
            dialog.setVisible(true);
        }

        for (int i = 0; i < count; i++) {
            if(e.getSource() == lookandfeel[i]) {
                lookandfeel[activeRadioButton].setSelected(false);
                activeRadioButton = i;
                try {
                    UIManager.setLookAndFeel(lookandfeelinfo[i].getClassName());
                    SwingUtilities.updateComponentTreeUI(hauptfenster);
                }
                catch (Exception ex) {
                    System.out.println("Exeception: ");
                }
                hauptfenster.pack();
            }
        }
    }
}