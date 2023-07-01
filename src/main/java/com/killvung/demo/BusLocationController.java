package com.killvung.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bus-locations")
public class BusLocationController {
    private final BusLocationService busLocationService;

    @Autowired
    public BusLocationController(BusLocationService busLocationService) {
        this.busLocationService = busLocationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<BusLocation>> getBusLocations() {
        return ResponseEntity.ok(busLocationService.getBusLocations());
    }

    @GetMapping("/ingest")
    public ResponseEntity<String> ingestBusLocations() {
        String apiUrl = "https://retro.umoiq.com/service/publicXMLFeed?command=vehicleLocations&a=ttc";

        try {
            String xmlData = busLocationService.fetchXmlData(apiUrl);
            List<BusLocation> busLocations = busLocationService.parseBusLocations(xmlData);
            busLocationService.saveBusLocations(busLocations);
            return ResponseEntity.ok("Bus locations ingested successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error ingesting bus locations: " + e.getMessage());
        }
    }
}
