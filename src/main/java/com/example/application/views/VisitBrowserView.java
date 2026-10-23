package com.example.application.views;

import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.SharedData;
import com.example.application.pdf.PdfVisitReport;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.application.views.PatientVisitView.getPdfReportFilename;
import static com.example.application.views.PdfViewerView.showPdfViewerDialog;

@PermitAll
@Route(value = "visitBrowser", layout = MainLayout.class)
@PageTitle("Visit Browser | Colposcope app")
public class VisitBrowserView extends VerticalLayout implements BeforeEnterObserver {

    Grid<KolposkopijaIzmeklejumsEntity> grid = new Grid<>(KolposkopijaIzmeklejumsEntity.class);
    TextField filterText = new TextField();
    private final Button btnClose = new Button("Close");
    private final Button btnAddVisit = new Button("Add Visit Today");
    final private CrmService service;
    final private SharedData sharedData;
    final private PdfVisitReport pdfReport;
    private LocalDate filterDate;

    public VisitBrowserView(SharedData sharedData,
                            CrmService service,
                            PdfVisitReport pdfReport) {
        this.service = service;
        this.sharedData = sharedData;
        this.pdfReport = pdfReport;
        setSizeFull();
        configureGrid();

        VerticalLayout gridPart = new VerticalLayout();
        gridPart.setSizeFull();
        gridPart.add(getToolbar(), grid);

        add(gridPart, new HorizontalLayout(btnAddVisit, btnClose));
        expand(gridPart);

        btnAddVisit.addClickListener(e -> {
            createNewVisit();
        });

        btnClose.addClickListener(e -> UI.getCurrent().navigate(DashboardView.class));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> dateParam = event.getLocation().getQueryParameters().getSingleParameter("date");
        if (dateParam.isPresent()) {
            filterDate = LocalDate.parse(dateParam.get());
            btnAddVisit.setText("Add Visit for " + filterDate);
        } else {
            filterDate = null;
            btnAddVisit.setText("Add Visit Today");
        }
        refreshGrid();
    }

    private void createNewVisit() {
        Dialog dialog = new Dialog();
        PatientSelectorView patientSelectorView = new PatientSelectorView(sharedData, service);
        patientSelectorView.setDialog(dialog);
        patientSelectorView.setOnPatientSelectedCallback(() -> {
            KolposkopijaIzmeklejumsEntity newVisit = new KolposkopijaIzmeklejumsEntity();
            newVisit.setPacients(sharedData.getSelectedPatient());
            newVisit.setDakteris(sharedData.getSelectedDoctor());
            newVisit.setIzmeklejumaDatums(filterDate != null ? filterDate : LocalDate.now());
            
            // Save immediately to have an ID, or just navigate?
            // User said "prefill several patient visit forms", so better save it.
            KolposkopijaIzmeklejumsEntity saved = service.saveVisit(newVisit);
            sharedData.setSelectedVisitId(saved.getId());

            UI.getCurrent().navigate(PatientVisitView.class);
            // We need to tell PatientVisitView to load this visit.
            // Since PatientVisitView currently uses SharedData but also hardcodes a new visit in constructor,
            // we should probably update it to look for a selected visit ID.
            refreshGrid();
        });
        dialog.add(patientSelectorView);
        dialog.open();
    }


    private void configureGrid() {
        grid.addClassNames("visit-browser-grid");
        grid.setSizeFull();
        grid.setItems(service.findAllVisits(null));
        grid.setAllRowsVisible(false);
        grid.setWidth("100%");
        grid.removeAllColumns();
        grid.addColumn(KolposkopijaIzmeklejumsEntity::getIzmeklejumaNr).setHeader("Izmeklejuma Nr").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(KolposkopijaIzmeklejumsEntity::getIzmeklejumaDatums).setHeader("Izmeklejuma Datums").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(kolposkopija -> kolposkopija.getPacients().getVardsUzvardsPacients()).setHeader("Vards Uzvards Pacients").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(kolposkopija -> kolposkopija.getPacients().getPersonasKods()).setHeader("Personas Kods").setAutoWidth(true).setFlexGrow(0);



        grid.addComponentColumn(item -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(click -> {
                sharedData.setSelectedVisitId(item.getId());
                UI.getCurrent().navigate(PatientVisitView.class);
            });
            return editButton;
        }).setHeader("Edit").setAutoWidth(true).setFlexGrow(0);

        grid.addComponentColumn(item -> {
            Button printButton = new Button("Print");
            printButton.addClickListener(click -> {
                sharedData.setSelectedVisitId(item.getId());
                sharedData.setPdfReportFilename(getPdfReportFilename(item, this.sharedData.getPdfPath()));
                this.pdfReport.generate();
                showPdfViewerDialog(sharedData);
            });
            return printButton;
        }).setHeader("Print").setAutoWidth(true).setFlexGrow(0);
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
        if (filterDate != null) {
            grid.setItems(service.findVisitsByDate(filterDate));
        } else {
            grid.setItems(service.findAllVisits(filterText.getValue()));
        }
    }

}