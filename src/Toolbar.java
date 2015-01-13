import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.GridLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import java.io.IOException;
import de.fhwgt.dionarap.view.HighScoreFile;

/**
 * Toolbar
 * Initialisierung der Toolbar, abgeleitet von <code>JToolBar</code>, implementiert <code>ActionListener</code>
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
public class Toolbar extends JToolBar implements ActionListener {
    private Hauptfenster hauptfenster;
    // Separator
    private String separator = Hauptfenster.getSeparator();
    private String [] arr_list;
    private JButton newGame;
    private JButton list;
    private JPanel score;
    private JPanel ammo;
    private JPanel progress;
    private JTextField textfield_score;
    private JProgressBar progressbar;
    private JLabel arr_ammo[] = new JLabel [3];
    private int countAmmo;

    /**
     * Konstruktor der Klasse Toolbar
     * ist fuer die Toolbar zustaendig ("NeuesSpiel"-Button, Punktestand, Munition, Spielfortschritt, Bestenliste-Button)
     * @param hauptfenster Instanz des <code>Hauptfenster</code>
     */
    Toolbar(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
        this.setFloatable(false);

        // Anlegen Button
        newGame = new JButton("Neues Spiel");
        newGame.setActionCommand("10");
        newGame.setEnabled(false);
        newGame.addActionListener(this);
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
        list.addActionListener(this);
        this.add(list);
    }

    /**
     * get-Methode gibt die Labels der Munitionsanzeige zurueck (benoetigt im Thread)
     * @return JLabel[]
     */
    public JLabel[] getArrAmmo() {
        return arr_ammo;
    }

    /**
     * get-Methode gibt das Panel zurueck indem sich die Munitionsanzeige befindet (benoetigt im Thread)
     * @return JPanel
     */
    public JPanel getAmmo() {
        return ammo;
    }

    /**
     * Methode setzt den "Neues Spiel"-Button auf aktiv
     */
    public void setEnabled() {
        newGame.setEnabled(true);
    }

    /**
     * Methode setzt den "Neues Spiel"-Button auf inaktiv
     */
    public void setDisabled() {
        newGame.setEnabled(false);
    }

    /**
     * Methode setzt den Punktestand
     */
    private void setScore(int score) {
        textfield_score.setText(String.valueOf(score));
    }

    /**
     * Methode setzt den Spielfortschritt
     */
    private void setProgress(int progress) {
        progressbar.setValue(progress);
    }

    /**
     * Methode blendet die Toolbar aus
     */
    public void setDisabledToolbar() {
        this.setVisible(false);
    }

    /**
     * Methode blendet die Toolbar ein
     */
    public void setEnabledToolbar() {
        this.setVisible(true);
    }

    /**
     * Methode zeichnet die Munitionsanzeige in der Toolbar
     * @param countAmmo Anzahl der Munition
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

    /**
     * Methode updatet die Toolbar-Elemente (Munitionsanzeige, Punktestand, Fortschritt)
     *@param countAmmo aktuelle Munitionsvorrat
     *@param score aktueller Punktestand
     *@param progress aktueller Spielfortschritt
     */
    public void updateToolbar(int countAmmo, int score, int progress) {
        setAmmoIcons(countAmmo);
        setScore(score);
        setProgress(progress);
    }

    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * Fuer jedes Element in der Tollbar wird das Event verarbeietet
     * Events: "Neues Spiel"-Button wird betaetigt, Anzeigen der Bestenliste in einem JFrame
     * @param e Event fuer Buttons "Neues Spiel" und "Bestenliste"
     */
    public void actionPerformed(ActionEvent e) {
        if(Integer.parseInt(e.getActionCommand()) == 10) { // ActionEvent fuer den "Neues Spiel"-Button in der Toolbar
            hauptfenster.newGame();
            hauptfenster.requestFocus();
        }
        if(Integer.parseInt(e.getActionCommand()) == 11) { // ActionEvent fuer den "Bestenliste"-Button in der Toolbar
            String[] columnNames = {"Spielername", "Punkte", "Rang"};
            JFrame blist = new JFrame("Bestenliste");

            // Auslesen der Bestenliste
            try{
                arr_list = HighScoreFile.getInstance().readFromHighscore();
            } catch (IOException ex) {
                System.err.println("File kann nicht gelesen werden: " + ex);
            }

            // 2-dimensoniales Array das der JTable uebergeben wird
            String [][] arr_list2d = new String [10][3];
            int count = 0;
            for (int i = 0; i < 10; i++) { // fuellen des 2D-Arrays
                for(int k = 0; k < 2; k++) {
                    arr_list2d[i][k] = arr_list[count];
                    count++;
                }
                arr_list2d[i][2] = String.valueOf(i+1);
            }
            JTable table = new JTable(arr_list2d, columnNames);
            table.setRowHeight(30);
            blist.add(new JScrollPane(table));
            blist.setLocationRelativeTo(hauptfenster);
            blist.setSize(350,350);
            blist.setVisible(true);
        }
    }
}
