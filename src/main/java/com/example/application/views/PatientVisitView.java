package com.example.application.views;

import com.example.application.data.*;
import com.example.application.pdf.PdfVisitReport;
import com.example.application.services.CrmService;
import com.example.application.system.ResourceBundleControl;
import com.example.application.system.StaticTexts;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
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
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import static com.example.application.helpers.Helpers.asProperties;
import static com.example.application.system.StaticTexts.PDF_PATH_PROPERTY;
import static com.example.application.system.StaticTexts.WATCHER_PATH_PROPERTY;


@PermitAll
@Route(value = "addVisit", layout = MainLayout.class)
@PageTitle("Visit | Colposcope app")
public class PatientVisitView extends FormLayout implements BeforeEnterObserver { //implements HasUrlParameter<String>
    public static final String SAMPLE_IMAGE = "colposcopy-logo.jpg";
    TextField izmeklejumaNr = new TextField("Izmeklējuma Nr");
    DatePicker izmeklejumaDatums = new DatePicker("Izmeklējuma Datums");
    Checkbox vizitesAtkartojums = new Checkbox("Vizites Atkartojums");
    TextField skriningaNr = new TextField("Skrīninga Nr");
    Checkbox kolposkopijaAdekvata  = new Checkbox("Kolposkopija adekvata?");
    TextArea anamneze = new TextArea("Anamnēze");
    TextField iepriekshVeiktaTerapija = new TextField("Iepriekš Veikta Terapija");
    Checkbox alergijas = new Checkbox("Alergijas?");
    TextField trnsformacijasZonasTips = new TextField("Trnsformacijas Zonas Tips");
    TextArea rezultati = new TextArea("Rezultāti");
    TextArea sledziens = new TextArea("Slēdziens");
    TextField nakosaKolposkopijasKontrole = new TextField("Nākošā Kolposkopijas Kontrole");
    Div divFormTitle = new Div();
    //VerticalLayout imagesLayout = new VerticalLayout();
    Button save = new Button("Save (F9)");
    Button close = new Button("Cancel");
    Button btnPatientSelector = new Button("Pacients");

    Button btnPrintPdfReport = new Button("Print PDF report");

    VerticalLayout imagesLayout = new VerticalLayout();
    Div pointsDiv = new Div();
    Binder<KolposkopijaIzmeklejumsEntity> binder = new BeanValidationBinder<>(KolposkopijaIzmeklejumsEntity.class);

    CrmService service;
    SharedData sharedData;
    public static final String DOCTOR_ID_ROUTE_PARAM = "drId";
    public static final String PATIENT_ID_ROUTE_PARAM = "ptId";
    Integer drId;
    Integer ptId;

    ResourceBundle bundle;

    Long currentVisitId = null;
    final private ImageRepository imageRepository;
    final private DoctorSelectorRepository doctorSelectorRepository;

    Map<String,Object> appProperties;

    final private String watcherSercvicePath;

    List<GridRow> items;

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientVisitView.class);

    PdfVisitReport pdfReport;
    public PatientVisitView(CrmService service
            , SharedData sharedData,
              ImageRepository imageRepository,
              DoctorSelectorRepository doctorSelectorRepository,
              Environment env,
              PdfVisitReport pdfReport
    ) {

        this.service = service;
        this.sharedData = sharedData;
        this.imageRepository = imageRepository;
        this.doctorSelectorRepository = doctorSelectorRepository;
        this.appProperties = asProperties(env);
        this.pdfReport = pdfReport;


        watcherSercvicePath = this.appProperties.get(WATCHER_PATH_PROPERTY).toString();
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



        sampleImage();  // add sample to imagesLayout

        divFormTitle.add(getFormTitle());


        add(divFormTitle,
                createButtonsLayout(),
                izmeklejumaNr,
                izmeklejumaDatums,
                vizitesAtkartojums,
                skriningaNr,
                anamneze,
                iepriekshVeiktaTerapija,
                alergijas,
                trnsformacijasZonasTips,
                kolposkopijaAdekvata,
                featureGrid(),
                rezultati,
                sledziens,
                nakosaKolposkopijasKontrole,
                imagesLayout,
                addRefreshButton()
        );
        KolposkopijaIzmeklejumsEntity visit = new KolposkopijaIzmeklejumsEntity();
        LocalDate currentDate = LocalDate.now();
        Instant currentInstant = currentDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        visit.setIzmeklejumaDatums(currentInstant);
        this.setVisit(visit);
        this.addSaveListener(this::saveVisit); // <1>
        this.addCloseListener(e -> closeEditor());
        UI.getCurrent().setPollInterval(30000); // Polling interval is set in milliseconds

        UI.getCurrent().addPollListener(e -> refreshImagesLayout());
    }


    void calcPoints(){
        pointsDiv.removeAll();
        AtomicInteger count = new AtomicInteger(0);
        this.items.forEach(item -> count.addAndGet(item.value));
        pointsDiv.add(new Div(new Div("Points: %s".formatted(count.get()))));
    }

    String getTranslation1(String key) {
        if (this.bundle == null) {
            this.bundle = ResourceBundle.getBundle("messages/messages_en", new ResourceBundleControl());
        }
        return bundle.getString(key);
    }

    @Builder
    @Getter
    @Setter
    public static class GridRow {
        private String label;
        private String option1;
        private String option2;
        private String option3;
        private Checkbox checkbox1;
        private Checkbox checkbox2;
        private Checkbox checkbox3;
        private int value;
        private String id;
    }

    Component featureGrid() {

        var layout = new VerticalLayout();

        layout.add(this.pointsDiv);

        pointsDiv.getStyle().set("font-size", "24px");
        pointsDiv.getStyle().set("font-weight", "bold");

        Checkbox cb1_0p = new Checkbox();
        Checkbox cb1_1p = new Checkbox();
        Checkbox cb1_2p = new Checkbox();

        Checkbox cb2_0p = new Checkbox();
        Checkbox cb2_1p = new Checkbox();
        Checkbox cb2_2p = new Checkbox();

        Checkbox cb3_0p = new Checkbox();
        Checkbox cb3_1p = new Checkbox();
        Checkbox cb3_2p = new Checkbox();

        Checkbox cb4_0p = new Checkbox();
        Checkbox cb4_1p = new Checkbox();
        Checkbox cb4_2p = new Checkbox();

        Checkbox cb5_0p = new Checkbox();
        Checkbox cb5_1p = new Checkbox();
        Checkbox cb5_2p = new Checkbox();

        this.items = Arrays.asList(
                new GridRow(StaticTexts.P1_LABEL, StaticTexts.P1_0P, StaticTexts.P1_1P, StaticTexts.P1_2P, cb1_0p, cb1_1p, cb1_2p, 0, "P1"),
                new GridRow(StaticTexts.P2_LABEL, StaticTexts.P2_0P, StaticTexts.P2_1P, StaticTexts.P2_2P, cb2_0p, cb2_1p, cb2_2p, 0, "P2"),
                new GridRow(StaticTexts.P3_LABEL, StaticTexts.P3_0P, StaticTexts.P3_1P, StaticTexts.P3_2P, cb3_0p, cb3_1p, cb3_2p, 0, "P3"),
                new GridRow(StaticTexts.P4_LABEL, StaticTexts.P4_0P, StaticTexts.P4_1P, StaticTexts.P4_2P, cb4_0p, cb4_1p, cb4_2p, 0, "P4"),
                new GridRow(StaticTexts.P5_LABEL, StaticTexts.P5_0P, StaticTexts.P5_1P, StaticTexts.P5_2P, cb5_0p, cb5_1p, cb5_2p, 0, "P5")
        );

        addValueChangeCb1Listener(this.items.get(0),cb1_0p, cb1_1p, cb1_2p);
        addValueChangeCb1Listener(this.items.get(1),cb2_0p, cb2_1p, cb2_2p);
        addValueChangeCb1Listener(this.items.get(2),cb3_0p, cb3_1p, cb3_2p);
        addValueChangeCb1Listener(this.items.get(3),cb4_0p, cb4_1p, cb4_2p);
        addValueChangeCb1Listener(this.items.get(4),cb5_0p, cb5_1p, cb5_2p);

        for (GridRow item : items) {
            // Create a label with blue font color
            Div label = new Div();
            label.getElement().setProperty("innerHTML", item.getLabel());
            label.getStyle().set("color", "blue");

            // Create checkboxes
            item.checkbox1.setLabel(item.getOption1());
            item.checkbox2.setLabel(item.getOption2());
            item.checkbox3.setLabel(item.getOption3());

            // Add the label and checkboxes to a vertical layout
            VerticalLayout group = new VerticalLayout(label, item.checkbox1, item.checkbox2, item.checkbox3);

            // Add a border to the group
            group.getStyle().set("border", "1px solid black");

            // Add the group to the main layout
            layout.add(group);
        }

        return layout;
    }
    private void addValueChangeCb1Listener(GridRow gr, Checkbox... _checkboxes) {
        for (Checkbox checkbox : _checkboxes) {
            checkbox.addValueChangeListener(event -> {
                Checkbox sourceCheckbox = event.getSource();
                for (Checkbox checkboxToClear : _checkboxes) {
                    if (checkboxToClear != sourceCheckbox && event.getValue()) {
                        checkboxToClear.clear();
                    }
                }

                if(_checkboxes[0].getValue()) gr.value=0;
                else if(_checkboxes[1].getValue()) gr.value=1;
                else if(_checkboxes[2].getValue()) gr.value=2;
                else gr.value = 0;
                calcPoints();
            });

        }
    }

    Component addRefreshButton() {

        //layout.removeAll();
        Button refreshButton = new Button("Refresh Images (F8)");
        refreshButton.addClickShortcut(Key.F8);

        refreshButton.addClickListener(e -> refreshImagesLayout());
        return refreshButton;
    }

    Component addPatientSelectionButton() {
        Button openDialogButton = new Button("Patient selector");
        openDialogButton.addClickListener(event -> {
            Dialog dialog = new Dialog();
            dialog.addDetachListener(evt -> {
                divFormTitle.removeAll();
                divFormTitle.add(getFormTitle());
            });
            PatientSelectorView patientSelectionView = new PatientSelectorView(sharedData, service);
            patientSelectionView.setDialog(dialog);
            dialog.setSizeFull();
            dialog.add(patientSelectionView);
            dialog.open();
        });
        return openDialogButton;
    }

    void refreshImagesLayout() {
        // Clear the layout
        LOGGER.info("Refreshing images for visit id: %s saved!".formatted(this.currentVisitId));

        this.imagesLayout.removeAll();

        sampleImage();

        // Fetch images from the database
        List<ImageEntity> imageEntities = imageRepository.findByVisitId((this.currentVisitId!=null)?this.currentVisitId.intValue():0);
        imageEntities.forEach(imageEntity -> addImage(imageEntity));
    }

    void sampleImage() {
        try{
        var watcherSercvicePath = this.appProperties.get(WATCHER_PATH_PROPERTY).toString();

        List.of(watcherSercvicePath+"/"+SAMPLE_IMAGE).forEach(imagePath -> {

            StreamResource resource = new StreamResource("image", () -> {
                try {
                    return new FileInputStream(imagePath);
                } catch (FileNotFoundException e) {
                    LOGGER.error("error reading sample image",e);
                    return null;
                }
            });
            Image image = new Image(resource, "alt text");
            image.setWidth("400px");  // Set the width of the image to 400 pixels
            this.imagesLayout.add(image);
        });
        } catch (Exception e) {
            LOGGER.error("error reading sample image",e);
        }
    }


    void addImage(ImageEntity entity ){//} .getImagePath()  String imagePath) {
            LOGGER.info("Loading image from path: %s".formatted(entity.getImagePath()));
            StreamResource resource = new StreamResource("image", () -> {
                try {
                    return new FileInputStream(new File(entity.getImagePath()));
                } catch (FileNotFoundException e) {
                    LOGGER.error("error reading image",e);
                    return null;
                }
            });
            Image image = new Image(resource, "colposcopy image");
            image.setWidth("400px");  // Set the width of the image to 400 pixels
            Checkbox imageIncluded = new Checkbox("Iekļaut");
            imageIncluded.setValue(entity.getImageIncluded()!=null?entity.getImageIncluded():false);
            imageIncluded.addValueChangeListener(event -> {
                boolean isIncluded = event.getValue();
                entity.setImageIncluded(isIncluded);
                imageRepository.save(entity);
            });
            this.imagesLayout.add(imageIncluded, image);
    }


    private void saveVisit(SaveEvent event) {
        var entity = event.getEntity();
        items.forEach(item -> {
            switch (item.id) {
                case "P1" -> entity.setP1(item.value);
                case "P2" -> entity.setP2(item.value);
                case "P3" -> entity.setP3(item.value);
                case "P4" -> entity.setP4(item.value);
                case "P5" -> entity.setP5(item.value);
            }
            LOGGER.info("Item: %s value: %s".formatted(item.label, item.value));
        });

        try {
            saveVisit(entity);
            Notification.show("Visit saved", 3000, Notification.Position.MIDDLE);
            sharedData.setSelectedVisitId(entity.getId());
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
//        var dakterisSelector = doctorSelectorRepository.search()
//                .stream()
//                .findFirst()
//                .orElse(null);

        DakterisEntity dakterisEntity = doctorSelectorRepository.search()
                .stream()
                .map(DakterisSelectorEntity::getDakterisEntity) // Extract DakterisEntity from DakterisSelectorEntity
                .findFirst()
                .orElse(null);
        sharedData.setSelectedDoctor(dakterisEntity);

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

        btnPrintPdfReport.addClickListener(event -> {

            String filePathPrefix = this.appProperties.get(PDF_PATH_PROPERTY).toString();

            sharedData.setPdfReportFilename(getPdfReportFilename(binder.getBean(),filePathPrefix));

            this.pdfReport.generate();
            // viewer

            Dialog dialog = new Dialog();
            PdfViewerView pdfViewerView = new PdfViewerView(sharedData);
            Button closeButton = new Button("Close", event_ -> dialog.close());
            dialog.add(closeButton, pdfViewerView);
            dialog.setSizeFull();
            dialog.open();
    });

        return new HorizontalLayout(btnPatientSelector, save, close, addPatientSelectionButton(),btnPrintPdfReport);
    }


    public static String getPdfReportFilename(KolposkopijaIzmeklejumsEntity entity, String filePathPrefix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return "%s/kolposkopija-%s_%s.pdf".formatted(
                filePathPrefix,
                formatter.format(ZonedDateTime.ofInstant(
                        entity.getIzmeklejumaDatums(),
                        ZoneId.systemDefault())),
                entity.getPacients().getVardsUzvardsPacients().replace(" ", "-")
        );
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
                                            .imagePath(newFilePath)
                                            .imageIncluded(true)
                                            .build());
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


}