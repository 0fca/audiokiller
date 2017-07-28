/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiokiller;

import audiokiller.gui.AudioKillerGUI;
import audiokiller.gui.parser.JSONController;
import audiokiller.gui.parser.SettingsDataWrapper;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author obsidiam
 */
public class AudioKiller {
    private static String[] settings = new String[5];
    private static String PATH = System.getProperty("user.home")+"/audiokiller/",COMM = "";
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        new File(System.getProperty("user.home")+"/audiokiller").mkdir();
        loadSettings();
        new AudioKiller().addSystemTray();
    }
    
    
    protected void addSystemTray(){
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(new ImageIcon(AudioKiller.class.getClass().getResource("/audiokiller/images/audio-speakers.png")).getImage());
        final SystemTray tray = SystemTray.getSystemTray();
        trayIcon.setToolTip("StartUp!");
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem settingsItem = new MenuItem("Settings");
        
        aboutItem.addActionListener(listener ->{
            settings[0] = "1";
            settings[1] = COMM;
            settings[2] = PATH;
            AudioKillerGUI.main(settings);
        });
      
        settingsItem.addActionListener(listener ->{
            settings[0] = "0";
            settings[1] = COMM;
            settings[2] = PATH;
            AudioKillerGUI.main(settings);
        });
        
        popup.add(aboutItem);
        popup.add(settingsItem);
        popup.addSeparator();
        
        for(String item : COMM.split(";")){
            String name = item.split(":")[0];
            String cmd = item.split(":")[1];
            
            MenuItem mi = new MenuItem(name);
            mi.addActionListener(listener ->{
                Runtime r = Runtime.getRuntime();
                String[] cmds = cmd.split(",");
                try {
                    for(int i = 0; i < cmds.length; i++){
                        r.exec(cmds[i]);
                    }
                } catch (IOException ex) {
                   JOptionPane.showMessageDialog(null, "Bad command given!","Error",JOptionPane.ERROR_MESSAGE);
                }
            });
            popup.add(mi);
        }
        
        exitItem.addActionListener(exit_action ->{
            System.exit(0);
        });
       
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
    
    private static void loadSettings() throws FileNotFoundException{
        JSONController parser = JSONController.getInstance();
        if(new File(System.getProperty("user.home")+"/audiokiller/settings.json").exists()){
            parser.setFileName(System.getProperty("user.home")+"/audiokiller/settings.json");
            SettingsDataWrapper settings = parser.readSettingsData();
            COMM = settings.getCommand();
        }
    }
}
