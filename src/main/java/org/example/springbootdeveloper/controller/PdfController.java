package org.example.springbootdeveloper.controller;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdeveloper.domain.Article;
import org.example.springbootdeveloper.repository.BlogRepository;
import org.example.springbootdeveloper.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Slf4j
@RequiredArgsConstructor
@Controller
public class PdfController {

    private final BlogService blogService;


    @GetMapping("/createPdf/{id}")
    public void createPdf(HttpServletResponse response, @PathVariable Long id) throws IOException {

        Article article = blogService.findById(id);

        // PDF 문서를 브라우저에서 바로 다운로드할 수 있도록 설정
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=members.pdf");

        // PDF 문서 생성
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document document = new Document(pdfDoc);


        // 한글 폰트 로드
        PdfFont font = PdfFontFactory.createFont("C:\\Users\\CHANO\\study\\Spring\\springboot-developer\\src\\main\\resources\\static\\font\\AppleSDGothicNeoR.ttf",
                PdfEncodings.IDENTITY_H, true);

        // 현재 날짜의 월을 가져와서 포맷팅
        LocalDate currentDate = LocalDate.now();
        String monthString = currentDate.format(DateTimeFormatter.ofPattern("MMMM"));
        Paragraph monthParagraph = new Paragraph(monthString + " 보고서")
                .setFont(font) // 한글 폰트 설정
                .setTextAlignment(TextAlignment.CENTER) // 가운데 정렬
                .setFontSize(22); // 글자 크기를 18로 설정
        document.add(monthParagraph);

        Paragraph titleParagraph =new Paragraph(article.getTitle())
                .setFont(font);
        document.add(titleParagraph);

        Paragraph contentParagraph =new Paragraph(article.getContent())
                .setFont(font);
        document.add(contentParagraph);

        // 문서 닫기
            document.close();
        }
    }