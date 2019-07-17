package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistTrack {

    @JsonProperty("trackStringId")
    private String trackStringId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artists")
    private String artists;

    public PlaylistTrack(String trackStringId, String title, String artists) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
    }

    @Override
    public String toString() {
        return title + ", " + artists;
    }
}
