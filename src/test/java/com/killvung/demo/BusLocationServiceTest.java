package com.killvung.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class BusLocationServiceTest {
    @Test
    public void testParseBusLocations() {
        String xmlData = """
                <body>
                <vehicle id="3634" routeTag="52" dirTag="52_0_52G" lat="43.6833648" lon="-79.5665969" secsSinceReport="28" predictable="true" heading="69" speedKmHr="11"/>
                <vehicle id="3650" routeTag="169" dirTag="169_1_169A" lat="43.7741699" lon="-79.2585678" secsSinceReport="27" predictable="true" heading="216" speedKmHr="0"/>
                </body>""";
        BusLocationService service = new BusLocationService(null);
        List<BusLocation> result = service.parseBusLocations(xmlData);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testFetchXmlDataAndParseBusLocation(){
        String apiUrl = "https://retro.umoiq.com/service/publicXMLFeed?command=vehicleLocations&a=ttc";
        BusLocationService service = new BusLocationService(null);
        service.parseBusLocations(service.fetchXmlData(apiUrl));
    }
}