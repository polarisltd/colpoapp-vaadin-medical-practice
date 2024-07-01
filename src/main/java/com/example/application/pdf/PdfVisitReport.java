package com.example.application.pdf;

import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.KolposkopijaIzmeklejumsRepository;
import com.example.application.data.SharedData;
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

@Component
public class PdfVisitReport
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfVisitReport.class);

    SharedData sharedData;
    KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository;
    final public String PDF_FILE_NAME = "HelloWorld.pdf";
    public PdfVisitReport(
            SharedData sharedData,
            KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository) {
                this.sharedData = sharedData;
                this.kolposkopijaIzmeklejumsRepository = kolposkopijaIzmeklejumsRepository;

    }

    public void generate(String filename){
        if (sharedData.getSelectedVisitId() == null) {
            LOGGER.error("No visit selected");
            return;
        }
        KolposkopijaIzmeklejumsEntity entity =
                kolposkopijaIzmeklejumsRepository.findById(sharedData.getSelectedVisitId()).orElse(null);

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();


            PdfPTable table01 = new PdfPTable(2);
            table01.setWidths(new float[]{0.25f, 0.75f});
            table01.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table01.addCell(
                    getTextCell("KOLPOSKOPIJA "+ (entity.getVizitesAtkartojums()?"Atkārtota":"Pirmreizēja"), Element.ALIGN_LEFT,Rectangle.NO_BORDER));
            table01.addCell(
                    getTextCell("DATUMS "+ entity.getIzmeklejumaDatums().toString(), Element.ALIGN_RIGHT,Rectangle.NO_BORDER)
            );
            table01.addCell(
                    getTextCell("VARDS UZVĀRDS ", Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.addCell(
                    getTextCell(entity.getPacients().getVardsUzvardsPacients(), Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.addCell(
                    getTextCell("ALERĢIJAS ", Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.addCell(
                    getTextCell((entity.getAlergijas()?"IR":"NAV"), Element.ALIGN_LEFT,Rectangle.NO_BORDER)
            );
            table01.setSpacingAfter(50f);


            document.add(table01);

            PdfPTable table02 = new PdfPTable(2);
            table02.setWidths(new float[]{0.25f, 0.75f});
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
                    getTextCell("Pazīme ", Element.ALIGN_LEFT,Rectangle.BOX)
            );
            table02.addCell(
                    getTextCell("Punkti ", Element.ALIGN_LEFT,Rectangle.BOX)
            );
            table02.addCell(
                    getTextCell(StaticTexts.P1_LABEL, Element.ALIGN_LEFT,Rectangle.BOX)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP1() == null ? "n/a" :
                                    switch (entity.getP1()) {
                                        case 0 -> StaticTexts.P1_0P;
                                        case 1 -> StaticTexts.P1_1P;
                                        default -> StaticTexts.P1_2P;
                                    }, Element.ALIGN_LEFT,Rectangle.BOX)
            );

            table02.addCell(
                    getTextCell(StaticTexts.P2_LABEL, Element.ALIGN_LEFT,Rectangle.BOX)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP2() == null ? "n/a" :
                            switch (entity.getP2()) {
                                case 0 -> StaticTexts.P2_0P;
                                case 1 -> StaticTexts.P2_1P;
                                default -> StaticTexts.P2_2P;
                            }, Element.ALIGN_LEFT,Rectangle.BOX)
            );

            table02.addCell(
                    getTextCell(StaticTexts.P3_LABEL, Element.ALIGN_LEFT,Rectangle.BOX)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP3() == null ? "n/a" :
                            switch (entity.getP3()) {
                                case 0 -> StaticTexts.P3_0P;
                                case 1 -> StaticTexts.P3_1P;
                                default -> StaticTexts.P3_2P;
                            }, Element.ALIGN_LEFT,Rectangle.BOX)
            );

            table02.addCell(
                    getTextCell(StaticTexts.P4_LABEL, Element.ALIGN_LEFT,Rectangle.BOX)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP4() == null ? "n/a" :
                            switch (entity.getP4()) {
                                case 0 -> StaticTexts.P4_0P;
                                case 1 -> StaticTexts.P4_1P;
                                default -> StaticTexts.P4_2P;
                            }, Element.ALIGN_LEFT,Rectangle.BOX)
            );

            table02.addCell(
                    getTextCell(StaticTexts.P5_LABEL, Element.ALIGN_LEFT,Rectangle.BOX)
            );
            table02.addCell(
                    getTextCell(
                            entity.getP5() == null ? "n/a" :
                            switch (entity.getP5()) {
                                case 0 -> StaticTexts.P5_0P;
                                case 1 -> StaticTexts.P5_1P;
                                default -> StaticTexts.P5_2P;
                            }, Element.ALIGN_LEFT,Rectangle.BOX)
            );

            
            document.add(table02);
            

            document.add(getTextPhrase(StaticTexts.M_LABEL,16));
            
            List listOfM = new List(List.UNORDERED);
          
            listOfM.add(new ListItem(
                    getTextPhrase(StaticTexts.M1+" "+(entity.getM1() == null ? "n/a"
                            : entity.getM1() ? "Jā"
                            : "Nē"),16)
            ));
            listOfM.add(new ListItem(
                    getTextPhrase(StaticTexts.M2+" "+(entity.getM2() == null ? "n/a"
                            : entity.getM1() ? "Jā"
                            : "Nē"),16)
            ));

            document.add(listOfM);

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
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (alignment);
        cell.setPadding (5.0f);
        cell.setBorder(border);
        return cell;
    }

    static Phrase getTextPhrase(String text, float size) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, size);
        fs.addFont(font);
        return fs.process(text);
    }

    /*
void doit() {
            PdfPTable irdTable = new PdfPTable(2);

            irdTable.addCell(getIRDCell("Invoice No"));
            //System.out.println("Enter Invoice No : ");
            //=sc.nextLine();
            irdTable.addCell(getIRDCell("Invoice Date"));
            //System.out.println("Enter Invoice Date : ");
            //=sc.nextLine();
            irdTable.addCell(getIRDCell(invoiveno)); // pass invoice number
            irdTable.addCell(getIRDCell(invoivedate)); // pass invoice date

            PdfPTable irhTable = new PdfPTable(3);
            irhTable.setWidthPercentage(100);

            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_CENTER));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            PdfPCell invoiceTable = new PdfPCell (irdTable);
            invoiceTable.setBorder(0);
            irhTable.addCell(invoiceTable);

            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            fs.addFont(font);
            Phrase bill = fs.process("Bill To"); // customer information
            //System.out.println("Enter Customer Name : ");
            //=sc.nextLine();
            Paragraph name = new Paragraph(name2);
            name.setIndentationLeft(20);
            //System.out.println("Enter Customer Mobile : ");
            //=sc.nextLine();
            Paragraph contact = new Paragraph(mob);
            contact.setIndentationLeft(20);
            //System.out.println("Enter Customer Address : ");

            Paragraph address = new Paragraph(add);
            address.setIndentationLeft(20);
            int n;
            //System.out.println("Enter No. of Quantities : ");
            //n=Integer.parseInt(sc.nextLine());




            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell("Warranty"));
            validity.addCell(getValidityCell(" * Products purchased comes with 1 year national warranty \n   (if applicable)"));
            validity.addCell(getValidityCell(" * Warranty should be claimed only from the respective manufactures"));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setColspan (3);
            summaryL.setPadding (1.0f);


            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("Subtotal"));
            accounts.addCell(getAccountsCellR("12620.00"));
            accounts.addCell(getAccountsCell("Discount (10%)"));
            accounts.addCell(getAccountsCellR("1262.00"));
            accounts.addCell(getAccountsCell("Tax(2.5%)"));
            accounts.addCell(getAccountsCellR("315.55"));
            accounts.addCell(getAccountsCell("Total"));
            accounts.addCell(getAccountsCellR("11673.55"));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setColspan (3);


            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell(" "));
            describer.addCell(getdescCell("Goods once sold will not be taken back or exchanged || Subject to product justification || Product damage no one responsible || "
                    + " Service only at concarned authorized service centers"));


    }
*/

    


    public static void setHeader() {

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

//    public static PdfPCell getValidityCell(String text) {
//        FontSelector fs = new FontSelector();
//        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
//        font.setColor(BaseColor.GRAY);
//        fs.addFont(font);
//        Phrase phrase = fs.process(text);
//        PdfPCell cell = new PdfPCell (phrase);
//        cell.setBorder(0);
//        return cell;
//    }

//    public static PdfPCell getAccountsCell(String text) {
//        FontSelector fs = new FontSelector();
//        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
//        fs.addFont(font);
//        Phrase phrase = fs.process(text);
//        PdfPCell cell = new PdfPCell (phrase);
//        cell.setBorderWidthRight(0);
//        cell.setBorderWidthTop(0);
//        cell.setPadding (5.0f);
//        return cell;
//    }
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


//    static Element getBillTable(PdfPCell summaryL,PdfPCell summaryR) {
//        PdfPTable billTable = new PdfPTable(3);
//        billTable.setWidthPercentage(100);
//        billTable.addCell(getBillHeaderCell("Description"));
//        billTable.addCell(getBillHeaderCell("Quantity"));
//        billTable.addCell(getBillHeaderCell("Amount"));
//        billTable.addCell(getBillRowCell("Samsung Galaxy J7"));
//        billTable.addCell(getBillRowCell("1"));
//        billTable.addCell(getBillRowCell("12620.00"));
//        billTable.addCell(getBillRowCell("Samsung Galaxy J7"));
//        billTable.addCell(getBillRowCell("1"));
//        billTable.addCell(getBillRowCell("12620.00"));
//        billTable.addCell(getBillRowCell("Samsung Galaxy J7"));
//        billTable.addCell(getBillRowCell("1"));
//        billTable.addCell(getBillRowCell("12620.00"));
//        billTable.addCell(summaryL);
//        billTable.addCell(summaryR);
//        return billTable;
//    }

    static Element image() {
        try {
            Image image1 = Image.getInstance(PatientVisitView.SAMPLE_IMAGE_PATH);
            image1.setAlignment(Element.ALIGN_CENTER);
            image1.scaleAbsolute(450, 250);
            return image1;
        }catch (Exception e) {
            LOGGER.error("Error while loading image", e);
            return null;
        }
    }

}

