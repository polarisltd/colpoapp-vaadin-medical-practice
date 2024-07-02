package com.example.application.views;

import com.example.application.data.SharedData;
import com.example.application.pdf.PdfHelloWorld;
import com.example.application.services.CrmService;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@PermitAll
@Route(value = "pdfViewer", layout = MainLayout.class)
@PageTitle("Visit | Colposcope app")
public class PdfViewerView extends VerticalLayout {
    String SAMPLE_PDF_PATH = "C:/Users/polar/workspace/vaadin/flow-crm-tutorial/HelloWorld.pdf"; // replace with your file path
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfViewerView.class);

    public PdfViewerView() { // <2>
        //this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // <3>
        LOGGER.info(String.format("PdfViewerView - creating pdf preview from file: %s", SAMPLE_PDF_PATH));
        H2 formTitle =
                new H2("Kolposkopijas atskaite (pdf)");

/**
 * PdfViewer component. see docs:
 * https://vaadin.com/directory/component/pdf-viewer
 */

        PdfViewer pdfViewer = new PdfViewer();
         StreamResource   resource = new StreamResource("visit-report.pdf", () -> {
                try {
                    return new FileInputStream(SAMPLE_PDF_PATH);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            pdfViewer.setSrc(resource);
            pdfViewer.setHeight("600px");
        add(formTitle, pdfViewer);
    }




}
