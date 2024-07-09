package com.example.application.services;

import com.example.application.data.ImageEntity;
import com.example.application.data.ImageRepository;
import com.example.application.data.SharedData;
import com.example.application.pdf.PdfVisitReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestMapping("/api/v2")
@RestController
public class ImageUploadController {

    private static final String UPLOADED_FOLDER = System.getProperty("java.io.tmpdir");
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadController.class);
    SharedData sharedData;
    ImageRepository imRepository;

    public ImageUploadController(SharedData sharedData,
                                 ImageRepository imRepository) {
        this.sharedData = sharedData;
        this.imRepository = imRepository;
        LOGGER.info("ImageUploadController started");
    }

    @GetMapping(value = "/test", produces = "application/json")
    public String test() {
        LOGGER.info("Test worked");
        return "Test worked";
    }
    @PostMapping(value = "/test", produces = "application/json")
    public String postTest() {
        LOGGER.info("Test worked");
        return "Test worked";
    }
    /**
     *
     * @param uploadfile
     * @return
     ```bash
     curl -X POST -H "Content-Type: multipart/form-data" -F "image=@/path/to/your/image.jpg" http://localhost:8080/upload
     ```
     */
    @PostMapping("/upload") // <- endpoint
    public ResponseEntity<?> uploadFile(
            @RequestParam("image") MultipartFile uploadfile) {
        LOGGER.info("Single file upload!");
        if (uploadfile.isEmpty()) {
            return new ResponseEntity<>("please select a file!", HttpStatus.OK);
        }

        try {
            saveUploadedFiles(uploadfile);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    private void saveUploadedFiles(MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            LOGGER.info("Successfully uploaded - " + file.getOriginalFilename());
            if(sharedData.getSelectedVisitId() != null) {
                imRepository.save(ImageEntity.builder()
                        .visitId(sharedData.getSelectedVisitId().intValue())
                        .imagePath(path.toString())
                        .imageIncluded(true)
                        .build());
                LOGGER.info("image saved in DB");
            }else{
                LOGGER.error("Cant save image. Save Visit first");
            }

        }

    }
}
