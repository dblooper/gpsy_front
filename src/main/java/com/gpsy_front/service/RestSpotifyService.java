package com.gpsy_front.service;

import com.gpsy_front.config.InitialLimitValues;
import com.gpsy_front.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.gpsy_front.service.ServerConnector.GPSY_API_ROOT;

public final class RestSpotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestSpotifyService.class);

    private static RestSpotifyService restSpotifyService;

    private ServerConnector serverConnector = ServerConnector.getInstance();

    private RestSpotifyService() {}

    public static RestSpotifyService getInstance() {
        if(restSpotifyService == null) {
            restSpotifyService = new RestSpotifyService();
        }
        return restSpotifyService;
    }

    public List<PopularTrack> getPopularTracks() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/spotify/popular")
                .queryParam("qty", InitialLimitValues.LIMIT_POPULAR_TRACKS).build().encode().toUri();
        try {
            PopularTrack[] mostFrequentTracks = serverConnector.getRestTemplate().getForObject(uri, PopularTrack[].class);
            return Optional.ofNullable(mostFrequentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<SearchedTrack> getSearchedTracks(String searchedItem) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/search")
                .queryParam("searchedItem", searchedItem)
                .build().encode().toUri();

        try {
            SearchedTrack[] searchedTracks = serverConnector.getRestTemplate().getForObject(uri, SearchedTrack[].class);
            return Optional.ofNullable(searchedTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<RecentTrack> getRecentTracksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/recent")
                .queryParam("qty", InitialLimitValues.LIMIT_RECENT_TRACKS).build().encode().toUri();
        try {
            RecentTrack[] recentTracks = serverConnector.getRestTemplate().getForObject(uri, RecentTrack[].class);
            return Optional.ofNullable(recentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        } catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<MostFrequentTrack> getFrequentTracksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/frequent")
                .queryParam("qty", InitialLimitValues.LIMIT_FREQUENT_TRACKS).build().encode().toUri();
        try {
            MostFrequentTrack[] frequentTracks = serverConnector.getRestTemplate().getForObject(uri, MostFrequentTrack[].class);
            return Optional.ofNullable(frequentTracks).map(Arrays::asList).orElse(new ArrayList<>());
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<RecommendedTrack> getRecommendedTrcksFromApi() {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/tracks/recommended")
                .queryParam("qty", InitialLimitValues.LIMIT_RECOMMENDED_TRACKS).build().encode().toUri();

        try {
            RecommendedTrack[] recommendedTracks = serverConnector.getRestTemplate().getForObject(uri, RecommendedTrack[].class);
            return Optional.ofNullable(recommendedTracks)
                    .map(Arrays::asList)
                    .orElse(new ArrayList<>());
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void updatePlaylistWithPopularTrack(Playlist playlist, Set<ParentTrack> popularTracks) {
        List<PlaylistTrack> popularTracksTooUpdate = popularTracks.stream()
                .map(track -> new PlaylistTrack(track.getTrackStringId(), track.getTitle(), track.getArtists()))
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
        try {
            return  Optional.ofNullable(serverConnector.getRestTemplate().getForObject(uri, RecommendedPlaylist.class)).orElse(new RecommendedPlaylist());
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new RecommendedPlaylist();
        }
    }

    public void updateFetchRecommendedPlaylist(int quantityOfTracksForPlaylist) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended/new")
                .queryParam("qty", quantityOfTracksForPlaylist)
                .build().encode().toUri();
        try {
            serverConnector.getRestTemplate().put(uri, RecommendedPlaylist.class);
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void changeQuantityOfRecommendedTracks(int quantityOfTracksForPlaylist) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GPSY_API_ROOT + "/playlists/recommended/change")
                .queryParam("qty", quantityOfTracksForPlaylist)
                .build().encode().toUri();
        try {
            serverConnector.getRestTemplate().put(uri, RecommendedPlaylist.class);
        }catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
