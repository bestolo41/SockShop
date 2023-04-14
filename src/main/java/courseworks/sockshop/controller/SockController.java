package courseworks.sockshop.controller;

import courseworks.sockshop.model.Color;
import courseworks.sockshop.model.Size;
import courseworks.sockshop.model.Sock;
import courseworks.sockshop.service.FileService;
import courseworks.sockshop.service.SockService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/socks/")
public class SockController {

    private final SockService sockService;
    private final FileService fileService;

    public SockController(SockService sockService, FileService fileService) {
        this.sockService = sockService;
        this.fileService = fileService;
    }

    @GetMapping("/getAll")
    @Operation(
            summary = "Показать весь товар"
    )
    public ResponseEntity<List<Sock>> getAll(){
        return ResponseEntity.ok(sockService.getAllSocks());
    }

    @PostMapping
    @Operation(
            summary = "Добавить товар"
    )
    public ResponseEntity<String> addSocks(@RequestBody Sock sock) {
        sockService.addSocks(sock);
        return ResponseEntity.ok("Товар добавлен в количестве " + sock.getCount() + " штук.");
    }

    @PutMapping
    @Operation(
            summary = "Выдать товар"
    )
    public ResponseEntity<String> issueSocks(@RequestBody Sock sock) {
        sockService.removeSocks(sock);
        sockService.saveOperationToFile("Выдача", sock);
        return ResponseEntity.ok("Товар выдан в количестве " + sock.getCount() + " штук.");
    }

    @GetMapping
    @Operation(
            summary = "Показать количество товара по параметрам"
    )
    public ResponseEntity<String> getSocksByParameters(@RequestParam(required = false) Color color,
                                                       @RequestParam(required = false) Size size,
                                                       @RequestParam(required = false, defaultValue = "0") int cottonMin,
                                                       @RequestParam(required = false, defaultValue = "100") int cottonMax) {
        return ResponseEntity.ok(sockService.getSoksByParameters(color, size, cottonMin, cottonMax));
    }

    @DeleteMapping
    @Operation(
            summary = "Забраковать товар"
    )
    public ResponseEntity<String> deleteSocks(@RequestBody Sock sock) {
        sockService.removeSocks(sock);
        sockService.saveOperationToFile("Забраковано", sock);
        return ResponseEntity.ok("Бракованный товар списан в количестве " + sock.getCount() + " штук.");
    }

}
