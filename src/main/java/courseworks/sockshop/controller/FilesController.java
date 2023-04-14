package courseworks.sockshop.controller;

import courseworks.sockshop.service.FileService;
import courseworks.sockshop.service.SockService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/files")
public class FilesController {
    private final SockService sockService;
    private final FileService fileService;

    public FilesController(SockService sockService, FileService fileService) {
        this.sockService = sockService;
        this.fileService = fileService;
    }

    @GetMapping
    @Operation(
            summary = "Скачать файл с данными"
    )
    public ResponseEntity<InputStreamResource> downloadSockFile() {
        Path path = sockService.saveSocksToFile();
        InputStreamResource resource = fileService.downloadFile(path);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(path.toFile().length())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.json")
                .body(resource);
    }

    @GetMapping("/operation")
    @Operation(
            summary = "Скачать файл с операциями"
    )
    public ResponseEntity<InputStreamResource> downloadOperationFile() {
        Path path = fileService.createOperationFile();
        InputStreamResource resource = fileService.downloadFile(path);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(path.toFile().length())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=operation.txt")
                .body(resource);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить файл с данными"
    )
    public ResponseEntity<Object> uploadRecipesFile(@RequestParam MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        sockService.readFromDataFile();
        sockService.saveSocksToFile();
        return ResponseEntity.ok("Новые данные загружены");
    }
}
