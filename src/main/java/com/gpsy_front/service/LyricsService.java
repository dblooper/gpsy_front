package com.gpsy_front.service;

import com.gpsy_front.domain.LyricsLibrary;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.gpsy_front.service.ServerConnector.GPSY_API_ROOT;

public final class LyricsService {

    private static LyricsService lyricsService;
    private ServerConnector serverConnector = ServerConnector.getInstance();

    private LyricsService() {
    }

    public static LyricsService getInstance() {
        if(lyricsService == null) {
            lyricsService = new LyricsService();
        }
        return  lyricsService;
    }

    public List<LyricsLibrary> getLyricsLibrary() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/libraries/get").build().encode().toUri();

        try {
            LyricsLibrary[] lyricsLibraries = serverConnector.getRestTemplate().getForObject(uri, LyricsLibrary[].class);
            return Optional.ofNullable(lyricsLibraries)
                    .map(Arrays::asList)
                    .orElse(new ArrayList<>());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public LyricsLibrary createLibrary(LyricsLibrary library) {
        serverConnector.httpServerRequest(library,"/libraries/new", HttpMethod.POST );
        return library;
    }

    public void deleteLibrary(long id) {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/libraries/delete")
                .queryParam("libraryId", id)
                .build().encode().toUri();
        serverConnector.getRestTemplate().delete(uri);
    }

    public LyricsLibrary addLyricsToLibrary(LyricsLibrary library) {
        serverConnector.httpServerRequest(library, "/libraries/lyrics/add", HttpMethod.POST );
        return library;
    }

    public void deleteLyricsFromLibrary(LyricsLibrary library) {
        serverConnector.httpServerRequest(library,"/libraries/lyrics/delete", HttpMethod.DELETE );
    }

    public void updateLibraryName(LyricsLibrary library) {
        serverConnector.httpServerRequest(library,"/libraries/update", HttpMethod.PUT );
    }
}
