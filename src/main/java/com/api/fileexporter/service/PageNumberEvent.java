package com.api.fileexporter.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

import java.awt.*;
import java.io.IOException;

public class PageNumberEvent extends PdfPageEventHelper {
    private Font font;

    public PageNumberEvent() {
        font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.BLACK);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Rectangle pageSize = document.getPageSize();
        cb.beginText();
        try {
            cb.setFontAndSize(BaseFont.createFont(), 10);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        float x = document.leftMargin();
        float y = document.bottomMargin() - 20;
        cb.setTextMatrix(x, y + 12);
        cb.showText("Texto inicial");
        cb.setTextMatrix(x, y); // Desce a posição do texto
        cb.showText("Mais texto");
        cb.setTextMatrix(pageSize.getWidth() - document.rightMargin() - 15, y);
        cb.showText("Page " + writer.getPageNumber());
        cb.endText();
    }
}
