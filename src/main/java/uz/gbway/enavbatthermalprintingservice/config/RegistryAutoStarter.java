package uz.gbway.enavbatthermalprintingservice.config;

import java.io.File;

public class RegistryAutoStarter {


    private static final String KEY_NAME = "ENavbatPrinter";
    private static final String REG_PATH = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";

    public static void enableAutoStart() {
        try {
            String exePath = new File(RegistryAutoStarter.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).getPath();

            // Jar bo‘lsa, uni exe deb hisoblaymiz (InnoSetup konvertatsiyasidan so‘ng)
            if (exePath.endsWith(".jar")) {
                exePath = exePath.replace(".jar", ".exe");
            }

            String regCommand = String.format("reg add \"%s\" /v \"%s\" /t REG_SZ /d \"%s\" /f",
                    REG_PATH, KEY_NAME, exePath);

            Process process = Runtime.getRuntime().exec(regCommand);
            process.waitFor();

            System.out.println("✅ Avto-start registryga qo‘shildi.");
        } catch (Exception e) {
            System.err.println("❌ Avto-start qo‘shishda xatolik: " + e.getMessage());
        }
    }

}
