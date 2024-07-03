package com.example.application.views;

import com.example.application.data.PacientsEntity;
import com.example.application.data.PatientsRepository;
import com.example.application.data.SharedData;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

public class PatientFormView extends FormLayout {
    private SharedData sharedData;
    private CrmService service;
    private  PatientsRepository repository;
    TextField vardsUzvardsPacients = new TextField("Vārds Uzvārds");
    TextField personasKods = new TextField("Personas kods");
    TextField dzimsanasGads = new TextField("Dzimšanas gads");
    Button save = new Button("Save/Saglabāt");
    Button add = new Button("Add/Pievienot");
    Button close = new Button("Cancel");
    private final Binder<PacientsEntity> binder = new BeanValidationBinder<>(PacientsEntity.class);
    private PatientFormListener listener;
    //
    public PatientFormView(SharedData sharedData, CrmService service) {
        this.service = service;
        this.sharedData = sharedData;

        addClassName("contact-form");

        binder.bindInstanceFields(this);

        add(vardsUzvardsPacients,
                personasKods,
                dzimsanasGads,
                createButtonsLayout());
    }
      private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

          add.addClickListener(event -> updateEntity(true));
        save.addClickListener(event -> updateEntity(false));

        return new HorizontalLayout(save, add, close);
      }
    private void updateEntity(Boolean insert) {
        PacientsEntity entity = new PacientsEntity();
        binder.writeBeanIfValid(entity);
        if(insert){
            entity.setId(null);
        }

        if (binder.isValid()) {
            service.savePatient(entity);
            Notification.show("Saved", 3000, Notification.Position.MIDDLE);
            if (this.listener != null) {
                this.listener.onPatientFormSaved();
            }
        } else {
            Notification.show("Form contains errors, please correct them",3000, Notification.Position.MIDDLE);
        }
    }
    public interface PatientFormListener {
        void onPatientFormSaved();
    }
    public void updateFormFields(PacientsEntity entity) {
        binder.setBean(entity);
    }
    public void setPatientFormListener(PatientFormListener listener) {
        this.listener = listener;
    }
}
