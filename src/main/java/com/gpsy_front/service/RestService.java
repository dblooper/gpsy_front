package com.gpsy_front.service;

import com.gpsy_front.domain.*;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.gpsy_front.service.ServerConnector.GPSY_API_ROOT;

public final class RestService {

    private static RestService restService;

    private ServerConnector serverConnector = ServerConnector.getInstance();

    private RestService() {}

    public static RestService getInstance() {
        if(restService == null) {
            restService = new RestService();
        }
        return restService;
    }

    public List<PopularTrack> getPopularTracks() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/spotify/popular").build().encode().toUri();
        try {
            PopularTrack[] mostFrequentTracks = serverConnector.getRestTemplate().getForObject(uri, PopularTrack[].class);
            System.out.println(mostFrequentTracks[0]);
            return Optional.ofNullable(mostFrequentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<SearchedTrack> getSearchedTracks(String searchedItem) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/search")
                .queryParam("searchedItem", searchedItem)
                .build().encode().toUri();

        try {
            SearchedTrack[] searchedTracks = serverConnector.getRestTemplate().getForObject(uri, SearchedTrack[].class);
            System.out.println(searchedTracks[0]);
            return Optional.ofNullable(searchedTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<RecentTrack> getRecentTracksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/recent").build().encode().toUri();
        try {
            RecentTrack[] recentTracks = serverConnector.getRestTemplate().getForObject(uri, RecentTrack[].class);
            return Optional.ofNullable(recentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        } catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<MostFrequentTrack> getFrequentTracksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/frequent").build().encode().toUri();
        try {
            MostFrequentTrack[] frequentTracks = serverConnector.getRestTemplate().getForObject(uri, MostFrequentTrack[].class);
            return Optional.ofNullable(frequentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }


    public List<Playlist> getPlaylistsFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/current").build().encode().toUri();

        try {
            Playlist[] playlists = serverConnector.getRestTemplate().getForObject(uri, Playlist[].class);
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
            RecommendedTrack[] recommendedTracks = serverConnector.getRestTemplate().getForObject(uri, RecommendedTrack[].class);
            List<RecommendedTrack> recommendedTrackList = Optional.ofNullable(recommendedTracks)
                    .map(Arrays::asList)
                    .orElse(new ArrayList<>());
            return recommendedTrackList;
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public void updatePlaylistWithPopularTrack(Playlist playlist, Set<ParentTrack> popularTracks) {
        List<PlaylistTrack> popularTracksTooUpdate = popularTracks.stream()
                .map(track -> new PlaylistTrack(track.getTrackId(), track.getTitle(), track.getAuthors()))
                .collect(Collectors.toList());

        Playlist playlistUpdated = new Playlist(playlist.getName(), playlist.getPlaylistStringId(), popularTracksTooUpdate);

        serverConnector.httpServerRequest(playlistUpdated, "/playlists/tracks/add", HttpMethod.POST);
  }

   public void createNewPlaylist(Playlist playlist) {
       serverConnector.httpServerRequest(playlist, "/playlists/new", HttpMethod.POST);
   }

    public void deleteTrackFromPlaylist(Playlist playlist) {
        serverConnector.httpServerRequest(playlist, "/playlists/tracks/delete", HttpMethod.DELETE );
    }

    public void updatePlaylistDetails(Playlist playlist) {
        serverConnector.httpServerRequest(playlist, "/playlists/update", HttpMethod.PUT );
    }

    public RecommendedPlaylist getRecommendedPlaylist() {

        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended")
                .build().encode().toUri();
        System.out.println(uri.toString());
        try {
            return  Optional.ofNullable(serverConnector.getRestTemplate().getForObject(uri, RecommendedPlaylist.class)).orElse(new RecommendedPlaylist());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new RecommendedPlaylist();
        }
    }

    public RecommendedPlaylist updateFetchRecommendedPlaylist(int quantityOfTracksForPlaylist) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended/new")
                .queryParam("qty", quantityOfTracksForPlaylist)
                .build().encode().toUri();
        try {
            return  Optional.ofNullable(serverConnector.getRestTemplate().getForObject(uri, RecommendedPlaylist.class)).orElse(new RecommendedPlaylist());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new RecommendedPlaylist();
        }
    }

    public RecommendedPlaylist changeQuantityOfRecommendedTracks(int quantityOfTracksForPlaylist) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended/change")
                .queryParam("qty", quantityOfTracksForPlaylist)
                .build().encode().toUri();
        try {
            return  Optional.ofNullable(serverConnector.getRestTemplate().getForObject(uri, RecommendedPlaylist.class)).orElse(new RecommendedPlaylist());
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new RecommendedPlaylist();
        }
    }

    public LyricsDto getLyrics(String title, String author) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/musixmatch/lyrics")
                .queryParam("title", title)
                .queryParam("author", author)
                .build().encode().toUri();
        try {
            return  Optional.ofNullable(serverConnector.getRestTemplate().getForObject(uri, LyricsDto.class)).orElse(new LyricsDto("n/a", "/na","n/a"));
        }catch(RestClientException e) {
            System.out.println(e.getMessage());
            return new LyricsDto("n/a", "/na","n/a");
        }
    }
}
