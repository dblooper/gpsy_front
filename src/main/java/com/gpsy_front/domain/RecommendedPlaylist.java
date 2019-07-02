package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private String playlistStringId;

    private String name;

    private Integer numberOfTracks;

    private boolean actual;

    private List<RecommendedTrack> playlistTracks = new ArrayList<>();
}
