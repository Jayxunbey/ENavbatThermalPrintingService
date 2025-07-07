package uz.gbway.enavbatthermalprintingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.gbway.enavbatthermalprintingservice.config.RegistryAutoStarter;
import uz.gbway.enavbatthermalprintingservice.config.TrayIconApp;

@SpringBootApplication
public class ENavbatThermalPrintingServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ENavbatThermalPrintingServiceApplication.class, args);
//        TrayIconApp.showTrayIcon();
//        RegistryAutoStarter.enableAutoStart();
    }

}
