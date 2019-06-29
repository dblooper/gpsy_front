package com.gpsy_front.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaadin.flow.i18n.I18NProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentTrack implements ParentTrack {

    private String trackId;

    private String title;

    private String authors;

    private LocalDateTime playDate;

    public RecentTrack(String trackId, String title, String authors, LocalDateTime playDate) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.playDate = playDate;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public LocalDateTime getPlayDate() {
        return playDate;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPlayDate(LocalDateTime playDate) {
        this.playDate = playDate;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        RecentTrack that = (RecentTrack) o;
//
//        if (!trackId.equals(that.trackId)) return false;
//        if (!title.equals(that.title)) return false;
//        if (!authors.equals(that.authors)) return false;
//        return playDate.equals(that.playDate);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = trackId.hashCode();
//        result = 31 * result + title.hashCode();
//        result = 31 * result + authors.hashCode();
//        result = 31 * result + playDate.hashCode();
//        return result;
//    }

    @Override
    public String toString() {
        return "[ " + title + " ]";
    }
}
