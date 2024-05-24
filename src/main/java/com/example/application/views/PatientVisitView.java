package com.example.application.views;

import com.example.application.data.Contact;
import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.enumeration.ViziteAtkartojumsEnum;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


import java.time.*;

@PermitAll
@Route(value = "addVisit", layout = MainLayout.class)
@PageTitle("Visit | Vaadin CRM")
public class PatientVisitView extends FormLayout {
    TextField izmeklejumaNr = new TextField("Izmeklējuma Nr");
    DatePicker izmeklejumaDatums = new DatePicker("Izmeklējuma Datums");
    ComboBox<ViziteAtkartojumsEnum> vizitesAtkartojums = new ComboBox<>("Vizites Atkartojums");
    TextField skriningaNr = new TextField("Skrīninga Nr");
    TextField anamneze = new TextField("Anamnēze");
    TextField iepriekshVeiktaTerapija = new TextField("Iepriekš Veikta Terapija");
    Checkbox alergijas = new Checkbox("Alergijas");
    TextField trnsformacijasZonasTips = new TextField("Trnsformacijas Zonas Tips");
    Checkbox p1 = new Checkbox("P1");
    Checkbox p2 = new Checkbox("P2");
    Checkbox p3 = new Checkbox("P3");
    Checkbox p4 = new Checkbox("P4");
    Checkbox p5 = new Checkbox("P5");
    Checkbox m1 = new Checkbox("M1");
    Checkbox m2 = new Checkbox("M2");
    Checkbox m3 = new Checkbox("M3");
    TextField rezultati = new TextField("Rezultati");
    TextField sledziens = new TextField("Sledziens");
    TextField nakosaKolposkopijasKontrole = new TextField("Nakosa Kolposkopijas Kontrole");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<KolposkopijaIzmeklejumsEntity> binder = new BeanValidationBinder<>(KolposkopijaIzmeklejumsEntity.class);

    CrmService service;

    public PatientVisitView(CrmService service) {
        this.service = service;
        addClassName("patient-visit-view");
        binder.forField(izmeklejumaDatums)
                .withConverter(
                        new Converter<LocalDate, Instant>() {
                            public Result<Instant> convertToModel(LocalDate fieldValue, ValueContext context) {
                                return Result.ok(fieldValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            }

                            public LocalDate convertToPresentation(Instant modelValue, ValueContext context) {
                                return modelValue.atZone(ZoneId.systemDefault()).toLocalDate();
                            }
                        })
                .bind(KolposkopijaIzmeklejumsEntity::getIzmeklejumaDatums, KolposkopijaIzmeklejumsEntity::setIzmeklejumaDatums);
        binder.bindInstanceFields(this);

        add(izmeklejumaNr, izmeklejumaDatums, vizitesAtkartojums, skriningaNr, anamneze, iepriekshVeiktaTerapija, alergijas, trnsformacijasZonasTips, p1, p2, p3, p4, p5, m1, m2, m3, rezultati, sledziens, nakosaKolposkopijasKontrole, createButtonsLayout());
        KolposkopijaIzmeklejumsEntity visit = new KolposkopijaIzmeklejumsEntity();
        LocalDate currentDate = LocalDate.now();
        Instant currentInstant = currentDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        visit.setIzmeklejumaDatums(currentInstant);
        this.setVisit(visit);
        this.addSaveListener(this::saveVisit); // <1>
        this.addCloseListener(e -> closeEditor());
    }

    private void saveVisit(SaveEvent event) {
        service.saveVisit(event.getEntity());
        closeEditor();
    }
    private void closeEditor() {
        this.setVisit(null);
        this.setVisible(false);
        removeClassName("patient-visit-view");
    }
    public void setVisit(KolposkopijaIzmeklejumsEntity entity) {
        binder.setBean(entity);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());

        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }

    public static abstract class PatientVisitFormEvent extends ComponentEvent<PatientVisitView> {
        private final KolposkopijaIzmeklejumsEntity entity;

        protected PatientVisitFormEvent(PatientVisitView source, KolposkopijaIzmeklejumsEntity entity) {
            super(source, false);
            this.entity = entity;
        }

        public KolposkopijaIzmeklejumsEntity getEntity() {
            return entity;
        }
    }

    public static class SaveEvent extends PatientVisitFormEvent {
        SaveEvent(PatientVisitView source, KolposkopijaIzmeklejumsEntity entity) {
            super(source, entity);
        }
    }


    public static class CloseEvent extends PatientVisitFormEvent {
        CloseEvent(PatientVisitView source) {
            super(source, null);
        }
    }
    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }
    public void addCloseListener(ComponentEventListener<CloseEvent> listener) {
        addListener(CloseEvent.class, listener);
    }
}