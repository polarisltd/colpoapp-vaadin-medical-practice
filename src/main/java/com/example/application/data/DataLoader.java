package com.example.application.data;


import com.example.application.pdf.PdfHelloWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);
    private DakterisRepository drRepository;
    private PatientsRepository ptRepository;
    private ImageRepository imRepository;
    private KolposkopijaIzmeklejumsRepository visitRepository;
    private Long drId;
    private Long ptId;
    private Long visitId;
    Environment env;

    public DataLoader(DakterisRepository drRepository,
                      PatientsRepository ptRepository,
                      ImageRepository imRepository,
                      KolposkopijaIzmeklejumsRepository visitRepository,
                      Environment env){
        this.drRepository = drRepository;
        this.ptRepository = ptRepository;
        this.imRepository = imRepository;
        this.visitRepository = visitRepository;
        this.env = env;
    }

    @Override
    public void run(String... args) throws Exception {
        // ignore for -Dspring.profiles.active=prod
        boolean isProdActive = Arrays.asList(env.getActiveProfiles()).contains("prod");
        if (!isProdActive) {
            loadDrs();
            loadPacients();
            loadVisit();
            loadImages();
        }
    }


    private void loadDrs() {
        if(drRepository.count()==0) {
          try{
            DakterisEntity dakterisEntity = DakterisEntity.builder()
                    .id(1L)
                    .vardsUzvardsDakteris("Dr. Kolposkops Inese")
                    .build();
            drRepository.save(dakterisEntity);
            drId = dakterisEntity.getId();
        }catch(Exception e) {
            LOGGER.error("Error loading Visit: ", e);
        }
        }
    }
    private void loadPacients() {
        if(ptRepository.count()==0) {
         try{
            PacientsEntity pacientsEntity = PacientsEntity.builder()
                    .id(1L)
                    .vardsUzvardsPacients("Janis Berzins")
                    .personasKods("123456-12345")
                    .dzimsanasGads(1980)
                    .build();
            ptRepository.save(pacientsEntity);
            ptId = pacientsEntity.getId();
        }catch(Exception e) {
            LOGGER.error("Error loading Patients: ", e);
        }
        }
    }

    private void loadVisit() {
        if(visitRepository.count()==0) {
            try {
                KolposkopijaIzmeklejumsEntity visitEntity = KolposkopijaIzmeklejumsEntity.builder()
                        .id(1L)
                        .dakteris(drRepository.findById(drId).get())
                        .pacients(ptRepository.findById(ptId).get())
                        .build();
                visitRepository.save(visitEntity);
                this.visitId = visitEntity.getId();
            }catch(Exception e) {
                LOGGER.error("Error loading Visit: ", e);
            }
        }
    }

    private void loadImages() {
        if(imRepository.count()==0) {
        try{
            ImageEntity imagesEntity = ImageEntity.builder()
                    .id(1)
                    .visitId(this.visitId.intValue())
                    .imagePath("C:\\Users\\polar\\Downloads\\001\\pog009-3.jpg")
                    .build();
            imRepository.save(imagesEntity);
        }catch(Exception e) {
            LOGGER.error("Error loading Image: ", e);
        }
        }
    }



}