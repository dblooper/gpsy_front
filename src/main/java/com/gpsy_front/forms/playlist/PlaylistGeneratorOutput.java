package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.RecommendedPlaylist;
import com.gpsy_front.domain.RecommendedTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;

public class PlaylistGeneratorOutput extends VerticalLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private Grid<RecommendedTrack> playlistTrackGrid = new Grid<>(RecommendedTrack.class);
    private Button button = new Button("Accept and Proceed");
    private Button newPlaylistButton = new Button("Generate new set");
    NumberField numberField = new NumberField("Tracks q-ty");
    private Label gridLabel = new Label("");
    private RestService restService = RestService.getInstance();

    public PlaylistGeneratorOutput() {

        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();

        playlistTrackGrid.setColumns("title", "authors");
        playlistTrackGrid.addComponentColumn(track -> {
            if(track.getSample() != null) {
                Anchor anchor = new Anchor(track.getSample(), "Click");
                anchor.setTarget("tab");
                return anchor;
            } else {
                Anchor anchor = new Anchor("https://www.youtube.com/results?search_query=" + track.getTitle()+ " " + track.getAuthors(), "Search on YT");
                anchor.setTarget("tab");
                return anchor;
            }
        }).setHeader("Try it out");


        numberField.setMin(1);
        numberField.setMax(50);
        numberField.setHasControls(true);

        verticalLayout.add(gridLabel, playlistTrackGrid, numberField, button, newPlaylistButton);
        verticalLayout.addClassName("forms-style");

        RecommendedPlaylist recommendedPlaylist = restService.getRecommendedPlaylist();
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());
        numberField.setValue(recommendedPlaylist.getNumberOfTracks().doubleValue());

        button.addClickListener(event -> updateAndRefresh(numberField.getValue().intValue()));
        newPlaylistButton.addClickListener(event -> generateNewSetOfTracks(numberField.getValue().intValue()));
        add(verticalLayout);
    }

    private void updateAndRefresh(int value) {
        RecommendedPlaylist recommendedPlaylist = restService.changeQuantityOfRecommendedTracks(value);
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
    }

    private void generateNewSetOfTracks(int value) {
        RecommendedPlaylist recommendedPlaylist = restService.updateFetchRecommendedPlaylist(value);
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
    }

}
