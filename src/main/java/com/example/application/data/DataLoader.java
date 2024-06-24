package com.example.application.data;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private DakterisRepository drRepository;
    private PatientsRepository ptRepository;

    public DataLoader(DakterisRepository drRepository,
                      PatientsRepository ptRepository) {
        this.drRepository = drRepository;
        this.ptRepository = ptRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadDrs();
        loadPacients();
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
}