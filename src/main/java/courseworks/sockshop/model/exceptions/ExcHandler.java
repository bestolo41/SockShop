package courseworks.sockshop.model.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice

public class ExcHandler {

    @ExceptionHandler({IncorrectValueException.class, ReceiptException.class, })
    public ResponseEntity<?> IncorrectValueException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<?> InvalidFormatException(InvalidFormatException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Введены некорректные данные");
    }

    @ExceptionHandler({ValueInstantiationException.class})
    public ResponseEntity<?> ValueInstantiationException(ValueInstantiationException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Некорректное количество товара");
    }

    @ExceptionHandler({IOException.class, JsonProcessingException.class})
    public ResponseEntity<?> ioException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка сервера");
    }

    @ExceptionHandler({DataReadingException.class, InternalException.class})
    public ResponseEntity<?> DataException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
