package com.example.application.views;

import com.example.application.data.SharedData;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

@PermitAll
@Route(value = "pdfViewer", layout = MainLayout.class)
@PageTitle("Visit | Colposcope app")
public class PdfViewerView extends VerticalLayout {
    String SAMPLE_PDF_PATH = "C:/Users/polar/workspace/vaadin/colpoapp-vaadin/HelloWorld.pdf"; // replace with your file path

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfViewerView.class);
    SharedData sharedData;

    public PdfViewerView(SharedData sharedData) { // <2>
         this.sharedData = sharedData;
        //this.service = service;
        addClassName("pdf-preview-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // <3>
        H2 formTitle =
                new H2("Kolposkopijas atskaite (pdf)");

/**
 * PdfViewer component. see docs:
 * https://vaadin.com/directory/component/pdf-viewer
 */

        PdfViewer pdfViewer = new PdfViewer();
        var filename = getFilenameFromAbsolutePath(sharedData.getPdfReportFilename());
        LOGGER.info(String.format("PdfViewerView - creating pdf preview from file: %s", filename));

        StreamResource   resource = new StreamResource(filename, () -> {
                try {
                    return new FileInputStream(sharedData.getPdfReportFilename());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            pdfViewer.setSrc(resource);
            pdfViewer.setHeight("600px");
        add(formTitle, pdfViewer);
    }

    static void showPdfViewerDialog(SharedData sharedData){
        Dialog dialog = new Dialog();
        PdfViewerView pdfViewerView = new PdfViewerView(sharedData);
        Button closeButton = new Button("Close", event_ -> dialog.close());
        dialog.add(closeButton, pdfViewerView);
        dialog.setSizeFull();
        dialog.open();
    }
String getFilenameFromAbsolutePath(String absolutePath){
    return Paths.get(absolutePath).getFileName().toString();
}

}
