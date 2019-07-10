package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendedPlaylist {

    @JsonProperty(value = "playlistStringId")
    private String playlistStringId;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "playlistTracks")
    private List<RecommendedTrack> playlistTracks;

    @JsonProperty(value = "numberOfTracks")
    private Integer numberOfTracks;

    @JsonProperty(value = "actual")
    private boolean actual;
}
