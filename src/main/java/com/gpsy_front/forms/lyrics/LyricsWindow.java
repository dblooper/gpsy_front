package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.*;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;


public class LyricsWindow extends VerticalLayout {

    private RestService restService = RestService.getInstance();
    private VerticalLayout mainContent = new VerticalLayout();
    private HorizontalLayout searchForm = new HorizontalLayout();
    private MostFrequentTrack track;
    private PopularTracksWithLyrics popularTracksWithLyrics;
    private TextField title = new TextField("Title:");
    private TextField author = new TextField("Author:");
    private Button searchButton = new Button("Search");
    private TextArea textArea;

    public LyricsWindow(PopularTracksWithLyrics popularTracksWithLyrics) {
        this.popularTracksWithLyrics = popularTracksWithLyrics;
        this.track = new MostFrequentTrack("n/a", "n/a", "n/a", 0);
        this.textArea = new TextArea("Waiting for track to search lyrics");
        title.setPlaceholder("Type title");
        author.setPlaceholder("Type author");
        searchButton.addClickListener(event -> searchTrackByTyping());
        searchForm.add(title, author, searchButton);
        searchForm.setAlignItems(Alignment.END);
        searchForm.setClassName("forms-style");
        searchForm.setPadding(true);
        searchForm.setMargin(false);
        searchForm.setWidth("70%");

        title.setMinLength(3);
        author.setMinLength(4);

        textArea.setSizeFull();
        mainContent.add(textArea);

        mainContent.setWidth("60%");
        mainContent.addClassName("forms-style");
        add(searchForm, mainContent);
        setPadding(false);
    }

    public void setTrack(MostFrequentTrack mostFrequentTrack) {
        FormLayout formLayout = new FormLayout();
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
    private void searchTrackByTyping() {

        if(!title.getValue().isEmpty() && !author.getValue().isEmpty()
            && !(title.getValue().length() < 3)
            && !(author.getValue().length() < 3)) {

            LyricsDto lyricsDto = restService.fetchLyrics(title.getValue(), author.getValue());
            textArea.setValue(lyricsDto.getLyrics());
            textArea.setLabel("Title: " + title.getValue() + " | Author: " + author.getValue());
        }

        if(title.isEmpty()) {
            title.setInvalid(true);
            title.setErrorMessage("Missed title");
        }else if(author.isEmpty()) {
            author.setInvalid(true);
            author.setErrorMessage("Missed author");
        }else if(title.getValue().length() < 3 ) {
            title.setInvalid(true);
            title.setErrorMessage("Too short");
        }else if(author.getValue().length() < 3) {
            author.setInvalid(true);
            author.setErrorMessage("Too short");
        }
    }
}
