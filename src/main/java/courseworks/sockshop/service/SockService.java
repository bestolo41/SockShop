package courseworks.sockshop.service;

import courseworks.sockshop.model.Color;
import courseworks.sockshop.model.Size;
import courseworks.sockshop.model.Sock;
import courseworks.sockshop.model.exceptions.IncorrectValueException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface SockService {
    void addSocks(Sock sock);

    void removeSocks(Sock sock);

    String getSoksByParameters(Color color, Size size, int cottonMin, int cottonMax);

    List<Sock> getAllSocks();

    Path saveSocksToFile();

    void readFromDataFile();

    void saveOperationToFile(String type, Sock sock);
}
