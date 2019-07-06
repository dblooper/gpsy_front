package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyricsLibrary {

    @JsonProperty(value = "libraryName")
    private String libraryName;

    @JsonProperty(value = "lyrics")
    private List<LyricsDto> lyrics = new ArrayList<>();

    @JsonProperty(value = "libraryCode")
    private String libraryCode;

    public LyricsLibrary(String libraryName, List<LyricsDto> lyrics) {
        this.libraryName = libraryName;
        this.lyrics = lyrics;
    }
}
