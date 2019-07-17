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
public class RecommendedTrack implements ParentTrack{

    @JsonProperty("trackStringId")
    private String trackStringId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artists")
    private String artists;

    @JsonProperty("sample")
    private String sample;

//    private Anchor link;

}
