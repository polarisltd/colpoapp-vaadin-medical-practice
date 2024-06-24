package com.example.application.views;

import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.SharedData;
import com.example.application.data.enumeration.ViziteAtkartojumsEnum;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@PermitAll
@Route(value = "addVisit/:drId?([0-9]+)/:ptId?([0-9]+)", layout = MainLayout.class)

@PageTitle("Visit | Colposcope app")
public class PatientVisitView extends FormLayout implements BeforeEnterObserver  { //implements HasUrlParameter<String>
    TextField izmeklejumaNr = new TextField("Izmeklējuma Nr");
    DatePicker izmeklejumaDatums = new DatePicker("Izmeklējuma Datums");
    ComboBox<ViziteAtkartojumsEnum> vizitesAtkartojums = new ComboBox<>("Vizites Atkartojums");
    TextField skriningaNr = new TextField("Skrīninga Nr");
    TextArea anamneze = new TextArea("Anamnēze");
    TextField iepriekshVeiktaTerapija = new TextField("Iepriekš Veikta Terapija");
    Checkbox alergijas = new Checkbox("Alergijas");
    TextField trnsformacijasZonasTips = new TextField("Trnsformacijas Zonas Tips");
    Text p1label = new Text(getTranslation("P1.label"));
    Checkbox p1 = new Checkbox(getTranslation("P1"));
    Text p2label = new Text(getTranslation("P2.label"));
    Checkbox p2 = new Checkbox(getTranslation("P2"));
    Text p3label = new Text(getTranslation("P3.label"));
    Checkbox p3 = new Checkbox(getTranslation("P3"));
    Text p4label = new Text(getTranslation("P4.label"));
    Checkbox p4 = new Checkbox(getTranslation("P4"));
    Text p5label = new Text(getTranslation("P5.label"));
    Checkbox p5 = new Checkbox(getTranslation("P5"));
    Checkbox m1 = new Checkbox(getTranslation("M1"));
    Checkbox m2 = new Checkbox(getTranslation("M2"));
    Checkbox m3 = new Checkbox(getTranslation("M3"));
    TextArea rezultati = new TextArea("Rezultati");
    TextArea sledziens = new TextArea("Sledziens");
    TextField nakosaKolposkopijasKontrole = new TextField("Nakosa Kolposkopijas Kontrole");
    //VerticalLayout imagesLayout = new VerticalLayout();
    Grid<ImageItem> imageGrid = new Grid<>();
    List<ImageItem> images = setupImages();
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Button btnPatientSelector = new Button("Pacients");
    Binder<KolposkopijaIzmeklejumsEntity> binder = new BeanValidationBinder<>(KolposkopijaIzmeklejumsEntity.class);

    CrmService service;
    SharedData sharedData;
    public static final String DOCTOR_ID_ROUTE_PARAM = "drId";
    public static final String PATIENT_ID_ROUTE_PARAM = "ptId";
    Integer drId;
    Integer ptId;

    public PatientVisitView(CrmService service
            , SharedData sharedData
    ) {

        this.service = service;
        this.sharedData = sharedData;
        watchDirectory("c:/far");
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

        Div pParentDiv = new Div(
                new Div(bold(p1label), p1),
                new Div(bold(p2label), p2),
                new Div(bold(p3label), p3),
                new Div(bold(p4label), p4),
                new Div(bold(p5label), p5));
        pParentDiv.getStyle().set("border", "1px solid black");
        Div mParentDiv = new Div(
                bold(new Text("Manipulācijas vizītes laikā")),
                new Div(m1),
                new Div(m2),
                new Div(m3));
        mParentDiv.getStyle().set("border", "1px solid black");

        setupImages();
        imageGrid.addColumn(
                LitRenderer
                        .<ImageItem>of(
                                "<div><img style='height: 80px; width: 80px;' src=${item.imagedata} alt=${item.name}></div>"
                        )
                        .withProperty("imagedata", item -> getImageAsBase64(item.getImage()))
                        .withProperty("name", ImageItem::getId)
        );
        imageGrid.setItems(images);

        add(getFormTitle(), izmeklejumaNr, izmeklejumaDatums, vizitesAtkartojums, skriningaNr, anamneze, iepriekshVeiktaTerapija, alergijas, trnsformacijasZonasTips,
                pParentDiv,
                mParentDiv,
                rezultati, sledziens, nakosaKolposkopijasKontrole,
                imageGrid,
                createButtonsLayout());
        KolposkopijaIzmeklejumsEntity visit = new KolposkopijaIzmeklejumsEntity();
        LocalDate currentDate = LocalDate.now();
        Instant currentInstant = currentDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        visit.setIzmeklejumaDatums(currentInstant);
        this.setVisit(visit);
        this.addSaveListener(this::saveVisit); // <1>
        this.addCloseListener(e -> closeEditor());
    }

    List<ImageItem> setupImages(){
        ArrayList<ImageItem> images = new ArrayList<>();
        images.add(new ImageItem(loadImageFromFile("C:\\Users\\polar\\Downloads\\001\\pog009-3.jpg")));
        return images;
    }

    private void saveVisit(SaveEvent event) {
        var entity = event.getEntity();
        try {
            entity.setDakteris(sharedData.getSelectedDoctor());
            entity.setPacients(sharedData.getSelectedPatient());
            service.saveVisit(entity);
            closeEditor();
        }catch (Exception e) {
            Notification.show("error: %s".formatted(e.getMessage()), 3000, Notification.Position.MIDDLE);
        }

    }
    private void closeEditor() {
        this.setVisit(null);
        this.setVisible(false);
        removeClassName("patient-visit-view");
    }

    Component getFormTitle() {
        var dr = (sharedData.getSelectedDoctor()!=null)?
                "Dr. %s".formatted(sharedData.getSelectedDoctor().getVardsUzvardsDakteris()):
                "";
        var pt = (sharedData.getSelectedPatient()!=null)?
                "patient: %s".formatted(sharedData.getSelectedPatient().getVardsUzvardsPacients()):
                "";
        return new Div(new H2(dr), new H2(pt));
    }

    public void setVisit(KolposkopijaIzmeklejumsEntity entity) {
        binder.setBean(entity);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        //save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());

        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        btnPatientSelector.addClickListener(e -> UI.getCurrent().navigate(""));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(btnPatientSelector, save, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }

    @Getter
    public static abstract class PatientVisitFormEvent extends ComponentEvent<PatientVisitView> {
        private final KolposkopijaIzmeklejumsEntity entity;

        protected PatientVisitFormEvent(PatientVisitView source, KolposkopijaIzmeklejumsEntity entity) {
            super(source, false);
            this.entity = entity;
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

@Override
public void beforeEnter(BeforeEnterEvent event) {
    ptId = event.getRouteParameters().getInteger(DOCTOR_ID_ROUTE_PARAM).orElse(null);
    drId = event.getRouteParameters().getInteger(PATIENT_ID_ROUTE_PARAM).orElse(null);
}

    private Div bold(Text textComponent) {
        Div div = new Div(textComponent);
        div.getStyle().set("font-weight", "bold"); // Add your desired style here
        div.getStyle().set("color", "blue");
        return div;
    }


    private byte[] loadImageFromFile(String filePath) {
//        String filePath = "c:\\images\\pog009-3.jpg";
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Base64.getDecoder().decode(TRANSPARENT_GIF_1PX);
        }
    }

    private void watchDirectory(String directoryPath) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(directoryPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                                Path newPath = ((WatchEvent<Path>)event).context();
                                String newFilePath = Paths.get(directoryPath, newPath.toString()).toString();
                                var image = loadImageFromFile(newFilePath);
                                images.add(new ImageItem(image));
                                System.out.println("New file created: " + newFilePath);
                                imageGrid.setItems(images);
                            }
                        }
                        key.reset();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String TRANSPARENT_GIF_1PX =
            "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACwAAAAAAQABAAACAkQBADs=";
    private String getImageAsBase64(byte[] string) {
        String mimeType = "image/png";
        String htmlValue = null;
        if (string == null) htmlValue = TRANSPARENT_GIF_1PX; else htmlValue =
                "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(string);
        return htmlValue;
    }
    private class ImageItem {
        @Getter
        private byte[] image;
        private String id;

        public ImageItem(byte[] image) {
            this.image = image;
        }
        public String getId() {
            return String.valueOf(image.hashCode());
        }

    }

}