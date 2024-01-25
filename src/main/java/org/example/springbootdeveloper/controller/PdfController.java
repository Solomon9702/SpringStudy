package org.example.springbootdeveloper.controller;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
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

    private void drawLineChart(PdfDocument pdfDoc) {
        PdfPage currentPage = pdfDoc.getLastPage();
        PdfCanvas canvas = new PdfCanvas(currentPage);

        // 선 그래프 데이터 (예시)
        float[][] data = {
                {100, 100}, // 첫 번째 데이터 포인트 (x, y)
                {150, 200}, // 두 번째 데이터 포인트
                {200, 150}, // 세 번째 데이터 포인트
                {250, 180},
                {300, 210},
                {350, 322},
                {400, 112},
                {450, 102},
                {500, 51},
        };

        // 선 그래프 그리기
        canvas.setColor(ColorConstants.RED, false); // 선 색상 설정
        canvas.setLineWidth(2); // 선 두께 설정

        // 데이터 포인트를 선으로 연결
        for (int i = 0; i < data.length - 1; i++) {
            canvas.moveTo(data[i][0], data[i][1]);    // 현재 데이터 포인트로 이동
            canvas.lineTo(data[i+1][0], data[i+1][1]); // 다음 데이터 포인트로 선 그리기
        }

        canvas.stroke(); // 선 그리기
    }

    private void drawBarChart(PdfDocument pdfDoc, int xPlace, int yPlace){
        // 현재 페이지 얻기
        PdfPage currentPage = pdfDoc.getLastPage();
        PdfCanvas canvas = new PdfCanvas(currentPage);

        // 바 그래프 데이터
        int[] data = {15, 30, 50, 20, 50};
        Color[] colors = {ColorConstants.RED, ColorConstants.GREEN, ColorConstants.BLUE, ColorConstants.YELLOW, ColorConstants.ORANGE};

        // 바 그래프 그리기
        int x = xPlace; // 시작 x 좌표
        int y = yPlace; // 시작 y 좌표
        int width = 20; // 바의 너비
        int gap = 0; // 바 사이의 간격

        for (int i = 0; i < data.length; i++) {
            // 바 그리기
            canvas.setColor(colors[i], true); // 각 막대에 다른 색상 적용
            canvas.rectangle(x, y, width, data[i]);
            canvas.fill();

            // 다음 바를 위한 x 좌표 업데이트
            x += (width + gap + 10);

        }
    }

    private final BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

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

        // Article 제목 추가
        String title = article.getTitle(); // Article 객체에서 제목 가져오기
        Paragraph titleParagraph = new Paragraph("제목: " + title)
                .setFont(font)
                .setTextAlignment(TextAlignment.LEFT) // 왼쪽 정렬
                .setFontSize(16); // 글자 크기 설정
        document.add(titleParagraph);

        // 그래프 그리는 메소드 호출
        drawBarChart(pdfDoc,50, 500);
        drawBarChart(pdfDoc,200, 500);
        drawBarChart(pdfDoc,350, 500);
        drawBarChart(pdfDoc,500, 500);


        // 선그래프 그리는 메소드 호출
        drawLineChart(pdfDoc);

        // 문서 닫기
            document.close();
        }
    }