package uz.gbway.enavbatthermalprintingservice.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegistryAutoStarter {


    private static final String KEY_NAME = "ENavbatPrinter";
    private static final String REG_PATH = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";

    public static void enableAutoStart() {
        try {
            // Jar fayl yo'lini olish
            String jarPath = System.getProperty("java.class.path");
            Path path = Paths.get(jarPath);

            // Avto-start uchun registry yo'li
            String key = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
            String valueName = "ENavbatPrinter";
            String command = "\"" + path.toAbsolutePath().toString() + "\"";

            // Windows registryga qo'shish
            Process process = Runtime.getRuntime().exec(
                    "reg add " + key + " /v " + valueName + " /d " + command + " /f"
            );

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ Auto start muvaffaqiyatli qo‘shildi!");
            } else {
                System.err.println("❌ Auto start qo‘shilmadi. Exit code: " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("Xatolik: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
