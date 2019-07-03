package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PopularTrack  implements ParentTrack {

    private String trackId;

    private String title;

    private String authors;

    private Integer popularity;

    @Override
    public String toString() {
        return "PopularTrack{" +
                "trackId='" + trackId + '\'' +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", popularity=" + popularity +
                '}';
    }
}
