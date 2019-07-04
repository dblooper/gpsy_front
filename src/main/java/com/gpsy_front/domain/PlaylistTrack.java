package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
//@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistTrack {

//    @JsonProperty("trackStringId")
    private String trackStringId;

//    @JsonProperty("title")
    private String title;

//    @JsonProperty("authors")
    private String authors;

    public PlaylistTrack(String trackStringId, String title, String authors) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                '}';
    }
}
