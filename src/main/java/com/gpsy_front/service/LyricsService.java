package com.gpsy_front.service;

import com.google.gson.Gson;
import com.gpsy_front.domain.LyricsDto;
import com.gpsy_front.domain.LyricsLibrary;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.gpsy_front.service.RestService.GPSY_API_ROOT;

public final class LyricsService {

    private static LyricsService lyricsService;
    private RestTemplate restTemplate = new RestTemplate();

    private LyricsService() {
    }

    public static LyricsService getInstance() {
        if(lyricsService == null) {
            lyricsService = new LyricsService();
        }
        return  lyricsService;
    }


    public LyricsDto getLyrics(String title, String author) {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/audd/getLyrics/")
                .queryParam("title", title)
                .queryParam("author", author)
                .build().encode().toUri();
        System.out.println(uri.toString());
        try {
            return  Optional.ofNullable(restTemplate.getForObject(uri, LyricsDto.class)).orElse(new LyricsDto("n/a", "/na","n/a"));
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new LyricsDto("n/a", "/na","n/a");
        }
    }

    public List<LyricsLibrary> getLyricsLibrary() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/library/getLibraries").build().encode().toUri();

        try {
            LyricsLibrary[] lyricsLibraries = restTemplate.getForObject(uri, LyricsLibrary[].class);
            List<LyricsLibrary> lyricsLibrariesList = Optional.ofNullable(lyricsLibraries)
                    .map(Arrays::asList)
                    .orElse(new ArrayList<>());

            return lyricsLibrariesList ;

        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public LyricsLibrary createLibrary(LyricsLibrary library) {

        Gson gson = new Gson();
        String jsonContent = gson.toJson(library);

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/library/add").build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        String answer = restTemplate.postForObject(uri, entity, String.class);

        System.out.println(answer);
        return library;
    }

    public void deleteLibrary(long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/library/delete")
                .queryParam("libraryId", id)
                .build().encode().toUri();

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
//        ResponseEntity answer = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        restTemplate.delete(uri);
    }

    public LyricsLibrary addLyricsToLibrary(LyricsLibrary library) {

        Gson gson = new Gson();
        String jsonContent = gson.toJson(library);

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/library/lyrics/add").build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        String answer = restTemplate.postForObject(uri, entity, String.class);

        System.out.println(answer);
        return library;
    }

    public void deleteLyricsFromLibrary(LyricsLibrary library) {

        Gson gson = new Gson();

        String jsonContent = gson.toJson(library);

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/library/lyrics/delete").build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        ResponseEntity answer = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        System.out.println(answer.toString());
    }

    public void updateLibraryName(LyricsLibrary library) {
        Gson gson = new Gson();

        String jsonContent = gson.toJson(library);

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/library/update").build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        ResponseEntity answer = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        System.out.println(answer.toString());
    }
}
