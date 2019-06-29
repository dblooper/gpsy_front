package com.gpsy_front.service;

import com.google.gson.Gson;
import com.gpsy_front.Tracks;
import com.gpsy_front.domain.*;
import com.vaadin.flow.component.html.Anchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public final class RestService implements WebMvcConfigurer {

    private static RestService restService;

    private RestTemplate restTemplate = new RestTemplate();

    private RestService() {}

    public static RestService getInstance() {

//        if(restService == null) {
//            synchronized(RestService.class) {
                if(restService == null) {
                    return new RestService();
                }
//            }
//        }
        return restService;
    }

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

    public void updatePlaylistWithPopularTrack(Playlist playlist, Set<ParentTrack> popularTracks) {
        updatePlaylist(playlist, popularTracks);
    }

    public void updatePlaylistWithRecentTrack(Playlist playlist, Set<ParentTrack> recentrTracks) {
        updatePlaylist(playlist, recentrTracks);
    }

    public void updatePlaylist(Playlist playlist, Set<ParentTrack> parentTracks) {
        Gson gson = new Gson();
        List<PlaylistTrack> popularTracksTooUpdate = parentTracks.stream()
                .map(track -> new PlaylistTrack(track.getTrackId(), track.getTitle(), track.getAuthors()))
                .collect(Collectors.toList());

        Playlist playlistUpdated = new Playlist(playlist.getName(), playlist.getPlaylistStringId(), popularTracksTooUpdate);

        String jsonContent = gson.toJson(playlistUpdated);
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/gpsy/playlists/addToPlaylist")
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        String answer = restTemplate.postForObject(uri, entity, String.class);

        System.out.println(answer);
    }

    public void deleteTrackFromPlaylist(Playlist playlist, Set<PlaylistTrack> parentTrack) {
        Gson gson = new Gson();
        List<PlaylistTrack> playlistTracks = new ArrayList<>(parentTrack);

        Playlist playlistWithDeleteTrack = new Playlist(playlist.getName(), playlist.getPlaylistStringId(), playlistTracks);

        String jsonContent = gson.toJson(playlistWithDeleteTrack);
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/gpsy/playlists/deleteTrack")
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        ResponseEntity answer = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

        System.out.println(answer);
    }
}
