package com.killvung.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bus-locations")
public class BusLocationController {
    private final BusLocationService busLocationService;

    @Autowired
    public BusLocationController(BusLocationService busLocationService) {
        this.busLocationService = busLocationService;
    }

    @GetMapping
    public ResponseEntity<String> fetchBusLocations() {
        String apiUrl = "https://retro.umoiq.com/service/publicXMLFeed?command=vehicleLocations&a=ttc";

        try {
            String xmlData = busLocationService.fetchXmlData(apiUrl);
            List<BusLocation> busLocations = parseBusLocations(xmlData);
            busLocationService.saveBusLocations(busLocations);
            return ResponseEntity.ok("Bus locations ingested successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error ingesting bus locations: " + e.getMessage());
        }
    }

    private List<BusLocation> parseBusLocations(String xmlData) throws Exception {
        Document document = Jsoup.parse(xmlData);
        Elements vehicleElements = document.select("vehicle");

        List<BusLocation> busLocations = new ArrayList<>();

        // Implement XML parsing logic to extract BusLocation objects
        // based on the XML structure provided in the question

        // For demonstration, we'll assume the parsing logic is already implemented
        // and returns a list of BusLocation objects

        for (Element vehicleElement : vehicleElements) {
            Long vehicleId = Long.parseLong(vehicleElement.attr("id"));
            String routeTag = vehicleElement.attr("routeTag");
            String dirTag = vehicleElement.attr("dirTag");
            Double latitude = Double.parseDouble(vehicleElement.attr("lat"));
            Double longitude = Double.parseDouble(vehicleElement.attr("lon"));
            Integer secsSinceReport = Integer.parseInt(vehicleElement.attr("secsSinceReport"));
            Boolean predictable = Boolean.parseBoolean(vehicleElement.attr("predictable"));
            Integer heading = Integer.parseInt(vehicleElement.attr("heading"));
            Double speedKmHr = Double.parseDouble(vehicleElement.attr("speedKmHr"));

            BusLocation busLocation = new BusLocation(
                    vehicleId,
                    routeTag,
                    dirTag,
                    latitude,
                    longitude,
                    secsSinceReport,
                    predictable,
                    heading,
                    speedKmHr
            );
            busLocations.add(busLocation);
        }
        return busLocations;
    }
}
