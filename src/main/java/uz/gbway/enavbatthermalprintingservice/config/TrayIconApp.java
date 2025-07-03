package uz.gbway.enavbatthermalprintingservice.config;

import javax.imageio.ImageIO;
import java.awt.*;
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
        MenuItem exitItem = new MenuItem("Chiqish");
        exitItem.addActionListener(e -> System.exit(0));
        popup.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(image, "Mening Ilovam", popup);
        trayIcon.setImageAutoSize(true);
        SystemTray.getSystemTray().add(trayIcon);

        System.out.println("Tray supported!"); // keyingi kodlarni shu yerga qo‘shasiz

    }

}
