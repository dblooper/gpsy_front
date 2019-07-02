package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyricsDto {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "artist")
    private String artist;

    @JsonProperty(value = "lyrics")
    private String lyrics;
}
