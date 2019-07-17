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
public class RecentTrack implements ParentTrack {

    @JsonProperty(value = "trackStringId")
    private String trackStringId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "artists")
    private String artists;

    @JsonProperty(value = "playDate")
    private String playDate;

    public RecentTrack(String trackStringId, String title, String artists, String playDate) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.playDate = playDate;
    }

    @Override
    public String toString() {
        return "[ " + title + " ]";
    }
}
