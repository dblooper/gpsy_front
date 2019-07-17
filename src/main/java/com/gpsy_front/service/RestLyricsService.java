package com.gpsy_front.service;

import com.gpsy_front.domain.Lyrics;
import com.gpsy_front.domain.LyricsLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.gpsy_front.service.ServerConnector.GPSY_API_ROOT;

public final class RestLyricsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestLyricsService.class);

    private static RestLyricsService restLyricsService;
    private ServerConnector serverConnector = ServerConnector.getInstance();

    private RestLyricsService() {
    }

    public static RestLyricsService getInstance() {
        if(restLyricsService == null) {
            restLyricsService = new RestLyricsService();
        }
        return restLyricsService;
    }

    public Lyrics getLyrics(String title, String author) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/musixmatch/lyrics")
                .queryParam("title", title)
                .queryParam("author", author)
                .build().encode().toUri();
        try {
            return  Optional.ofNullable(serverConnector.getRestTemplate().getForObject(uri, Lyrics.class)).orElse(new Lyrics("n/a", "/na","n/a"));
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new Lyrics("n/a", "/na","n/a");
        }
    }

    public List<LyricsLibrary> getLyricsLibrary() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/libraries/get").build().encode().toUri();

        try {
            LyricsLibrary[] lyricsLibraries = serverConnector.getRestTemplate().getForObject(uri, LyricsLibrary[].class);
            return Optional.ofNullable(lyricsLibraries)
                    .map(Arrays::asList)
                    .orElse(new ArrayList<>());
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
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
