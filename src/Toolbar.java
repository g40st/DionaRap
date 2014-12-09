import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.GridLayout;
import java.awt.ComponentOrientation;

/**
 * Toolbar
 * Initialiserung der Toolbar, implementiert <code>JToolBar</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class Toolbar extends JToolBar {
    Hauptfenster hauptfenster;
    // Separator
    String separator = Hauptfenster.getSeparator();
    JButton newGame;
    JButton list;
    JPanel score;
    JPanel ammo;
    JPanel progress;
    JTextField textfield_score;
    JProgressBar progressbar;
    JLabel arr_ammo[] = new JLabel [3];
    int countAmmo;

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
        ammo.setLayout(new GridLayout(1, 3, 10, 10));
        for(int i = 0; i < 3; i++) {
            arr_ammo[i] = new JLabel();
            arr_ammo[i].setSize(30 ,30);
            arr_ammo[i].setPreferredSize(new Dimension(30, 30));
            arr_ammo[i].setHorizontalAlignment(JLabel.CENTER);
        }
        setAmmoIcons(hauptfenster.getDionaRapModel().getShootAmount());

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
     * Methode setzt den "Neues Spiel"-Button auf aktiv
     *
     */
    public void setEnabled() {
        newGame.setEnabled(true);
    }

    /**
     * Methode setzt den "Neues Spiel"-Button auf inaktiv
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

    /**
     * Methode zeichnet die Munitionsanzeige in der Toolbar
     *
     */
    public void setAmmoIcons(int countAmmo) {
        String img_source = Hauptfenster.getDirectory() + "image" + separator + hauptfenster.getTheme();
        ImageIcon img_ammo = new ImageIcon(img_source + separator + "ammo_small.png");
        if(this.countAmmo != countAmmo) {
            this.countAmmo = countAmmo;
            for (int i = 0; i < 3 ; i++) {
                arr_ammo[i].setText(null);
                arr_ammo[i].setIcon(null);
                arr_ammo[i].setBorder(null);
                ammo.remove(arr_ammo[i]);
                ammo.add(arr_ammo[i]);
            }
            if(countAmmo <= 3) {
                for (int i = 0; i < countAmmo; i++) {
                    arr_ammo[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    arr_ammo[i].setIcon(img_ammo);
                    ammo.add(arr_ammo[i]);
                }
            } else {
                arr_ammo[0].setBorder(null);
                arr_ammo[0].setText("*" + String.valueOf(countAmmo));
                ammo.add(arr_ammo[0]);
                arr_ammo[1].setIcon(img_ammo);
                arr_ammo[1].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                ammo.add(arr_ammo[1]);
                arr_ammo[2].setIcon(img_ammo);
                arr_ammo[2].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                ammo.add(arr_ammo[2]);
            }
        }
        ammo.updateUI();
    }
}
