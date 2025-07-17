package uz.gbway.enavbatthermalprintingservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;
import uz.gbway.enavbatthermalprintingservice.service.PrintService;

@Slf4j
@RestController
@RequestMapping("/api")
public class PrintController {

    private final PrintService printService;

    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @PostMapping("/print")
    public ResponseEntity printCheck(@RequestBody PrintReqDto req) {

        log.info("--> Incoming request to print check <--");

        int statusCode = printService.print(req);

        return ResponseEntity.status(HttpStatusCode.valueOf(statusCode)).build();

    }

}
