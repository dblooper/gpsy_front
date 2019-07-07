package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.MostFrequentTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LyricsAndLibraryTopLayout extends FormLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private VerticalLayout popularTracksLayout = new VerticalLayout();
    private Grid<MostFrequentTrack> userPopularTrack = new Grid<>(MostFrequentTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most frequent songs");
    private RestService restService = RestService.getInstance();
    private LyricsWindow lyricsWindow = new LyricsWindow(this);
    private LyricsLibraryForm lyricsLibraryForm = new LyricsLibraryForm(lyricsWindow);

    public LyricsAndLibraryTopLayout() {
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        userPopularTrack.setColumns("title", "authors", "popularity");
        userPopularTrack.setSelectionMode(Grid.SelectionMode.SINGLE);
        popularTracksLayout.add(gridLabel, userPopularTrack, textField);
        verticalLayout.add(lyricsLibraryForm, popularTracksLayout);

        lyricsLibraryForm.addClassName("forms-style");
        popularTracksLayout.addClassName("forms-style");
        verticalLayout.setSizeFull();
        userPopularTrack.setItems(restService.getFrequentTracksFromApi());

        userPopularTrack.asSingleSelect().addValueChangeListener(event -> lyricsWindow.setTrack(userPopularTrack.asSingleSelect().getValue()));

        add(verticalLayout, lyricsWindow);
    }
}
