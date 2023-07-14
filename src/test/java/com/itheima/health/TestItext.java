package com.itheima.health;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TestItext {
    // 测试 Itext
    @Test
    public void test01() throws FileNotFoundException, DocumentException {
        // 创建文档对象
        Document document = new Document();

        // 创建PdfWriter对象
        PdfWriter.getInstance(document, new FileOutputStream("D:\\test.pdf"));

        document.open();
        // 中文不能写入
        document.add(new Paragraph("Hello IText 平头哥!!!"));

        document.close();
    }
}