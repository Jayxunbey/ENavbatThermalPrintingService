package uz.gbway.enavbatthermalprintingservice.util;

import org.springframework.stereotype.Component;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintNewQueueReqDto;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

@Component
public class ShablonUtil {


    public void drawLine(Graphics2D grPage, int x, int y, int width, int height) {
        grPage.drawRect(x, y, width, height);
    }


    public void drawText(Graphics2D g2d, String text, String fontName, int fontSize, int x, int y, int pageWidth) {
        Font font = new Font(fontName, Font.CENTER_BASELINE, fontSize);
        g2d.setFont(font);

//        FontMetrics metrics = g2d.getFontMetrics(font);
//        int textWidth = metrics.stringWidth(text);

//        int x = Math.max((pageWidth - textWidth) / 2, 0);

        g2d.drawString(text, x, y);
    }

    public int drawCenteredAndLineBreakerText(Graphics2D g2d, String text,String fontName, int fontSize, int y, int pageWidth, float margin) {

        Font font = new Font(fontName, Font.PLAIN, fontSize);
        g2d.setFont(font);

        return writeAsLineBreaking(g2d, text, y, pageWidth, font,margin);


    }

    private int writeAsLineBreaking(Graphics2D g2d, String text, int y, int pageWidth, Font font, float margin) {



        FontRenderContext frc = g2d.getFontRenderContext();
        AttributedString attrStr = new AttributedString(text);
        attrStr.addAttribute(TextAttribute.FONT, font);

        AttributedCharacterIterator paragraph = attrStr.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float wrappingWidth = (float) (pageWidth - margin*2); // 15px margin each side

        while (lineMeasurer.getPosition() < paragraph.getEndIndex()) {
            TextLayout layout = lineMeasurer.nextLayout(wrappingWidth);
            y += layout.getAscent();
            float drawPosX = (float)(pageWidth - layout.getAdvance()) / 2+margin/2;
            layout.draw(g2d, drawPosX, y);
            y += layout.getDescent() + layout.getLeading();
        }

        return y;

    }

    private int getPageWidth(int paperWidthMM, double dpi) {
        int pageWidth = (int) (paperWidthMM * dpi / 25.4); // 80mm in pixels

        return pageWidth;

    }

    public void drawImage(Graphics2D g2d, Image image, int x, int y, int width, int pageWidth) {

        g2d.drawImage(image, x, y, width, width, null); // Centered

    }

    public void drawCenteredImage(Graphics2D g2d, Image image, int y, int pageWidth) {

        int imageWidth = image.getWidth(null);

        int x = Math.max((pageWidth - imageWidth) / 2, 0);

        g2d.drawImage(image, x, y, null); // Centered

    }



    public void drawCenteredText(Graphics2D g2d, String text,String fontName, int fontSize, int y, int pageWidth) {

        Font font = new Font(fontName, Font.CENTER_BASELINE, fontSize);
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);

        int x = Math.max((pageWidth - textWidth) / 2, 0);
        g2d.drawString(text, x, y);

    }



}
