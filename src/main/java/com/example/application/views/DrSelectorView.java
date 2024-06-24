package com.example.application.views;

import com.example.application.data.DakterisEntity;
import com.example.application.data.PacientsEntity;
import com.example.application.data.SharedData;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "dr/selector", layout = MainLayout.class)
@PageTitle("Select Dr | Colposcope app")
public class DrSelectorView extends VerticalLayout {

    Grid<DakterisEntity> grid = new Grid<>(DakterisEntity.class);
    TextField filterText = new TextField();
    private final Button btnSelectPatient = new Button("Select Dr");
    private final Button btnClose = new Button("Close");
    private final Button btnAdd = new Button("Add Dr");
    final private CrmService service;
    public DrSelectorView(SharedData sharedData, CrmService service) {
        this.service = service;
        configureGrid();
        add(getToolbar(), grid, btnClose);
        btnSelectPatient.addClickListener(e -> {
            var selected = grid.asSingleSelect().getValue();
            if(selected == null) {
                Notification.show("Please select a Dr", 3000, Notification.Position.MIDDLE);
                return;
            }
            sharedData.setSelectedDoctor(selected);
            UI.getCurrent().navigate(DashboardView.class);
        });
        btnClose.addClickListener(e -> {
            grid.deselectAll();
            sharedData.setSelectedDoctor(null);
            UI.getCurrent().navigate(DashboardView.class);
        });
        grid.addItemDoubleClickListener(e -> {
            var selected = grid.asSingleSelect().getValue();
            if(selected == null) {
                Notification.show("Please select a Dr.", 3000, Notification.Position.MIDDLE);
                return;
            }
            sharedData.setSelectedDoctor(selected);
            UI.getCurrent().navigate(DashboardView.class);
        });
    }

    private void configureGrid() {
        grid.addClassNames("dr-grid");
        grid.setSizeFull();
        grid.setItems(service.findAllDrs(null));
        grid.setAllRowsVisible(true);
        grid.setSizeUndefined();
        //grid.setWidth("60%");
        grid.setColumns("vardsUzvardsDakteris");
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
        grid.setItems(service.findAllDrs(filterText.getValue()));
    }



}