package courseworks.sockshop.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import courseworks.sockshop.model.Color;
import courseworks.sockshop.model.Size;
import courseworks.sockshop.model.Sock;
import courseworks.sockshop.model.exceptions.DataReadingException;
import courseworks.sockshop.model.exceptions.IncorrectValueException;
import courseworks.sockshop.model.exceptions.InternalException;
import courseworks.sockshop.service.FileService;
import courseworks.sockshop.service.SockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SockServiceImpl implements SockService {
    private final FileService fileService;

    private static List<Sock> socks = new ArrayList<>();

    public SockServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    void init() {
        readFromDataFile();

    }

    @Override
    public void addSocks(Sock sock) {
        if (!socks.contains(sock)) {
            socks.add(sock);
        } else {
            int newCount = socks.get(socks.indexOf(sock)).getCount() + sock.getCount();
            socks.get(socks.indexOf(sock)).setCount(newCount);
        }
        saveSocksToFile();
        saveOperationToFile("Прием", sock);
    }

    @Override
    public void removeSocks(Sock sock) {
        int countOnStorage = socks.get(socks.indexOf(sock)).getCount();

        if (!socks.contains(sock)) {
            throw new IncorrectValueException("Такого товара нет на складе");
        }
        if (sock.getCount() <= 0) {
            throw new IncorrectValueException("Некорректное количество товара");
        }

        if (sock.getCount() > countOnStorage) {
            throw new IncorrectValueException("На складе осталось " + countOnStorage + " таких носков");
        }

        int newCount = countOnStorage - sock.getCount();
        socks.get(socks.indexOf(sock)).setCount(newCount);
        saveSocksToFile();
    }

    @Override
    public String getSoksByParameters(Color color, Size size, int cottonMin, int cottonMax) {
        int socksQuantity = 0;

        if (color == null && size == null) {
            for (Sock sock : socks) {
                if (sock.getCottonPart() >= cottonMin &&
                        sock.getCottonPart() <= cottonMax) {
                    socksQuantity = socksQuantity + sock.getCount();
                }
            }
        } else if (color == null) {
            for (Sock sock : socks) {
                if (sock.getSize().equals(size) &&
                        sock.getCottonPart() >= cottonMin &&
                        sock.getCottonPart() <= cottonMax) {
                    socksQuantity = socksQuantity + sock.getCount();
                }
            }
        } else if (size == null) {
            for (Sock sock : socks) {
                if (sock.getColor().equals(color) &&
                        sock.getCottonPart() >= cottonMin &&
                        sock.getCottonPart() <= cottonMax) {
                    socksQuantity = socksQuantity + sock.getCount();
                }
            }
        } else {
            for (Sock sock : socks) {
                if (sock.getColor().equals(color) &&
                        sock.getSize().equals(size) &&
                        sock.getCottonPart() >= cottonMin &&
                        sock.getCottonPart() <= cottonMax) {
                    socksQuantity = socksQuantity + sock.getCount();
                }
            }
        }
        return String.valueOf(socksQuantity);
    }

    @Override
    public List<Sock> getAllSocks() {
        return socks;
    }

    @Override
    public Path saveSocksToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socks);
            return fileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new DataReadingException("Ошибка получения строки из данных");
        }
    }

    @Override
    public void readFromDataFile() {
        String json = fileService.readFromFile();
        try {
            socks = new ObjectMapper().readValue(json, new TypeReference<ArrayList<Sock>>() {
            });
            socks = checkDuplicateElements(socks);
        } catch (JsonProcessingException e) {
            throw new DataReadingException("Ошибка сохранения данных");
        }
    }

    @Override
    public void saveOperationToFile(String type, Sock sock) {
        Path path = fileService.createOperationFile();
        try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            writer.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")) + "|" +
                    StringUtils.center(type, 14) + "|" +
                    StringUtils.center(Integer.toString(sock.getCount()), 14) + "|" +
                    StringUtils.center(sock.getSize().getValue(), 14) + "|" +
                    StringUtils.center(Integer.toString(sock.getCottonPart()), 14) + "|" +
                    StringUtils.center(sock.getColor().getValue(), 14) + "\n");
        } catch (IOException e) {
            throw new InternalException("Ошибка записи операции");
        }
    }

    private List<Sock> checkDuplicateElements(List<Sock> array) {
        List<Sock> checkedArray = new ArrayList<>();
        for (Sock sock : array) {
            if (!checkedArray.contains(sock)) {
                checkedArray.add(sock);
            } else {
                int newCount = checkedArray.get(checkedArray.indexOf(sock)).getCount() + sock.getCount();
                checkedArray.get(checkedArray.indexOf(sock)).setCount(newCount);
            }
        }
        return checkedArray;
    }

}
