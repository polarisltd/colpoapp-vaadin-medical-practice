package com.example.application.pdf;

import com.example.application.data.ImageRepository;
import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.KolposkopijaIzmeklejumsRepository;
import com.example.application.data.SharedData;
import com.example.application.system.StaticTexts;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class PdfVisitReport
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfVisitReport.class);

    SharedData sharedData;
    KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository;
    ImageRepository imageRepository;
    final public String PDF_FILE_NAME = "HelloWorld.pdf";
    final static String FONT_FILE_PATH = "/fonts/arial.ttf";
    final static String FONT_BOLD_FILE_PATH = "/fonts/arialbd.ttf";
    final static BaseFont baseFont = getBaseFont(FONT_FILE_PATH);
    final static BaseFont baseBoldFont = getBaseFont(FONT_BOLD_FILE_PATH);
    KolposkopijaIzmeklejumsEntity entity;
    public PdfVisitReport(
            SharedData sharedData,
            KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository,
            ImageRepository imageRepository) {
                this.sharedData = sharedData;
                this.kolposkopijaIzmeklejumsRepository = kolposkopijaIzmeklejumsRepository;
                this.imageRepository = imageRepository;
    }



    private static BaseFont getBaseFont(String fontPath) {
        try {
            InputStream fontStream = PdfVisitReport.class.getResourceAsStream(fontPath);
            if (fontStream == null) {
                throw new IllegalArgumentException("Font file not found: " + fontPath);
            }
            File tempFile = File.createTempFile("font", ".ttf");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(fontStream, out);
            }
            LOGGER.info("Loading font file: " + tempFile.getAbsolutePath()+" exists: "+tempFile.exists());
            return BaseFont.createFont(tempFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load font file: " + fontPath, e);
        }
    }
    private static BaseFont getBoldBaseFont() {
        try {
            return BaseFont.createFont(FONT_BOLD_FILE_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            LOGGER.error("Error while loading font", e);
            return null;
        }
    }

    public void generate(){
        if (sharedData.getSelectedVisitId() == null) {
            LOGGER.error("No visit selected");
            return;
        }
        entity =
                kolposkopijaIzmeklejumsRepository.findById(sharedData.getSelectedVisitId()).orElse(null);
        if (entity == null){
                LOGGER.error("kolposkopijaIzmeklejums is null entity id: %s".formatted(sharedData.getSelectedVisitId()));
                return;
        }

        Document document = new Document(PageSize.A4, 0, 0, 0, 0);


        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(sharedData.getPdfReportFilename()));
            document.open();



            PdfPTable table01 = createTable01();
            document.add(table01);


            // Create root table with 2 columns
            PdfPTable rootTable = new PdfPTable(2);
            rootTable.setWidthPercentage(100f);
            rootTable.setWidths(new float[]{0.7f, 0.3f});

            // First column: Add existing tables
            PdfPTable leftColumnTable = new PdfPTable(1);
            leftColumnTable.setWidthPercentage(100f);

            PdfPTable table02 = createTable02();
            leftColumnTable.addCell(createTableCell(table02));

            PdfPTable table03 = createTable03();
            leftColumnTable.addCell(createTableCell(table03));

            // Add left column table to root table
            rootTable.addCell(createTableCell(leftColumnTable));
            //document.add(table02);

// Second column: Add images
            PdfPTable rightColumnTable = new PdfPTable(1);
            rightColumnTable.setWidthPercentage(100f);
            visitImages(rightColumnTable);

            // Add right column table to root table
            rootTable.addCell(createTableCell(rightColumnTable));

            document.add(rootTable);


//            PdfPTable table04 = new PdfPTable(1);
//            table04.setWidthPercentage(90f);
//            table03.setSpacingAfter(10f);
//            visitImages(entity, table04);
//            document.add(table04);

            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error while creating PDF", e);
        }
    }


    private PdfPTable getRezultatiTable(KolposkopijaIzmeklejumsEntity entity){
        PdfPTable table = new PdfPTable(1);

        if(notNullAndTrue(entity.getR1())) {
            table.addCell(getTextCellDefaultNoBorder(StaticTexts.R1 ));
        }
        if(notNullAndTrue(entity.getR2())) {
            table.addCell(getTextCellDefaultNoBorder(StaticTexts.R2 ));
        }
        if(notNullAndTrue(entity.getR3())) {
            table.addCell(getTextCellDefaultNoBorder(StaticTexts.R3 ));
        }
        if(notNullAndTrue(entity.getR4())) {
            table.addCell(getTextCellDefaultNoBorder(StaticTexts.R4 ));
        }
        if(notNullAndTrue(entity.getR5())) {
            table.addCell(getTextCellDefaultNoBorder(StaticTexts.R5 ));
        }
        if(notNullAndTrue(entity.getR6())) {
            table.addCell(getTextCellDefaultNoBorder(StaticTexts.R6 ));
        }
        return table;
    }
    public static boolean notNullAndTrue(Boolean value) {
        return value != null && value;
    }

    String getTwoOptions(Boolean value, String trueValue, String falseValue) {
        if (value == null) return "";
        return value ? trueValue : falseValue;
    }
    public static PdfPCell getTextCell(String text, int alignment,int border) {
        return getTextCell(text, alignment,border, FontFactory.getFont(FontFactory.HELVETICA,10));
    }
    public static PdfPCell getTextCellDefaultNoBorder(String text){
        return getTextCell(text, Element.ALIGN_LEFT,Rectangle.NO_BORDER,new Font(baseFont, 10));
    }
    public static PdfPCell getTextCellDefaultBoldNoBorder(String text){
        return getTextCell(text, Element.ALIGN_LEFT,Rectangle.NO_BORDER,new Font(baseBoldFont, 10));
    }
    public static PdfPCell getTextCellDefaultBoldRightAlignNoBorder(String text){
        return getTextCell(text, Element.ALIGN_RIGHT,Rectangle.NO_BORDER,new Font(baseBoldFont, 10));
    }
    public static PdfPCell getTextCellDefaultBoxed(String text){
        return getTextCell(text, Element.ALIGN_LEFT,Rectangle.BOX,new Font(baseFont, 10));
    }

    public static PdfPCell getTextCell(String text, int alignment,int border, Font font) {
        FontSelector fs = new FontSelector();
        //Font font = FontFactory.getFont(fontSpec, 10);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text!=null?text:"");
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (alignment);
        cell.setPadding (5.0f);
        cell.setBorder(border);
        return cell;
    }

    public static PdfPCell getTextCell(String text) {
        return getTextCell(text, Element.ALIGN_LEFT, Rectangle.BOX);
    }

    static Phrase getTextPhrase(String text, float size) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, size);
        fs.addFont(font);
        return fs.process(text);
    }

    void visitImages(PdfPTable imageTable) { // PatientVisitView.SAMPLE_IMAGE_PATH

        this.imageRepository.findByVisitIdAndIncluded(entity.getId().intValue()).forEach(image -> {
                try {
                    LOGGER.info("Adding image to PDF: " + image.getImagePath());
                    Image image1 = Image.getInstance(image.getImagePath());
                    image1.setAlignment(Element.ALIGN_CENTER);
                    image1.scaleAbsoluteWidth(400);
                    imageTable.addCell(image1);
                } catch (Exception e) {
                    LOGGER.error("Error while loading image", e);
                }
            });
        //imageTable.setSpacingAfter(10f);
        //imageTable.addCell(getTextCell(""));

        }

    PdfPTable createTable02() throws DocumentException {
        PdfPTable table02 = new PdfPTable(2);
        //table02.setWidthPercentage(90f);
        //table02.setWidths(new float[]{0.25f, 0.75f});
        table02.setSpacingAfter(10f);
        var thisCell = table02.addCell(
                getTextCellDefaultBoxed("KOLPOSKOPIJA " + getTwoOptions(entity.getKolposkopijaAdekvata(), "Adekvāta", "neadekvāta")));
        thisCell.setColspan(2);

        String transformacijasZonasTips =
                entity.getTrnsformacijasZonasTips() == 0 ? "n/a"
                        : entity.getTrnsformacijasZonasTips() == 1 ? "I"
                        : entity.getTrnsformacijasZonasTips() == 2 ? "II"
                        : "III";
        thisCell = table02.addCell(
                getTextCellDefaultBoxed("Transformācijas zonas tips (1-3) " + transformacijasZonasTips)
        );
        thisCell.setColspan(2);

        {
            var c = getTextCellDefaultBoxed("Pazīme ");
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table02.addCell(c);
        }
        {
            var c = getTextCell("Punkti ");
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table02.addCell(c);
        }
        table02.addCell(
                getTextCellDefaultBoxed(StaticTexts.P1_LABEL)
        );
        table02.addCell(
                getTextCellDefaultBoxed(
                        entity.getP1() == null ? "n/a" :
                                switch (entity.getP1()) {
                                    case 0 -> StaticTexts.P1_0P;
                                    case 1 -> StaticTexts.P1_1P;
                                    default -> StaticTexts.P1_2P;
                                })
        );

        table02.addCell(
                getTextCellDefaultBoxed(StaticTexts.P2_LABEL)
        );
        table02.addCell(
                getTextCellDefaultBoxed(
                        entity.getP2() == null ? "n/a" :
                                switch (entity.getP2()) {
                                    case 0 -> StaticTexts.P2_0P;
                                    case 1 -> StaticTexts.P2_1P;
                                    default -> StaticTexts.P2_2P;
                                })
        );

        table02.addCell(
                getTextCellDefaultBoxed(StaticTexts.P3_LABEL)
        );
        table02.addCell(
                getTextCellDefaultBoxed(
                        entity.getP3() == null ? "n/a" :
                                switch (entity.getP3()) {
                                    case 0 -> StaticTexts.P3_0P;
                                    case 1 -> StaticTexts.P3_1P;
                                    default -> StaticTexts.P3_2P;
                                })
        );

        table02.addCell(
                getTextCell(StaticTexts.P4_LABEL, Element.ALIGN_LEFT, Rectangle.BOX, new Font(baseFont, 10))
        );
        table02.addCell(
                getTextCellDefaultBoxed(
                        entity.getP4() == null ? "n/a" :
                                switch (entity.getP4()) {
                                    case 0 -> StaticTexts.P4_0P;
                                    case 1 -> StaticTexts.P4_1P;
                                    default -> StaticTexts.P4_2P;
                                })
        );

        table02.addCell(
                getTextCell(StaticTexts.P5_LABEL, Element.ALIGN_LEFT, Rectangle.BOX, new Font(baseFont, 10))
        );
        table02.addCell(
                getTextCellDefaultBoxed(
                        entity.getP5() == null ? "n/a" :
                                switch (entity.getP5()) {
                                    case 0 -> StaticTexts.P5_0P;
                                    case 1 -> StaticTexts.P5_1P;
                                    default -> StaticTexts.P5_2P;
                                })
        );
        int totalPoints =
                ((entity.getP1() != null) ? entity.getP1() : 0)
                        + ((entity.getP2() != null) ? entity.getP2() : 0)
                        + ((entity.getP3() != null) ? entity.getP3() : 0)
                        + ((entity.getP4() != null) ? entity.getP4() : 0)
                        + ((entity.getP5() != null) ? entity.getP5() : 0);
        {
            var c = getTextCellDefaultBoxed(String.format("%s (%s)", StaticTexts.P_TOTAL_POINTS_LABEL, totalPoints));
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table02.addCell(c);
        }
        {
            var c = getTextCellDefaultBoxed(
                    (switch (totalPoints) {
                        case 0, 1, 2, 3, 4 -> StaticTexts.P_TOTAL_POINTS_0_4;
                        case 5, 6 -> StaticTexts.P_TOTAL_POINTS_5_6;
                        case 7, 8, 9, 10 -> StaticTexts.P_TOTAL_POINTS_7_10;
                        default -> "n/a";
                    }));
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table02.addCell(c);
        }

        return table02;

    }

    PdfPTable createTable03() throws DocumentException {
        PdfPTable table03 = new PdfPTable(2);
        //table03.setWidthPercentage(90f);
        table03.setWidths(new float[]{0.25f, 0.75f});
        table03.setSpacingAfter(10f);

        {
            PdfPCell cell = new PdfPCell(getTextCell(StaticTexts.M_LABEL, Element.ALIGN_LEFT, Rectangle.NO_BORDER, new Font(baseFont, 10)));
            cell.setRowspan(2);
            table03.addCell(cell);
        }
        {
            PdfPCell cell = new PdfPCell(getTextCell(
                    StaticTexts.M1 + " " + (entity.getM1() == null ? "n/a"
                            : entity.getM1() ? "Jā"
                            : "Nē"
                    ), Element.ALIGN_LEFT, Rectangle.NO_BORDER, new Font(baseFont, 10)));
            table03.addCell(cell);
        }
        {
            PdfPCell cell = new PdfPCell(getTextCell(
                    StaticTexts.M2 + " " + (entity.getM2() == null ? "n/a"
                            : entity.getM2() ? "Jā"
                            : "Nē"
                    ), Element.ALIGN_LEFT, Rectangle.NO_BORDER, new Font(baseFont, 10)));
            table03.addCell(cell);
        }

        table03.addCell(
                getTextCellDefaultNoBorder("Rezultāti")
        );
        table03.addCell(
                getRezultatiTable(entity)
        );
        table03.addCell(
                getTextCellDefaultNoBorder("Slēdziens")
        );
        table03.addCell(
                getTextCellDefaultNoBorder(entity.getSledziens())
        );
        return table03;
    }

    PdfPTable createTable01() throws DocumentException{

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var formattedDate = Optional.ofNullable(entity.getIzmeklejumaDatums())
                .map(date -> date.atZone(ZoneId.systemDefault()).format(formatter))
                .orElse("N/A");

        PdfPTable table01 = new PdfPTable(3);
        table01.setWidthPercentage(100f);
        table01.setWidths(new float[]{0.25f, 0.25f, 0.5f});
        table01.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        //
        table01.addCell(
                getTextCellDefaultBoldNoBorder("Vārds Uzvārds ")
        );
        table01.addCell(
                getTextCellDefaultBoldNoBorder(entity.getPacients().getVardsUzvardsPacients())
        );
        table01.addCell(
                getTextCellDefaultBoldRightAlignNoBorder("DATUMS " + formattedDate)
        );
        //
        table01.addCell(
                getTextCellDefaultNoBorder("Personas Kods ")
        );

        {
            var cell = getTextCellDefaultNoBorder(entity.getPacients().getPersonasKods());
            cell.setColspan(2);
            table01.addCell(
                    cell
            );
        }

        //
        table01.addCell(
                getTextCellDefaultNoBorder("KOLPOSKOPIJA"));
        table01.addCell(
                getTextCellDefaultNoBorder((entity.getVizitesAtkartojums() != null
                        ? (entity.getVizitesAtkartojums()
                        ? "Atkārtota" : "Pirmreizēja")
                        : "N/A")));
        table01.addCell(getTextCellDefaultNoBorder(""));
        //
        table01.addCell(
                getTextCell("ALERĢIJAS: ", Element.ALIGN_LEFT, Rectangle.NO_BORDER, new Font(baseFont, 10))
        );
        table01.addCell(
                getTextCellDefaultNoBorder((entity.getAlergijas() ? "IR" : "NAV"))
        );
        table01.addCell(getTextCellDefaultNoBorder(entity.getAlergijasComment() != null ? entity.getAlergijasComment() : ""));
        //
        table01.addCell(
                getTextCellDefaultNoBorder("pēdējā mēnešreize: " + (entity.getPmPedejaMenesreize() == null ? "" : entity.getPmPedejaMenesreize())
                ));
        table01.addCell(
                getTextCellDefaultNoBorder("dzemdību skaits: " + (entity.getDzemdibuSkaits() == null ? "" : entity.getDzemdibuSkaits())
                ));
        table01.addCell(
                getTextCellDefaultNoBorder("Dzemdību veids: " + (entity.getPedejaGrutniecibaGads() == null ? "" : entity.getPedejaGrutniecibaGads())
                ));
//
        table01.addCell(
                getTextCellDefaultNoBorder("Kontracepcija: " + (entity.getKontracepcija() ? "Jā" : "Nē"))
        );
        table01.addCell(
                getTextCellDefaultNoBorder(entity.getKontracepcijaComment())
        );
        table01.addCell(
                getTextCellDefaultNoBorder("")
        );
//
        table01.addCell(
                getTextCellDefaultNoBorder("Smēķē: " + (entity.getSmeke() ? "Jā" : "Nē"))
        );
        table01.addCell(
                getTextCellDefaultNoBorder(entity.getSmekeComment())
        );
        table01.addCell(
                getTextCellDefaultNoBorder("")
        );
//
        table01.addCell(
                getTextCellDefaultNoBorder("Pēdējā citoloģiskā uztriepe (datums, rezultāts) :")
        );
        {
            var cell = getTextCellDefaultNoBorder(entity.getPedejaCitologiskaUztriepe());
            cell.setColspan(2);
            table01.addCell(cell);
        }
        //
        table01.addCell(
                getTextCellDefaultNoBorder("Hroniskas saslimšanas, medikamentu terāpija :")
        );
        {
            var cell = getTextCellDefaultNoBorder(entity.getHroniskasSaslimsanasMedikamentuLietosana());
            cell.setColspan(2);
            table01.addCell(cell);
        }

        table01.addCell(
                getTextCellDefaultNoBorder("Iepriekšēja terapija")
        );
        {
            var cell = getTextCellDefaultNoBorder(entity.getIepriekshVeiktaTerapija());
            cell.setColspan(2);
            table01.addCell(cell);

        }
        table01.addCell(
                getTextCellDefaultNoBorder("Anamnēze")
        );
        {
            var cell = getTextCellDefaultNoBorder(entity.getAnamneze());
            cell.setColspan(2);
            table01.addCell(cell);
        }
        //

        table01.setSpacingAfter(20f);
        return table01;
    }



    private PdfPCell createTableCell(PdfPTable table) {
        PdfPCell cell = new PdfPCell(table);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}



