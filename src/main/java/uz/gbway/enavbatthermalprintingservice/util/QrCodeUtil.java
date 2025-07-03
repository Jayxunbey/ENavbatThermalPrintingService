package uz.gbway.enavbatthermalprintingservice.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static java.awt.SystemColor.text;

@Component
public class QrCodeUtil {
    public BufferedImage generate(String qrNumber, int width, int height) {
        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;

        try {

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 0); // <-- Quiet zone o‘chirildi
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // optional

            bitMatrix = qrWriter.encode(qrNumber, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }


        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        return image;

    }

}
