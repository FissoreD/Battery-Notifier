import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.*;

public class Main {
    /**
     * commandForPercentage = "WMIC PATH Win32_Battery Get EstimatedChargeRemaining"
     * commandForPluggedCharger = "WMIC Path Win32_Battery Get BatteryStatus
     */

    private SystemTray tray;
    public Tray trayIcon;
    public final String pathFile = System.getProperty("user.home") + "/batterySetting.txt";
    private MyRunnable myRunnable;

    public int min = 15;
    public int max = 90;
    public int sec = 20;

    public static void main(String[] args) {
        Main m = new Main();
        m.initialize();
    }

    public void initialize() {
        setVarsFromFile();
        displayTray();
        createNewRunnable();
        if (SystemTray.isSupported()) {
            myRunnable.start();
        } else {
            System.err.println("System tray not supported!");
        }
    }

    private void displayTray() {
        tray = SystemTray.getSystemTray();
        try {
            InputStream stream = getClass().getResourceAsStream("battery1.png");
            assert stream != null;
            ImageIcon image = new ImageIcon(ImageIO.read(stream));
            System.out.println(image.getImage());
            trayIcon = new Tray(this, image.getImage());
            try {
                tray.add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setVarsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] res = currentLine.split(",");
                int val = Integer.parseInt(res[1]);
                switch (res[0]) {
                    case "sec" -> sec = val;
                    case "min" -> min = val;
                    case "max" -> max = val;
                }
            }
        } catch (NumberFormatException | IOException e) {
            File yourFile = new File(pathFile);
            try {
                yourFile.createNewFile();
                setVarsFromFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile))) {
            writer.write(String.format("sec,%d\nmin,%d\nmax,%d", sec, min, max));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MyRunnable getMyRunnable() {
        return myRunnable;
    }

    public void createNewRunnable() {
        myRunnable = new MyRunnable(this);
    }

    public void removeIcon() {
        tray.remove(trayIcon);
    }
}