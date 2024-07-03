package com.example.application.pdf;

import com.example.application.data.*;
import com.example.application.system.StaticTexts;
import com.example.application.views.PatientVisitView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

@Component
public class PdfVisitReport
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfVisitReport.class);

    SharedData sharedData;
    KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository;
    ImageRepository imageRepository;
    final public String PDF_FILE_NAME = "HelloWorld.pdf";
    public PdfVisitReport(
            SharedData sharedData,
            KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository,
            ImageRepository imageRepository) {
                this.sharedData = sharedData;
                this.kolposkopijaIzmeklejumsRepository = kolposkopijaIzmeklejumsRepository;
                this.imageRepository = imageRepository;
    }

    public void generate(){
        if (sharedData.getSelectedVisitId() == null) {
            LOGGER.error("No visit selected");
            return;
        }
        KolposkopijaIzmeklejumsEntity entity =
                kolposkopijaIzmeklejumsRepository.findById(sharedData.getSelectedVisitId()).orElse(null);

        Document document = new Document(PageSize.A4, 0, 0, 0, 0);


        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(sharedData.getPdfReportFilename()));
            document.open();


            PdfPTable table01 = new PdfPTable(3);
            table01.setWidthPercentage(90f);
            table01.setWidths(new float[]{0.25f, 0.25f, 0.5f});
            table01.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table01.addCell(
                    getTextCell("KOLPOSKOPIJA", Element.ALIGN_LEFT,Rectangle.NO_BORDER));
            table01.addCell(
                    getTextCell((entity.getVizitesAtkartojums()?"Atkārtota":"Pirmreizēja"), Element.ALIGN_LEFT,Rectangle.NO_BORDER));
            table01.addCell(
                    getTextCell("DATUMS "+ entity.getIzmeklejumaDatums().toString(), Element.ALIGN_RIGHT,Rectangle.NO_BORDER)
            );

            table01.addCell(
                    getTextCell("VARDS UZVĀRDS ", Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.addCell(
                    getTextCell(entity.getPacients().getVardsUzvardsPacients(), Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.addCell(getTextCell("", Element.ALIGN_LEFT,Rectangle.NO_BORDER));
            table01.addCell(
                    getTextCell("ALERĢIJAS ", Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.addCell(
                    getTextCell((entity.getAlergijas()?"IR":"NAV"), Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.addCell(getTextCell("", Element.ALIGN_LEFT,Rectangle.NO_BORDER));
            table01.setSpacingAfter(50f);


            document.add(table01);

            PdfPTable table02 = new PdfPTable(2);
            table02.setWidthPercentage(90f);
            table02.setWidths(new float[]{0.25f, 0.75f});
            table02.setSpacingAfter(10f);
            var thisCell = table02.addCell(
                    getTextCell("KOLPOSKOPIJA "+getTwoOptions(entity.getKolposkopijaAdekvata(), "Adekvāta", "neadekvāta"), Element.ALIGN_LEFT,Rectangle.BOX));
            thisCell.setColspan(2);

            String transformacijasZonasTips = entity.getTrnsformacijasZonasTips() == null ? "n/a"
                    : entity.getTrnsformacijasZonasTips() == 1 ? "I"
                    : entity.getTrnsformacijasZonasTips() == 2 ? "II"
                    : "III";
            thisCell = table02.addCell(
                    getTextCell("Transformācijas zonas tips "+ transformacijasZonasTips, Element.ALIGN_RIGHT,Rectangle.BOX)
            );
            thisCell.setColspan(2);
            table02.addCell(
                    getTextCell("Pazīme ")
            );
            table02.addCell(
                    getTextCell("Punkti ")
            );
            table02.addCell(
                    getTextCell(StaticTexts.P1_LABEL)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP1() == null ? "n/a" :
                                    switch (entity.getP1()) {
                                        case 0 -> StaticTexts.P1_0P;
                                        case 1 -> StaticTexts.P1_1P;
                                        default -> StaticTexts.P1_2P;
                                    })
            );

            table02.addCell(
                    getTextCell(StaticTexts.P2_LABEL)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP2() == null ? "n/a" :
                            switch (entity.getP2()) {
                                case 0 -> StaticTexts.P2_0P;
                                case 1 -> StaticTexts.P2_1P;
                                default -> StaticTexts.P2_2P;
                            })
            );

            table02.addCell(
                    getTextCell(StaticTexts.P3_LABEL)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP3() == null ? "n/a" :
                            switch (entity.getP3()) {
                                case 0 -> StaticTexts.P3_0P;
                                case 1 -> StaticTexts.P3_1P;
                                default -> StaticTexts.P3_2P;
                            })
            );

            table02.addCell(
                    getTextCell(StaticTexts.P4_LABEL)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP4() == null ? "n/a" :
                            switch (entity.getP4()) {
                                case 0 -> StaticTexts.P4_0P;
                                case 1 -> StaticTexts.P4_1P;
                                default -> StaticTexts.P4_2P;
                            })
            );

            table02.addCell(
                    getTextCell(StaticTexts.P5_LABEL)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP5() == null ? "n/a" :
                            switch (entity.getP5()) {
                                case 0 -> StaticTexts.P5_0P;
                                case 1 -> StaticTexts.P5_1P;
                                default -> StaticTexts.P5_2P;
                            })
            );
            int totalPoints =
                    entity.getP1()
                            +entity.getP2()
                            +entity.getP3()
                            +entity.getP4()
                            +entity.getP5();
            {
                var c = getTextCell(String.format("%s (%s)", StaticTexts.P_TOTAL_POINTS_LABEL, totalPoints), Element.ALIGN_LEFT, Rectangle.BOX);
                c.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table02.addCell(c);
            }
            {
                var c = getTextCell(
                        (switch (totalPoints) {
                            case 0, 1, 2, 3, 4 -> StaticTexts.P_TOTAL_POINTS_0_4;
                            case 5, 6 -> StaticTexts.P_TOTAL_POINTS_5_6;
                            case 7, 8, 9, 10 -> StaticTexts.P_TOTAL_POINTS_7_10;
                            default -> "n/a";
                        }), Element.ALIGN_LEFT, Rectangle.BOX);
                c.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table02.addCell(c);
            }
            document.add(table02);


            PdfPTable table03 = new PdfPTable(2);
            table03.setWidthPercentage(90f);
            table03.setWidths(new float[]{0.25f, 0.75f});
            table03.setSpacingAfter(10f);

            {
                PdfPCell cell = new PdfPCell(getTextCell(StaticTexts.M_LABEL));
                cell.setRowspan(2);
                table03.addCell(cell);
            }
            {
                PdfPCell cell = new PdfPCell(getTextCell(
                        StaticTexts.M1+" "+(entity.getM1() == null ? "n/a"
                            : entity.getM1() ? "Jā"
                            : "Nē"
                )));
                table03.addCell(cell);
            }
            {
                PdfPCell cell = new PdfPCell(getTextCell(
                        StaticTexts.M2+" "+(entity.getM2() == null ? "n/a"
                            : entity.getM2() ? "Jā"
                            : "Nē"
                )));
                table03.addCell(cell);
            }

//            List listOfM = new List(List.UNORDERED);
//
//            listOfM.add(new ListItem(
//                    getTextPhrase(StaticTexts.M1+" "+(entity.getM1() == null ? "n/a"
//                            : entity.getM1() ? "Jā"
//                            : "Nē"),16)
//            ));
//            listOfM.add(new ListItem(
//                    getTextPhrase(StaticTexts.M2+" "+(entity.getM2() == null ? "n/a"
//                            : entity.getM1() ? "Jā"
//                            : "Nē"),16)
//            ));
//            {
//                PdfPCell c = new PdfPCell();
//                cell.addElement(listOfM);
//                table03.addCell(c);
//            }


            table03.addCell(
                    getTextCell("Iepriekšēja terapija")
            );
            table03.addCell(
                    getTextCell(entity.getIepriekshVeiktaTerapija())
            );
            table03.addCell(
                    getTextCell("Anamnēze")
            );
            table03.addCell(
                    getTextCell(entity.getAnamneze())
            );

            table03.addCell(
                    getTextCell("Rezultāti")
            );
            table03.addCell(
                    getTextCell(entity.getRezultati())
            );
            table03.addCell(
                    getTextCell("Slēdziens")
            );
            table03.addCell(
                    getTextCell(entity.getSledziens())
            );

            document.add(table03);

            PdfPTable table04 = new PdfPTable(2);
            table04.setWidthPercentage(90f);
            table03.setSpacingAfter(10f);
            visitImages(entity, table04);
            document.add(table04);

            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error while creating PDF", e);
        }
    }

    String getTwoOptions(Boolean value, String trueValue, String falseValue) {
        if (value == null) return "";
        return value ? trueValue : falseValue;
    }
    public static PdfPCell getTextCell(String text, int alignment,int border) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
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

    public static PdfPCell getIRHCell(String text, int alignment) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        /*	font.setColor(BaseColor.GRAY);*/
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell getIRDCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        return cell;
    }

    public static PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getBillFooterCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }


    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorder(0);
        return cell;
    }

    static Element getBill1() {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        fs.addFont(font);
        Phrase phrase = fs.process("Invoice");
        return phrase;
    }




    void visitImages(KolposkopijaIzmeklejumsEntity visitEntity, PdfPTable imageTable) { // PatientVisitView.SAMPLE_IMAGE_PATH

        this.imageRepository.findByVisitId(visitEntity.getId().intValue()).forEach(image -> {
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

        }
    }



