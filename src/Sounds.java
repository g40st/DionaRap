import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Sounds
 * Erzeugen der Objekte fuer die Sounds
 * Copyright (c) 2014
 * @author Christian Hoegerle / Thomas Buck
 * @version 1.0
 */
class Sounds {
    private Hauptfenster hauptfenster;
    private AudioInputStream gameWon;
    private AudioInputStream gameOver;
    private AudioInputStream move;
    private AudioInputStream shoot;
    private AudioInputStream laser_shoot;
    private Clip clip_gameWon;
    private Clip clip_gameOver;
    private Clip clip_move;
    private Clip clip_shoot;
    private Clip clip_laserShoot;

    /**
     * Konstruktor der Klasse Sounds
     *
     * ist fuer die Erzeugung der Sounds zustaendig
     * @param das Hauptfenster wird uebergeben
     */
    public Sounds(Hauptfenster hauptfenster) {
        this.hauptfenster = hauptfenster;
        String sounds_dir = Hauptfenster.getDirectory() + "sound" + Hauptfenster.getSeparator();

        // die einzelnen Audiostreams erzeugen
        try {
            gameOver = AudioSystem.getAudioInputStream(new File (sounds_dir + "GameOver.wav"));
        } catch (Exception exception){System.out.println("Fehler beim Oeffnen der Sounddatei 'GameOver.wav'" + exception);}
        try {
            gameWon = AudioSystem.getAudioInputStream(new File(sounds_dir + "Gewonnen.wav"));
        } catch (Exception exception){System.out.println("Fehler beim Oeffnen der Sounddatei 'Gewonnen.wav'");}
        try {
            move = AudioSystem.getAudioInputStream(new File(sounds_dir + "Maustaste.wav"));
        } catch (Exception exception){System.out.println("Fehler beim Oeffnen der Sounddatei 'Maustaste.wav'");}
        try {
            shoot = AudioSystem.getAudioInputStream(new File(sounds_dir + "44MAG.wav"));
        } catch (Exception exception){System.out.println("Fehler beim Oeffnen der Sounddatei '44MAG.wav'");}
        try {
            laser_shoot = AudioSystem.getAudioInputStream(new File(sounds_dir + "Laser_Waffe.wav"));
        } catch (Exception exception){System.out.println("Fehler beim Oeffnen der Sounddatei 'Laser_Waffe.wav'");}

        // Clips aus einem AudioInputStream in den Speicher laden (preload)
        try {
            // gameOver
            DataLine.Info info = new DataLine.Info(Clip.class, gameOver.getFormat());
            clip_gameOver = (Clip) AudioSystem.getLine(info);
            // gameWon
            info = new DataLine.Info(Clip.class, gameWon.getFormat());
            clip_gameWon = (Clip) AudioSystem.getLine(info);
            // Bewegung
            info = new DataLine.Info(Clip.class, move.getFormat());
            clip_move = (Clip) AudioSystem.getLine(info);
            // Schuss Magnum
            info = new DataLine.Info(Clip.class, shoot.getFormat());
            clip_shoot = (Clip) AudioSystem.getLine(info);
            // Schuss Laser
            info = new DataLine.Info(Clip.class, laser_shoot.getFormat());
            clip_laserShoot = (Clip) AudioSystem.getLine(info);
        } catch (LineUnavailableException event) {
            System.out.println("Fehler beim Erzeugen eines Clip-Objektes!");
            event.printStackTrace();
        }
        // Clips öffnen
        try {
            clip_gameOver.open(gameOver);
            clip_gameWon.open(gameWon);
            clip_move.open(move);
            clip_shoot.open(shoot);
            clip_laserShoot.open(laser_shoot);
        } catch (LineUnavailableException e) {
            System.out.println("Fehler beim öffnen eines Clip");
            e.printStackTrace();
        } catch (IOException e) {}
    }

    /**
     * Methode, spielt den Sound, wenn das Spiel verloren wurde
     *
     */
    public void playGameOver() {
        try {
            clip_gameOver.start();
        }
        catch (Exception exception) {
            System.out.println("Fehler beim Abspielen der Sounddatei: GameOver.wav");
        }
    }

    /**
     * Methode, spielt den Sound, wenn das Spiel gewonnen wurde
     *
     */
    public void playGameWon() {
        try {
            clip_gameWon.start();
        }
        catch (Exception exception) {
            System.out.println("Fehler beim Abspielen der Sounddatei: Gewonnen.wav");
        }
    }
    /**
     * Methode, spielt den Sound, wenn sich der Spieler auf dem Spielfeld bewegt hat
     *
     */
    public void playMove() {
        try {
            clip_move.stop();
            clip_move.setFramePosition(0);
            clip_move.start();
        }
        catch (Exception exception) {
            System.out.println("Fehler beim Abspielen der Sounddatei: Mausklick.wav");
        }
    }

    /**
     * Methode, spielt den Sound, wenn der Spieler einen Schuss abgegeben hat (abhaengig vom aktuellen Theme)
     *
     */
    public void playShoot() {
        try {
            clip_shoot.stop();
            clip_shoot.setFramePosition(0);
            clip_laserShoot.stop();
            clip_laserShoot.setFramePosition(0);
            // Anpassen des Sounds je nach Theme
            if(hauptfenster.getTheme().equals("Dracula")) {
                clip_shoot.start();
            } else {
                clip_laserShoot.start();
            }
        }
        catch (Exception exception) {
            System.out.println("Fehler beim Abspielen der Schuss-Sounddatei!");
        }
    }

     /**
     * Methode, beendet alle aktiven Soundwiedergaben
     *
     */
    public void stopPlaying() {
        clip_gameOver.stop();
        clip_gameWon.stop();
        clip_move.stop();
        clip_shoot.stop();
        clip_laserShoot.stop();
    }
}