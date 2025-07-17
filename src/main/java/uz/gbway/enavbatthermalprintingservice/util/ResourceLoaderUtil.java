package uz.gbway.enavbatthermalprintingservice.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ResourceLoaderUtil {
    public BufferedImage loadPlayMarketDownlaodImage() {
        ClassPathResource imgFile = new ClassPathResource("static/printres/google-play40.png");

        try (InputStream is = imgFile.getInputStream()) {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
