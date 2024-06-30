package com.example.application.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import com.example.application.views.PatientVisitView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PdfHelloWorld
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfHelloWorld.class);

    public static void createPdf(
            String invoiveno,
            String invoivedate,
            String name2,
            String add,
            String mob,
            String price,
                String name5,
                    String desc,
                    String quan,
                    String amount
    )
    {
        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("HelloWorld.pdf"));
            document.open();
            //Scanner sc=new Scanner(System.in);

         //*String name2=sc.nextLine();







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

            document.open();//PDF document opened........

            // add text paragraph
            document.add(new Paragraph(" Hello World PDF document."+name2));
            document.add(image());
            document.add(getBill1());
            document.add(irhTable);
            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(address);
            document.add(getBillTable(summaryL, summaryR));
            document.add(describer);
            document.close();
            writer.close();
        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

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

    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
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


    static Element getBillTable(PdfPCell summaryL,PdfPCell summaryR) {
        PdfPTable billTable = new PdfPTable(3);
        billTable.setWidthPercentage(100);
        billTable.addCell(getBillHeaderCell("Description"));
        billTable.addCell(getBillHeaderCell("Quantity"));
        billTable.addCell(getBillHeaderCell("Amount"));
        billTable.addCell(getBillRowCell("Samsung Galaxy J7"));
        billTable.addCell(getBillRowCell("1"));
        billTable.addCell(getBillRowCell("12620.00"));
        billTable.addCell(getBillRowCell("Samsung Galaxy J7"));
        billTable.addCell(getBillRowCell("1"));
        billTable.addCell(getBillRowCell("12620.00"));
        billTable.addCell(getBillRowCell("Samsung Galaxy J7"));
        billTable.addCell(getBillRowCell("1"));
        billTable.addCell(getBillRowCell("12620.00"));
        billTable.addCell(summaryL);
        billTable.addCell(summaryR);
        return billTable;
    }

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

