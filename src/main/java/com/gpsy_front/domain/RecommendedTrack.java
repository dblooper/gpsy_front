package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.flow.component.html.Anchor;
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
public class RecommendedTrack {

    @JsonProperty("stringId")
    private String stringId;

    @JsonProperty("titles")
    private String titles;

    @JsonProperty("authors")
    private String authors;

    @JsonProperty("sample")
    private String sample;

//    private Anchor link;

}
