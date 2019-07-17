package com.gpsy_front.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public final class ServerConnector {

    private static ServerConnector serverConnector;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnector.class);

    private ServerConnector() {}

    public static ServerConnector getInstance() {
        if(serverConnector == null) {
            serverConnector = new ServerConnector();
        }
        return serverConnector;
    }

    public static final String GPSY_API_ROOT = "http://localhost:8080/v1/gpsy";

    private RestTemplate restTemplate = new RestTemplate();

    public void httpServerRequest(Object objectToSend, String httpTailPath, HttpMethod httpMethod) {
        Gson gson = new Gson();
        String jsonContent = gson.toJson(objectToSend);
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + httpTailPath).build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        ResponseEntity answer = restTemplate.exchange(uri, httpMethod, entity, String.class);
        LOGGER.info(answer.toString());
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
