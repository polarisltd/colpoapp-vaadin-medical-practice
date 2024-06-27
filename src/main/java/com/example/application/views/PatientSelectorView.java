package com.example.application.views;

import com.example.application.data.PacientsEntity;
import com.example.application.data.SharedData;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.notification.Notification;
import java.util.HashMap;
import java.util.Map;

import static com.example.application.views.PatientVisitView.DOCTOR_ID_ROUTE_PARAM;
import static com.example.application.views.PatientVisitView.PATIENT_ID_ROUTE_PARAM;
import java.util.Optional;
@PermitAll
@Route(value = "patient/selector", layout = MainLayout.class)
@PageTitle("Select Patient | Colposcope app")
public class PatientSelectorView extends VerticalLayout {

    Grid<PacientsEntity> grid = new Grid<>(PacientsEntity.class);
    TextField filterText = new TextField();
    private final Button btnSelectPatient = new Button("Select Patient");
    private final Button btnClose = new Button("Close");
    private final Button btnAdd = new Button("Add Patient");
    final private CrmService service;
    final private SharedData sharedData;
    private Dialog dialog = null;
    public PatientSelectorView(SharedData sharedData, CrmService service) {
        this.service = service;
        this.sharedData = sharedData;
        configureGrid();
        add(getToolbar(), grid, btnClose);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setItems(service.findAllPatients(null));
        grid.setAllRowsVisible(true);
        //grid.setSizeUndefined();
        grid.setWidth("60%");
        grid.setColumns("vardsUzvardsPacients", "personasKods");
        btnSelectPatient.addClickListener(e -> {
            selectPatient();
        });

        btnClose.addClickListener(e -> Optional.ofNullable(dialog).ifPresent(Dialog::close));

        grid.addItemDoubleClickListener(e -> {
            selectPatient();
        });

        grid.addComponentColumn(item -> {
            Button button = new Button("Select");
            button.addClickListener(click -> {
                System.out.println("Button clicked for item: " + item);
                sharedData.setSelectedPatient(item);
                Optional.ofNullable(dialog).ifPresent(Dialog::close);
            });
            return button;
        }).setHeader("Action");
    }

    void selectPatient(){
        var selected = grid.asSingleSelect().getValue();
        if(selected == null) {
            Notification.show("Please select a patient", 3000, Notification.Position.MIDDLE);
        }else {
            sharedData.setSelectedPatient(selected);
            Optional.ofNullable(dialog).ifPresent(Dialog::close);
        }
    }

    public void navigateToVisitEntryForm(Long drId, Long patientId) {
        Map<String, String> params = new HashMap<>();
        params.put(DOCTOR_ID_ROUTE_PARAM, drId.toString());
        params.put(PATIENT_ID_ROUTE_PARAM, patientId.toString());
        UI.getCurrent().navigate(PatientVisitView.class, new RouteParameters(params));
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        var toolbar =  new HorizontalLayout(filterText, btnAdd, btnSelectPatient);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(service.findAllPatients(filterText.getValue()));
    }



}