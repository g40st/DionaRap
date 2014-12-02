import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;

/**
 * Toolbar
 * Initialiserung der Toolbar, implementiert <code>JToolBar</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class Toolbar extends JToolBar {
    Hauptfenster hauptfenster;
    JButton newGame;
    JButton list;
    JPanel score;
    JPanel ammo;
    JPanel progress;
    JTextField textfield_score;
    JProgressBar progressbar;

    /**
     * Konstruktor der Klasse Toolbar
     *
     * ist fuer die Toolbar zustaendig (NeuesSpiel-Button, Punktestand, Munition, Spielfortschritt, Bestenliste-Button)
     * @param das Hauptfenster wird uebergeben
     */
    Toolbar(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
        this.setFloatable(false);

        // Anlegen Button
        newGame = new JButton("Neues Spiel");
        newGame.setActionCommand("10");
        newGame.setEnabled(false);
        newGame.addActionListener(new ListenerBewegung());
        this.add(newGame);

        // Anlegen JPanel
        score = new JPanel();
        score.setBorder(BorderFactory.createTitledBorder("Punktestand"));
        score.setToolTipText("Zeigt den Punktestand an");
        textfield_score = new JTextField();
        textfield_score.setEditable(false);
        textfield_score.setColumns(7);
        setScore(hauptfenster.getDionaRapModel().getScore());
        score.add(textfield_score);
        this.add(score);

        // Anlegen Ammo
        ammo = new JPanel();
        ammo.setToolTipText("Zeigt den aktuellen Munitionsvorrat an");
        ammo.setBorder(BorderFactory.createTitledBorder("Munition"));
        this.add(ammo);

        // Anlegen Spielfortschritt
        progress = new JPanel();
        progress.setToolTipText("Zeigt den aktuellen Spielfortschritt an");
        progress.setBorder(BorderFactory.createTitledBorder("Spielfortschritt"));
        progressbar = new JProgressBar(0,100);
        progressbar.setStringPainted(true);
        progressbar.setValue(hauptfenster.getProgress());
        progress.add(progressbar);
        this.add(progress);

        // Anlegen Bestenliste
        list = new JButton("Bestenliste");
        list.setActionCommand("11");
        this.add(list);

    }

    /**
     * Methode setzt den NeuesSpiel-Button auf aktiv
     *
     */
    public void setEnabled() {
        newGame.setEnabled(true);
    }

    /**
     * Methode setzt den NeuesSpiel-Button auf inaktiv
     *
     */
    public void setDisabled() {
        newGame.setEnabled(false);
    }

    /**
     * Methode setzt den Punktestand
     *
     */
    public void setScore(int score) {
        textfield_score.setText(String.valueOf(score));
    }

    /**
     * Methode setzt den Spielfortschritt
     *
     */
    public void setProgress(int progress) {
        progressbar.setValue(progress);
    }

    /**
     * Methode setzt den NeuesSpiel-Button, Punktestand und Spielfortschritt auf default
     *
     */
    public void setDefault() {
        newGame.setEnabled(false);
        textfield_score.setText(String.valueOf(0));
        progressbar.setValue(0);
    }

    /**
     * Methode blendet die Toolbar aus
     *
     */
    public void setDisabledToolbar() {
        this.setVisible(false);
    }

    /**
     * Methode blendet die Toolbar ein
     *
     */
    public void setEnabledToolbar() {
        this.setVisible(true);
    }
}
