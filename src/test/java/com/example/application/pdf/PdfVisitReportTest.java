package com.example.application.pdf;

import com.example.application.data.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PdfVisitReportTest {

    @TempDir
    Path tempDir;

    @Test
    public void testGeneratePdfWithMissingImage() throws Exception {
        SharedData sharedData = new SharedData();
        Long visitId = 1L;
        sharedData.setSelectedVisitId(visitId);
        String pdfFile = tempDir.resolve("test_report.pdf").toString();
        sharedData.setPdfReportFilename(pdfFile);

        KolposkopijaIzmeklejumsRepository visitRepo = Mockito.mock(KolposkopijaIzmeklejumsRepository.class);
        ImageRepository imageRepo = Mockito.mock(ImageRepository.class);

        PacientsEntity patient = PacientsEntity.builder()
                .vardsUzvardsPacients("Jānis Bērziņš")
                .personasKods("123456-12345")
                .dzimsanasGads(1980)
                .build();

        KolposkopijaIzmeklejumsEntity visit = KolposkopijaIzmeklejumsEntity.builder()
                .id(visitId)
                .pacients(patient)
                .izmeklejumaDatums(java.time.LocalDate.now())
                .alergijas(false)
                .kontracepcija(false)
                .smeke(false)
                .trnsformacijasZonasTips(0)
                .m1(false)
                .m2(false)
                .build();

        when(visitRepo.findById(visitId)).thenReturn(Optional.of(visit));

        String nonExistentImagePath = "non_existent_image.jpg";
        ImageEntity imageEntity = ImageEntity.builder()
                .id(1)
                .visitId(visitId.intValue())
                .imagePath(nonExistentImagePath)
                .imageIncluded(true)
                .build();

        when(imageRepo.findByVisitIdAndIncluded(visitId.intValue())).thenReturn(Collections.singletonList(imageEntity));

        PdfVisitReport pdfVisitReport = new PdfVisitReport(sharedData, visitRepo, imageRepo);
        pdfVisitReport.generate();

        File file = new File(pdfFile);
        assertTrue(file.exists(), "PDF file should be generated");

        // Check if PDF contains the path of the missing image
        PdfReader reader = new PdfReader(pdfFile);
        boolean found = false;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            String text = PdfTextExtractor.getTextFromPage(reader, i);
            System.out.println("[DEBUG_LOG] Page " + i + " text: " + text);
            if (text.contains(nonExistentImagePath)) {
                found = true;
                break;
            }
        }
        reader.close();

        assertTrue(found, "PDF should contain the missing image path: " + nonExistentImagePath);
    }
}
