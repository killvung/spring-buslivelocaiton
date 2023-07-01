package com.killvung.demo;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BusLocationService {
    private final BusLocationRepository busLocationRepository;

    @Autowired
    public BusLocationService(BusLocationRepository busLocationRepository) {
        this.busLocationRepository = busLocationRepository;
    }

    public void saveBusLocations(List<BusLocation> busLocations) {
        busLocationRepository.saveAll(busLocations);
    }

    public String fetchXmlData(String apiUrl){
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            ClassicHttpRequest httpGet = ClassicRequestBuilder.get(apiUrl).build();
            return httpclient.execute(httpGet, response -> {
                final HttpEntity entity1 = response.getEntity();
                String result = EntityUtils.toString(entity1);
                EntityUtils.consume(entity1);
                return result;
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse xml data", e);
        }
    }

    public List<BusLocation> parseBusLocations(String xmlData) {
        Document document = Jsoup.parse(xmlData);
        Elements vehicleElements = document.select("vehicle");

        List<BusLocation> busLocations = new ArrayList<>();

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