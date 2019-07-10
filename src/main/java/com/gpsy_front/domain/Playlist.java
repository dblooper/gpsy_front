package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.vaadin.flow.i18n.I18NProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@NoArgsConstructor
@Getter
@Setter
public class Playlist {

    @JsonProperty("name")
    private String name;

    @JsonProperty("playlistStringId")
    private String playlistStringId;

    @JsonUnwrapped
    @JsonProperty("tracks")
    private List<PlaylistTrack> tracks;

    @JsonProperty("quantityOfTracks")
    private int quantityOfTracks;

    public Playlist(String name, String playlistStringId, List<PlaylistTrack> tracks) {
        this.name = name;
        this.playlistStringId = playlistStringId;
        this.tracks = tracks;
    }

    public void countTracks() {
        quantityOfTracks = tracks.size();
    }


}
