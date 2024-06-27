package com.example.application.data;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private DakterisRepository drRepository;
    private PatientsRepository ptRepository;
    private ImageRepository imRepository;
    private KolposkopijaIzmeklejumsRepository visitRepository;
    public DataLoader(DakterisRepository drRepository,
                      PatientsRepository ptRepository,
                      ImageRepository imRepository,
                      KolposkopijaIzmeklejumsRepository visitRepository ){
        this.drRepository = drRepository;
        this.ptRepository = ptRepository;
        this.imRepository = imRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadDrs();
        loadPacients();
        loadVisit();
        loadImages();
    }


    private void loadDrs() {
        if(drRepository.count()==0) {
            DakterisEntity dakterisEntity = DakterisEntity.builder()
                    .id(1L)
                    .vardsUzvardsDakteris("Dr. Kolposkops Inese")
                    .build();
            drRepository.save(dakterisEntity);
        }
    }
    private void loadPacients() {
        if(ptRepository.count()==0) {
            PacientsEntity pacientsEntity = PacientsEntity.builder()
                    .id(1L)
                    .vardsUzvardsPacients("Janis Berzins")
                    .personasKods("123456-12345")
                    .dzimsanasGads(1980)
                    .build();
            ptRepository.save(pacientsEntity);
        }
    }

    private void loadVisit() {
        if(visitRepository.count()==0) {
            KolposkopijaIzmeklejumsEntity visitEntity = KolposkopijaIzmeklejumsEntity.builder()
                    .id(1L)
                    .dakteris(drRepository.findById(1L).get())
                    .pacients(ptRepository.findById(1L).get())
                    .build();
            visitRepository.save(visitEntity);
        }
    }

    private void loadImages() {
        if(imRepository.count()==0) {
            ImageEntity imagesEntity = ImageEntity.builder()
                    .id(1)
                    .visitId(1)
                    .imagePath("C:\\Users\\polar\\Downloads\\001\\pog009-3.jpg")
                    .build();
            imRepository.save(imagesEntity);
        }
    }



}