package com.gpsy_front.service;

import com.google.gson.Gson;
import com.gpsy_front.Tracks;
import com.gpsy_front.domain.*;
import com.vaadin.flow.component.html.Anchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class RestService implements WebMvcConfigurer {

    private Client client;
    private WebTarget webTarget;

    private RestTemplate restTemplate = new RestTemplate();


    public List<RecentTrack> getRecentTracksFromApi() {

        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/gpsy/tracks/recent").build().encode().toUri();

        try {
            RecentTrack[] recentTracks = restTemplate.getForObject(uri, RecentTrack[].class);
            return Optional.ofNullable(recentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        } catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PopularTrack> getPopularTracksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/gpsy/tracks/popular").build().encode().toUri();

        try {
            PopularTrack[] popularTracks = restTemplate.getForObject(uri, PopularTrack[].class);
            return Optional.ofNullable(popularTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }


    public List<Playlist> getPlaylistsFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/gpsy/playlists/current").build().encode().toUri();

        try {
            Playlist[] playlists = restTemplate.getForObject(uri, Playlist[].class);
            List<Playlist> retrievedPlaylists = Optional.ofNullable(playlists).map(Arrays::asList).orElse(new ArrayList<>());
            retrievedPlaylists.stream().forEach(Playlist::countTracks);
            return retrievedPlaylists;
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<RecommendedTrack> getRecommendedTrcksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/gpsy/tracks/recommended").build().encode().toUri();

        try {
            RecommendedTrack[] recommendedTracks = restTemplate.getForObject(uri, RecommendedTrack[].class);
            List<RecommendedTrack> recommendedTrackList = Optional.ofNullable(recommendedTracks).map(Arrays::asList).orElse(new ArrayList<>());
//            recommendedTrackList.stream()
//                    .forEach(track -> track.setLink(new Anchor(track.getSample(),"Click")));
            return recommendedTrackList;


        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public Playlist updatePlaylistWithPopularTrack(Playlist playlist, Set<PopularTrack> popularTracks) {
        Gson gson = new Gson();

        List<PlaylistTrack> popularTracksTooUpdate = popularTracks.stream()
                .map(track -> new PlaylistTrack(track.getTrackId(), track.getTitle(), track.getAuthors()))
                .collect(Collectors.toList());

        Playlist playlistUpdated = new Playlist(playlist.getName(), playlist.getPlaylistStringId(), popularTracksTooUpdate);

        String jsonContent = gson.toJson(playlistUpdated);
//        URI uri = UriComponentsBuilder.fromHttpUrl("")
//                .build().encode().toUri();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println(jsonContent);
        return new Playlist();
    }
}
