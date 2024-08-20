package com.example.application.views;

import com.example.application.data.PacientsEntity;
import com.example.application.data.SharedData;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;
@PermitAll
@Route(value = "patient/selector", layout = MainLayout.class)
@PageTitle("Select Patient | Colposcope app")
public class PatientSelectorView extends HorizontalLayout implements PatientFormView.PatientFormListener{

    Grid<PacientsEntity> grid = new Grid<>(PacientsEntity.class);
    TextField filterText = new TextField();
    private final Button btnClose = new Button("Close");

    final private CrmService service;
    final private SharedData sharedData;
    private Dialog dialog = null;

    private Runnable onPatientSelectedCallback;

    PatientFormView patientFormView;
    public PatientSelectorView(SharedData sharedData, CrmService service) {
        this.service = service;
        this.sharedData = sharedData;
        configureGrid();
        configureForm();

        VerticalLayout gridPart = new VerticalLayout();
        gridPart.add(getToolbar(), grid);

        add(gridPart, patientFormView, btnClose);

        patientFormView.setPatientFormListener(this);
    }
    public void setOnPatientSelectedCallback(Runnable callback) {
        this.onPatientSelectedCallback = callback;
    }
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setItems(service.findAllPatients(null));
        grid.setAllRowsVisible(true);
        grid.setWidth("60%");
        grid.setColumns("vardsUzvardsPacients", "personasKods");
        grid.asSingleSelect().addValueChangeListener(event -> {
            PacientsEntity selectedPatient = event.getValue();
            patientFormView.updateFormFields(selectedPatient);
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
                    if (onPatientSelectedCallback != null) {
                        onPatientSelectedCallback.run();
                    }
                Optional.ofNullable(dialog).ifPresent(Dialog::close);
            });
            return button;
        }).setHeader("Action");
    }

    void configureForm() {
        patientFormView = new PatientFormView(sharedData, service);
        patientFormView.setWidth("25em");
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
        grid.setItems(service.findAllPatients(filterText.getValue()));
    }


    @Override
    public void onPatientFormSaved() {
        refreshGrid();
    }
}