package com.example.application.views;

import com.example.application.data.ImageEntity;
import com.example.application.data.ImageRepository;
import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.SharedData;
import com.example.application.data.enumeration.ViziteAtkartojumsEnum;
import com.example.application.services.CrmService;
import com.example.application.system.AppProperties;
import com.example.application.system.ResourceBundleControl;
import com.example.application.system.StaticTexts;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.ResourceBundle;

@PermitAll
@Route(value = "addVisit/:drId?([0-9]+)/:ptId?([0-9]+)", layout = MainLayout.class)
@PageTitle("Visit | Colposcope app")
public class PatientVisitView extends FormLayout implements BeforeEnterObserver { //implements HasUrlParameter<String>
    TextField izmeklejumaNr = new TextField("Izmeklējuma Nr");
    DatePicker izmeklejumaDatums = new DatePicker("Izmeklējuma Datums");
    ComboBox<ViziteAtkartojumsEnum> vizitesAtkartojums = new ComboBox<>("Vizites Atkartojums");
    TextField skriningaNr = new TextField("Skrīninga Nr");
    TextArea anamneze = new TextArea("Anamnēze");
    TextField iepriekshVeiktaTerapija = new TextField("Iepriekš Veikta Terapija");
    Checkbox alergijas = new Checkbox("Alergijas");
    TextField trnsformacijasZonasTips = new TextField("Trnsformacijas Zonas Tips");
    Text p1label = new Text(StaticTexts.P1label);
    Checkbox p1 = new Checkbox(StaticTexts.P1);
    Text p2label = new Text(StaticTexts.P2label);
    Checkbox p2 = new Checkbox(StaticTexts.P2);
    Text p3label = new Text(StaticTexts.P3label);
    Checkbox p3 = new Checkbox(StaticTexts.P3);
    Text p4label = new Text(StaticTexts.P4label);
    Checkbox p4 = new Checkbox(StaticTexts.P4);
    Text p5label = new Text(StaticTexts.P5label);
    Checkbox p5 = new Checkbox(StaticTexts.P5);
    Checkbox m1 = new Checkbox(StaticTexts.M1);
    Checkbox m2 = new Checkbox(StaticTexts.M2);
    Checkbox m3 = new Checkbox(StaticTexts.M3);
    TextArea rezultati = new TextArea("Rezultāti");
    TextArea sledziens = new TextArea("Slēdziens");
    TextField nakosaKolposkopijasKontrole = new TextField("Nākošā Kolposkopijas Kontrole");
    //VerticalLayout imagesLayout = new VerticalLayout();
    Button save = new Button("Save (F9)");
    Button close = new Button("Cancel");
    Button btnPatientSelector = new Button("Pacients");

    VerticalLayout imagesLayout = new VerticalLayout();
    Binder<KolposkopijaIzmeklejumsEntity> binder = new BeanValidationBinder<>(KolposkopijaIzmeklejumsEntity.class);

    CrmService service;
    SharedData sharedData;
    public static final String DOCTOR_ID_ROUTE_PARAM = "drId";
    public static final String PATIENT_ID_ROUTE_PARAM = "ptId";
    Integer drId;
    Integer ptId;

    ResourceBundle bundle;

    Long currentVisitId = null;

    final String SAMPLE_IMAGE_PATH = "C:/far/images/colposcopy-logo.jpg";

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private Environment env;

    @Value("${watcherpath}")
    private String watcherSercvicePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientVisitView.class);

    public PatientVisitView(CrmService service
            , SharedData sharedData
    ) {

        this.service = service;
        this.sharedData = sharedData;

        watcherSercvicePath = AppProperties.watcherSercvicePath;
        if(watcherSercvicePath == null){
            watcherSercvicePath = "c:/far/images";
        }

        watchDirectory(watcherSercvicePath);
        addClassName("patient-visit-view");
        binder.forField(izmeklejumaDatums)
                .withConverter(
                        new Converter<LocalDate, Instant>() {
                            public Result<Instant> convertToModel(LocalDate fieldValue, ValueContext context) {

                                return Result.ok((fieldValue == null ? LocalDate.now() : fieldValue).atStartOfDay(ZoneId.systemDefault()).toInstant());
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

        sampleImage();  // add sample to imagesLayout

        add(getFormTitle(), izmeklejumaNr, izmeklejumaDatums, vizitesAtkartojums, skriningaNr, anamneze,
                iepriekshVeiktaTerapija, alergijas, trnsformacijasZonasTips,
                pParentDiv,
                mParentDiv,
                rezultati, sledziens, nakosaKolposkopijasKontrole,
                imagesLayout,
                addRefreshButton(),
                createButtonsLayout());
        KolposkopijaIzmeklejumsEntity visit = new KolposkopijaIzmeklejumsEntity();
        LocalDate currentDate = LocalDate.now();
        Instant currentInstant = currentDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        visit.setIzmeklejumaDatums(currentInstant);
        this.setVisit(visit);
        this.addSaveListener(this::saveVisit); // <1>
        this.addCloseListener(e -> closeEditor());
    }

    String getTranslation1(String key) {
        if (this.bundle == null) {
            this.bundle = ResourceBundle.getBundle("messages/messages_en", new ResourceBundleControl());
        }
        return bundle.getString(key);
    }

    Component addRefreshButton() {

        //layout.removeAll();
        Button refreshButton = new Button("Refresh Images (F8)");
        refreshButton.addClickShortcut(Key.F8);

        refreshButton.addClickListener(e -> {
            refreshImagesLayout();
        });
        return refreshButton;
    }



    void refreshImagesLayout() {
        // Clear the layout
        LOGGER.info("Refreshing images for visit id: %s saved!".formatted(this.currentVisitId));

        this.imagesLayout.removeAll();

        sampleImage();

        // Fetch images from the database
        List<ImageEntity> imageEntities = imageRepository.findByVisitId((this.currentVisitId!=null)?this.currentVisitId.intValue():0);
        imageEntities.forEach(imageEntity -> {
            addImage(imageEntity.getImagePath());
        });
    }

    void sampleImage() {
        List.of(SAMPLE_IMAGE_PATH).forEach(imagePath -> {
            StreamResource resource = new StreamResource("image", () -> {
                try {
                    return new FileInputStream(new File(imagePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            Image image = new Image(resource, "alt text");
            image.setWidth("400px");  // Set the width of the image to 400 pixels
            this.imagesLayout.add(image);
        });
    }


    void addImage(String imagePath) {
            LOGGER.info("Loading image from path: %s".formatted(imagePath));
            StreamResource resource = new StreamResource("image", () -> {
                try {
                    return new FileInputStream(new File(imagePath));
                } catch (FileNotFoundException e) {
                    LOGGER.error("error reading image",e);
                    return null;
                }
            });
            Image image = new Image(resource, "image from db");
            image.setWidth("400px");  // Set the width of the image to 400 pixels
            this.imagesLayout.add(image);
    }


    private void saveVisit(SaveEvent event) {
        var entity = event.getEntity();
        try {
            saveVisit(entity);
            Notification.show("Visit saved", 3000, Notification.Position.MIDDLE);
            //closeEditor();
        }catch (Exception e) {
            Notification.show("error: %s".formatted(e.getMessage()), 3000, Notification.Position.MIDDLE);

        }

    }

    private void saveVisit(KolposkopijaIzmeklejumsEntity entity){
        entity.setDakteris(sharedData.getSelectedDoctor());
        entity.setPacients(sharedData.getSelectedPatient());
        KolposkopijaIzmeklejumsEntity savedEntity = service.saveVisit(entity);
        this.currentVisitId = savedEntity.getId();
        LOGGER.info("Visit ID: %s saved!".formatted(this.currentVisitId));
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
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);
        save.addClickShortcut(Key.F9);

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


    private void watchDirectory(String directoryPath) {
        LOGGER.info("Watching directory: %s".formatted(directoryPath));
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
                                System.out.println("New file created: " + newFilePath);
                                if(this.currentVisitId != null) {
                                    imageRepository.save(ImageEntity.builder()
                                            .visitId(this.currentVisitId.intValue())
                                            .imagePath(newFilePath).build()); //(, this.currentVisitId.intValue()));
                                }else{

                                    LOGGER.error("Cant save image. Save Visit first");
                                }
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