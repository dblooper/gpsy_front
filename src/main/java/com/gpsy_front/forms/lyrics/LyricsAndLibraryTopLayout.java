package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.service.RestSpotifyService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LyricsAndLibraryTopLayout extends FormLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private VerticalLayout lyricsForm = new VerticalLayout();
    private Grid<RecentTrack> userRecentTrack = new Grid<>(RecentTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most recent songs");
    private RestSpotifyService restSpotifyService = RestSpotifyService.getInstance();
    private LyricsWindow lyricsWindow = new LyricsWindow(this);
    private LyricsLibraryForm libraryForm = new LyricsLibraryForm(lyricsWindow);

    public LyricsAndLibraryTopLayout() {

        userRecentTrack.setColumns("title", "artists", "playDate");
        userRecentTrack.setSelectionMode(Grid.SelectionMode.SINGLE);
        lyricsForm.add(gridLabel, userRecentTrack, textField);
        verticalLayout.add(libraryForm, lyricsForm);

        verticalLayout.setSizeFull();
        userRecentTrack.setItems(restSpotifyService.getRecentTracksFromApi());

        userRecentTrack.asSingleSelect().addValueChangeListener(event -> lyricsWindow.setTrack(userRecentTrack.asSingleSelect().getValue()));

        add(verticalLayout, lyricsWindow);


        setSizeFull();
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        verticalLayout.setPadding(false);
        libraryForm.addClassName("forms-style");
        lyricsForm.addClassName("forms-style");
    }
}
