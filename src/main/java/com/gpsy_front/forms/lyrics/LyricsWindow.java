package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.*;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class LyricsWindow extends VerticalLayout {

    private RestService restService = RestService.getInstance();
    private VerticalLayout mainContent = new VerticalLayout();
    private MostFrequentTrack track;
    private PopularTracksWithLyrics popularTracksWithLyrics;
    private TextArea textArea;

    public LyricsWindow(PopularTracksWithLyrics popularTracksWithLyrics) {
        this.popularTracksWithLyrics = popularTracksWithLyrics;
        this.track = new MostFrequentTrack("n/a", "n/a", "n/a", 0);
        this.textArea = new TextArea("Waiting for track to search lyrics");

        mainContent.add(textArea);
        textArea.setSizeFull();

        add(mainContent);
        mainContent.setWidth("60%");
        mainContent.addClassName("forms-style");

    }

    public void setTrack(MostFrequentTrack mostFrequentTrack) {

        if(mostFrequentTrack != null) {
            this.track = mostFrequentTrack;
            LyricsDto lyricsDto = restService.fetchLyrics(mostFrequentTrack.getTitle(), mostFrequentTrack.getAuthors());
//            LyricsDto lyricsDto = restService.fetchLyrics("rolling", "adele");
            textArea.setValue(lyricsDto.getLyrics());
            textArea.setLabel("Title: " + track.getTitle() + " | Author: " + track.getAuthors());
        }else {
            textArea.setLabel("Title: n/a | Author: n/a");
            textArea.setValue("Waiting for track to search lyrics");
        }
    }
}
