package uz.gbway.enavbatthermalprintingservice.config;

import uz.gbway.enavbatthermalprintingservice.ENavbatThermalPrintingServiceApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TrayIconApp {
    public static void showTrayIcon() throws Exception {

        System.setProperty("java.awt.headless", "false");

        if (!SystemTray.isSupported()) {
            System.out.println("Tray not supported.");
            return;
        }

        // 🔥 Faylni resources ichidan o‘qish
        InputStream iconStream = TrayIconApp.class.getClassLoader().getResourceAsStream("static/icon/icon.png");
        if (iconStream == null) {
            System.out.println("Icon not found in resources");
            return;
        }

        Image image = ImageIO.read(iconStream);

        PopupMenu popup = new PopupMenu();

        // 🔁 Restart menyusi
        MenuItem restartItem = new MenuItem("Restart");
        restartItem.addActionListener(e -> {
            try {
                restartApplication();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        MenuItem exitItem = new MenuItem("Chiqish");
        exitItem.addActionListener(e -> System.exit(0));

        popup.add(restartItem);
        popup.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(image, "E-Navbat Printer Service", popup);
        trayIcon.setImageAutoSize(true);
        SystemTray.getSystemTray().add(trayIcon);

        System.out.println("Tray supported!"); // keyingi kodlarni shu yerga qo‘shasiz

    }

    // 🔄 Dastur qayta ishga tushirish metodi
    private static void restartApplication() throws IOException {
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        File currentJar = new File(ENavbatThermalPrintingServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        if (!currentJar.getName().endsWith(".jar")) {
            System.out.println("JAR fayl emas. Qayta ishga tushirish bekor qilindi.");
            return;
        }

        final String[] command = new String[] { javaBin, "-jar", currentJar.getPath() };

        new ProcessBuilder(command).start();
        System.exit(0); // eski jar ni yopamiz
    }

}
