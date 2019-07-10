package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MostFrequentTrack implements ParentTrack {

    @JsonProperty(value = "trackStringId")
    private String trackStringId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "artists")
    private String artists;

    @JsonProperty(value = "popularity")
    private Integer popularity;
}
