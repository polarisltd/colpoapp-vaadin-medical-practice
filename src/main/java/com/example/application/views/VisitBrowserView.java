package com.example.application.views;

import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.SharedData;
import com.example.application.pdf.PdfVisitReport;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.core.env.Environment;

import java.util.Map;

import static com.example.application.helpers.Helpers.asProperties;
import static com.example.application.system.StaticTexts.PDF_PATH_PROPERTY;
import static com.example.application.views.PatientVisitView.getPdfReportFilename;

@PermitAll
@Route(value = "visitBrowser", layout = MainLayout.class)
@PageTitle("Visit Browser | Colposcope app")
public class VisitBrowserView extends VerticalLayout{

    Grid<KolposkopijaIzmeklejumsEntity> grid = new Grid<>(KolposkopijaIzmeklejumsEntity.class);
    TextField filterText = new TextField();
    private final Button btnClose = new Button("Close");
    final private CrmService service;
    final private SharedData sharedData;
    final private PdfVisitReport pdfReport;
    Map<String,Object> appProperties;
    public VisitBrowserView(SharedData sharedData,
                            CrmService service,
                            Environment env,
                            PdfVisitReport pdfReport) {
        this.service = service;
        this.sharedData = sharedData;
        this.pdfReport = pdfReport;
        this.appProperties = asProperties(env);
        configureGrid();

        VerticalLayout gridPart = new VerticalLayout();
        gridPart.add(getToolbar(), grid);

        add(gridPart,btnClose);

    }


    private void configureGrid() {
        grid.addClassNames("visit-browser-grid");
        grid.setSizeFull();
        grid.setItems(service.findAllVisits(null));
        grid.setAllRowsVisible(true);
        grid.setWidth("60%");
        grid.removeAllColumns();
        grid.addColumn(KolposkopijaIzmeklejumsEntity::getIzmeklejumaNr).setHeader("Izmeklejuma Nr");
        grid.addColumn(KolposkopijaIzmeklejumsEntity::getIzmeklejumaDatums).setHeader("Izmeklejuma Datums");
        grid.addColumn(kolposkopija -> kolposkopija.getPacients().getVardsUzvardsPacients()).setHeader("Vards Uzvards Pacients");
        grid.addColumn(kolposkopija -> kolposkopija.getPacients().getPersonasKods()).setHeader("Personas Kods");



        grid.addComponentColumn(item -> {
            Button button = new Button("Select");
            button.addClickListener(click -> {
                System.out.println("select button on grid row clicked for item: " + item);
                sharedData.setSelectedVisitId(item.getId());
                String filePathPrefix = this.appProperties.get(PDF_PATH_PROPERTY).toString();
                sharedData.setPdfReportFilename(getPdfReportFilename(item, filePathPrefix));

                this.pdfReport.generate();
                // viewer

                Dialog dialog = new Dialog();
                PdfViewerView pdfViewerView = new PdfViewerView(sharedData);
                Button closeButton = new Button("Close", event_ -> dialog.close());
                dialog.add(closeButton, pdfViewerView);
                dialog.setSizeFull();
                dialog.open();
            });

            return button;
        }).setHeader("Action");
    }


    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> refreshGrid());
        var toolbar =  new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void refreshGrid() {
        grid.setItems(service.findAllVisits(filterText.getValue()));
    }

}