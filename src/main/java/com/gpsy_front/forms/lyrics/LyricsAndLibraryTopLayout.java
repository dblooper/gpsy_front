package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.MostFrequentTrack;
import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LyricsAndLibraryTopLayout extends FormLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private VerticalLayout popularTracksLayout = new VerticalLayout();
    private Grid<RecentTrack> userRecentTrack = new Grid<>(RecentTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most recent songs");
    private RestService restService = RestService.getInstance();
    private LyricsWindow lyricsWindow = new LyricsWindow(this);
    private LyricsLibraryForm lyricsLibraryForm = new LyricsLibraryForm(lyricsWindow);

    public LyricsAndLibraryTopLayout() {
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        userRecentTrack.setColumns("title", "artists", "playDate");
        userRecentTrack.setSelectionMode(Grid.SelectionMode.SINGLE);
        popularTracksLayout.add(gridLabel, userRecentTrack, textField);
        verticalLayout.add(lyricsLibraryForm, popularTracksLayout);

        lyricsLibraryForm.addClassName("forms-style");
        popularTracksLayout.addClassName("forms-style");
        verticalLayout.setSizeFull();
        userRecentTrack.setItems(restService.getRecentTracksFromApi());

        userRecentTrack.asSingleSelect().addValueChangeListener(event -> lyricsWindow.setTrack(userRecentTrack.asSingleSelect().getValue()));

        add(verticalLayout, lyricsWindow);
        verticalLayout.setPadding(false);
        setSizeFull();
    }
}
