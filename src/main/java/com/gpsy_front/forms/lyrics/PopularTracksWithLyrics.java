package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.MostFrequentTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PopularTracksWithLyrics extends FormLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private Grid<MostFrequentTrack> userPopularTrack = new Grid<>(MostFrequentTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most frequent songs");
    private RestService restService = RestService.getInstance();
    private LyricsWindow lyricsWindow = new LyricsWindow(this);

    public PopularTracksWithLyrics() {
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        userPopularTrack.setColumns("title", "authors", "popularity");
        userPopularTrack.setSelectionMode(Grid.SelectionMode.SINGLE);

        verticalLayout.add(gridLabel, userPopularTrack, textField);
        verticalLayout.addClassName("forms-style");
        verticalLayout.setSizeFull();
        userPopularTrack.setItems(restService.getFrequentTracksFromApi());

        userPopularTrack.asSingleSelect().addValueChangeListener(event -> lyricsWindow.setTrack(userPopularTrack.asSingleSelect().getValue()));

        add(verticalLayout, lyricsWindow);

    }
}