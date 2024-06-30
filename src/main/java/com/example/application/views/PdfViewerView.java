package com.example.application.views;

import com.example.application.data.SharedData;
import com.example.application.pdf.PdfHelloWorld;
import com.example.application.services.CrmService;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.vaadin.flow.component.UI;

@PermitAll
@Route(value = "pdfViewer", layout = MainLayout.class)
@PageTitle("Visit | Colposcope app")
public class PdfViewerView extends VerticalLayout {
    private final CrmService service;
    String SAMPLE_PDF_PATH = "C:/Users/polar/workspace/vaadin/flow-crm-tutorial/HelloWorld.pdf"; // replace with your file path
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfViewerView.class);
    private StreamRegistration pdfStream;
    public PdfViewerView(CrmService service, SharedData sharedData) { // <2>
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // <3>
        LOGGER.info(String.format("PdfViewerView - creating pdf preview from file: %s", SAMPLE_PDF_PATH));
        H2 formTitle =
                new H2("PdfViewer demo");

/**
 * PdfViewer component. see docs:
 * https://vaadin.com/directory/component/pdf-viewer
 */



        PdfViewer pdfViewer = new PdfViewer();
         StreamResource   resource = new StreamResource("file.pdf", () -> {
                try {
                    return new FileInputStream(SAMPLE_PDF_PATH);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            pdfViewer.setSrc(resource);
            pdfViewer.setHeight("600px");

        var printBtn = new Button("Print pdf", e -> print());

        add(printBtn, formTitle, pdfViewer);
    }

    /**
     * Printing pdf file is based on following resource:
     * https://stackoverflow.com/questions/74433011/how-to-print-pdf-in-vaadin-flow
     * @param attachEvent
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
                pdfStream = VaadinSession.getCurrent().getResourceRegistry().registerResource(new StreamResource("colposcope-protocol.pdf", () -> {
            try {
                return new FileInputStream(SAMPLE_PDF_PATH);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        pdfStream.unregister();
        super.onDetach(detachEvent);
    }



    private void print() {
        final String uri = pdfStream.getResourceUri().toString();
        System.out.println("PRINTING 2: " + uri);
        UI.getCurrent().getPage().executeJs("// create iframe element\n" +
                "const iframe = document.createElement('iframe');\n" +
                "\n" +
                "// create object URL for your blob or file and set it as the iframe's src\n" +
                "iframe.src = $0;\n" +
                "iframe.name = 'pdf';\n" +
                "\n" +
                "// call the print method in the iframe onload handler\n" +
                "iframe.onload = () => {\n" +
                "    const pdfFrame = window.frames[\"pdf\"];\n" +
                "    pdfFrame.focus();\n" +
                "    pdfFrame.print();\n" +
                "}\n" +
                "iframe.hidden = true;document.body.appendChild(iframe);", uri);

    }
}


