package com.example.application.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

@Service
@Scope("singleton")
@Getter
@Setter
public class SharedData {
    private PacientsEntity selectedPatient;
    private DakterisEntity selectedDoctor;
    private Long selectedVisitId;
}