package courseworks.sockshop.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    Path saveToFile(String json);

    Path createOperationFile();

    String readFromFile();

    InputStreamResource downloadFile(Path path);

    void uploadFile(MultipartFile file) throws IOException;
}
