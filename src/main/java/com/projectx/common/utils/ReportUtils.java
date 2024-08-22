package com.projectx.common.utils;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class ReportUtils {

    public static byte[] generatePdf(String data) {
        try {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(data,target);
            return target.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
