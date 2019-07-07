package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.*;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;


public class LyricsWindow extends VerticalLayout {

    private RestService restService = RestService.getInstance();
    private VerticalLayout mainContent = new VerticalLayout();
    private VerticalLayout searchLayoutWithLabel = new VerticalLayout();
    private HorizontalLayout searchForm = new HorizontalLayout();
    private MostFrequentTrack track;
    private LyricsAndLibraryTopLayout lyricsAndLibraryTopLayout;
    private TextField title = new TextField("Title:");
    private TextField author = new TextField("Author:");
    private Button searchButton = new Button("Search");
    private TextArea textArea;
    private Text titleLabel = new Text("");
    private Text authorLabel = new Text("");
    private Label lyriicsTextLabel = new Label("Lyrics");
    private Label searchLyricsLabel = new Label("Search Lyrics");


    public LyricsWindow(LyricsAndLibraryTopLayout lyricsAndLibraryTopLayout) {
        this.lyricsAndLibraryTopLayout = lyricsAndLibraryTopLayout;
        this.track = new MostFrequentTrack("n/a", "n/a", "n/a", 0);
        this.textArea = new TextArea("Waiting for track to search lyrics");
        title.setPlaceholder("Type title");
        author.setPlaceholder("Type author");
        searchButton.addClickListener(event -> searchTrackByTyping());
        searchForm.add(searchLyricsLabel, title, author, searchButton);
        searchLyricsLabel.setClassName("grid-title");
        searchLyricsLabel.setSizeFull();

        searchForm.setAlignItems(Alignment.END);
        searchForm.setPadding(true);
        searchForm.setMargin(false);
        searchForm.setWidthFull();

        title.setMinLength(3);
        author.setMinLength(4);

        searchLayoutWithLabel.add(searchLyricsLabel, searchForm);
        searchLayoutWithLabel.setClassName("forms-style");
        searchLayoutWithLabel.setSizeFull();

        textArea.setSizeFull();
        mainContent.add(lyriicsTextLabel, textArea);
        lyriicsTextLabel.setClassName("grid-title");
        lyriicsTextLabel.setSizeFull();

        mainContent.setSizeFull();
        mainContent.addClassName("forms-style");
        add(searchLayoutWithLabel, mainContent);
        setPadding(false);
        setWidth("70%");
    }

    public void setTrack(MostFrequentTrack mostFrequentTrack) {
        if(mostFrequentTrack != null) {
            this.track = mostFrequentTrack;
            LyricsDto lyricsDto = restService.getLyrics(mostFrequentTrack.getTitle(), mostFrequentTrack.getAuthors());
//            LyricsDto lyricsDto = restService.getLyrics("rolling", "adele");
            textArea.setValue(lyricsDto.getLyrics());
            titleLabel.setText(track.getTitle());
            authorLabel.setText(track.getAuthors());
            textArea.setLabel("Title: " + titleLabel.getText() + " | Author: " + authorLabel.getText());
        }else {
            textArea.setLabel("Title: n/a | Author: n/a");
            textArea.setValue("Waiting for track to search lyrics");
        }
    }
    private void searchTrackByTyping() {

        if(!title.getValue().isEmpty() && !author.getValue().isEmpty()
            && !(title.getValue().length() < 3)
            && !(author.getValue().length() < 3)) {

            LyricsDto lyricsDto = restService.getLyrics(title.getValue(), author.getValue());
            titleLabel.setText(title.getValue());
            authorLabel.setText(author.getValue());

            textArea.setValue(lyricsDto.getLyrics());
            textArea.setLabel("Title: " + titleLabel.getText() + " | Author: " + authorLabel.getText());
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

    public LyricsDto getCurrentLyrics() {
        if(textArea.getValue().length() > 0) {
            return new LyricsDto(titleLabel.getText(), authorLabel.getText(), textArea.getValue());
        }
        return new LyricsDto("n/a", "n/a", "n/a");
    }

    public void setLyrics(LyricsDto lyricsDto) {
        if(lyricsDto != null) {
            textArea.setValue(lyricsDto.getLyrics());
            titleLabel.setText(lyricsDto.getTitle());
            authorLabel.setText(lyricsDto.getArtist());
            textArea.setLabel("Title: " + titleLabel.getText() + " | Author: " + authorLabel.getText());
        }else {
            textArea.setLabel("Title: n/a | Author: n/a");
            textArea.setValue("Waiting for track to search lyrics");
        }
    }

    public TextArea getLyricsTextArea() {
        return this.textArea;
    }
}
