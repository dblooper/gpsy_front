package com.gpsy_front.service;

import com.google.gson.Gson;

import com.gpsy_front.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public final class RestService implements WebMvcConfigurer {

    public static final String GPSY_API_ROOT = "http://localhost:8080/v1/gpsy";

    private static RestService restService;

    private RestTemplate restTemplate = new RestTemplate();

    private RestService() {}

    public static RestService getInstance() {

                if(restService == null) {
                    return new RestService();
                }

        return restService;
    }

    public List<PopularTrack> getPopularTracks() {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/spotify/popular").build().encode().toUri();

        try {
            PopularTrack[] mostFrequentTracks = restTemplate.getForObject(uri, PopularTrack[].class);
            System.out.println(mostFrequentTracks[0]);
            return Optional.ofNullable(mostFrequentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<RecentTrack> getRecentTracksFromApi() {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/recent").build().encode().toUri();

        try {
            RecentTrack[] recentTracks = restTemplate.getForObject(uri, RecentTrack[].class);
            return Optional.ofNullable(recentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        } catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<MostFrequentTrack> getFrequentTracksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/frequent").build().encode().toUri();

        try {
            MostFrequentTrack[] frequentTracks = restTemplate.getForObject(uri, MostFrequentTrack[].class);
            return Optional.ofNullable(frequentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }


    public List<Playlist> getPlaylistsFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/current").build().encode().toUri();

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
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/recommended").build().encode().toUri();

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

   public void createNewPlaylist(Playlist playlist) {
        Gson gson = new Gson();
        String jsonContent = gson.toJson(playlist);

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/addNewPlaylist").build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        String answer = restTemplate.postForObject(uri, entity, String.class);

       System.out.println(answer);
   }

    public void updatePlaylist(Playlist playlist, Set<ParentTrack> parentTracks) {
        Gson gson = new Gson();
        List<PlaylistTrack> popularTracksTooUpdate = parentTracks.stream()
                .map(track -> new PlaylistTrack(track.getTrackId(), track.getTitle(), track.getAuthors()))
                .collect(Collectors.toList());

        Playlist playlistUpdated = new Playlist(playlist.getName(), playlist.getPlaylistStringId(), popularTracksTooUpdate);

        String jsonContent = gson.toJson(playlistUpdated);
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/addToPlaylist")
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
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/deleteTrack")
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        ResponseEntity answer = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

        System.out.println(answer);
    }

    public void updatePlaylistDetails(Playlist playlist) {
        Gson gson = new Gson();

        String jsonContent = gson.toJson(playlist);

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/updateDetails")
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonContent, headers);
        ResponseEntity answer = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        System.out.println(answer);
    }

    public RecommendedPlaylist getRecommendedPlaylist() {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended")
                .build().encode().toUri();
        System.out.println(uri.toString());
        try {
            return  Optional.ofNullable(restTemplate.getForObject(uri, RecommendedPlaylist.class)).orElse(new RecommendedPlaylist());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new RecommendedPlaylist();
        }
    }

    public RecommendedPlaylist updateFetchRecommendedPlaylist(int quantityOfTracksForPlaylist) {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended/new")
                .queryParam("qty", quantityOfTracksForPlaylist)
                .build().encode().toUri();
        System.out.println(uri.toString());
        try {
            return  Optional.ofNullable(restTemplate.getForObject(uri, RecommendedPlaylist.class)).orElse(new RecommendedPlaylist());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new RecommendedPlaylist();
        }
    }

    public RecommendedPlaylist changeQuantityOfRecommendedTracks(int quantityOfTracksForPlaylist) {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended/change")
                .queryParam("qty", quantityOfTracksForPlaylist)
                .build().encode().toUri();
        System.out.println(uri.toString());
        try {
            return  Optional.ofNullable(restTemplate.getForObject(uri, RecommendedPlaylist.class)).orElse(new RecommendedPlaylist());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new RecommendedPlaylist();
        }
    }

    public LyricsDto fetchLyrics(String title, String author) {

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
}
