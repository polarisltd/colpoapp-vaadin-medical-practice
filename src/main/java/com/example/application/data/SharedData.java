package com.example.application.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

@Service
@Scope("singleton")
@Getter
@Setter
@Component
public class SharedData {
    private PacientsEntity selectedPatient;
    private DakterisEntity selectedDoctor;
    private Long selectedVisitId;
    private String pdfReportFilename;
    @Value("${watcher.path}")
    private String watcherPath;
    @Value("${pdf.path}")
    private String pdfPath;
}