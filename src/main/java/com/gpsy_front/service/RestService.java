package com.gpsy_front.service;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.PopularTrack;
import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.domain.RecommendedTrack;
import com.vaadin.flow.component.html.Anchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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


//    public List<RecentTrack> getRecentTracks() {
//        List<RecentTrack> fetchedTracks = new ArrayList<>();
//        for(int i = 0; i < 10; i++) {
//            fetchedTracks.add(new RecentTrack("id" + i, "title" + i, "author" + i, "date"));
//        }
//
//        return fetchedTracks;
//    }
}
