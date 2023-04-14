package courseworks.sockshop.service.impl;

import courseworks.sockshop.model.exceptions.DataReadingException;
import courseworks.sockshop.model.exceptions.NotFoundException;
import courseworks.sockshop.service.FileService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Getter
public class FileServiceImpl implements FileService {
    @Value("${name.of.data.file}")
    private String dataFileName;
    @Value("${name.of.operations.file}")
    private String operationsFileName;
    @Value("${path.to.data.files}")
    private String dataFilePath;

    @PostConstruct
    void init() {
        createOperationFile();
    }
    @Override
    public Path saveToFile(String json) {
        try {
            return Files.writeString(Path.of(dataFilePath, dataFileName), json);
        } catch (IOException e) {
            throw new DataReadingException("Ошибка записи в файл");
        }
    }

    @Override
    public Path createOperationFile() {
        if (Files.notExists(Path.of(dataFilePath, operationsFileName))) {
            String head = " Дата и время | Тип операции |  Количество  |    Размер    |    Хлопок    |     Цвет     \n" +
                    "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n";
            try {
                Files.createFile(Path.of(dataFilePath, operationsFileName));
                Files.writeString(Path.of(dataFilePath, operationsFileName), head);
            } catch (IOException e) {
                throw new DataReadingException("Ошибка записи в файл");
            }
        }
        return Path.of(dataFilePath, operationsFileName);
    }

    @Override
    public String readFromFile() {
        createEmptyRecipeFileIfNotExists();
        try {
            return Files.readString(Path.of(dataFilePath, dataFileName));
        } catch (IOException e) {
            throw new DataReadingException("Ошибка считывания файла");
        }

    }

    public void createEmptyRecipeFileIfNotExists() {
        if (Files.notExists(Path.of(dataFilePath, dataFileName))) {
            try {
                Files.createFile(Path.of(dataFilePath, dataFileName));
                Files.writeString(Path.of(dataFilePath, dataFileName), "[]");
            } catch (IOException e) {
                throw new NotFoundException("Директория не найдена");
            }
        }
    }

    @Override
    public InputStreamResource downloadFile(Path path) {
        File file = new File(path.toUri());
        try {
            return new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new NotFoundException("Файл не найден");
        }
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        Path filePath = Path.of(dataFilePath, file.getOriginalFilename());
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
    }
}
