package com.gpsy_front.domain;

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
public class PopularTrack {

    private String trackId;

    private String title;

    private String authors;

    private Integer popularity;
}
