package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.RecommendedPlaylist;
import com.gpsy_front.domain.RecommendedTrack;
import com.gpsy_front.service.RestSpotifyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;

public class PlaylistGeneratorOutput extends VerticalLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private Grid<RecommendedTrack> playlistTrackGrid = new Grid<>(RecommendedTrack.class);
    private Button updateButton = new Button("Reduce tracks");
    private Button newPlaylistTracksButton = new Button("Generate new set");
    NumberField numberField = new NumberField("Tracks quantity");
    private Label gridLabel = new Label("");
    private RestSpotifyService restSpotifyService = RestSpotifyService.getInstance();
    private int initialGeneratedPlaylistTracksValue;

    public PlaylistGeneratorOutput() {

        playlistTrackGrid.setColumns("title", "artists");
        playlistTrackGrid.addComponentColumn(track -> {
            if(track.getSample() != null) {
                Anchor anchor = new Anchor(track.getSample(), "Click");
                anchor.setTarget("tab");
                return anchor;
            } else {
                Anchor anchor = new Anchor("https://www.youtube.com/results?search_query=" + track.getTitle()+ " " + track.getArtists(), "Search on YT");
                anchor.setTarget("tab");
                return anchor;
            }
        }).setHeader("Try it out");

        horizontalLayout.add(numberField, updateButton);
        verticalLayout.add(gridLabel, playlistTrackGrid, horizontalLayout, newPlaylistTracksButton);

        RecommendedPlaylist recommendedPlaylist = restSpotifyService.getRecommendedPlaylist();
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());

        numberField.setMin(1);
        numberField.setMax(50);
        numberField.setHasControls(true);

        numberField.setValue(recommendedPlaylist.getNumberOfTracks().doubleValue());
        numberField.addValueChangeListener(event -> {
            int value = numberField.getValue().intValue();
            if(value < initialGeneratedPlaylistTracksValue)
            setVisibleButton();
        });

        initialGeneratedPlaylistTracksValue = numberField.getValue().intValue();

        updateButton.setVisible(false);
        updateButton.addClickListener(event -> updateAndRefresh(numberField.getValue().intValue()));
        newPlaylistTracksButton.addClickListener(event -> generateNewSetOfTracks(numberField.getValue().intValue()));
        add(verticalLayout);

        gridLabel.setClassName("grid-title");
        gridLabel.setWidthFull();
        verticalLayout.addClassName("forms-style");
        horizontalLayout.setAlignItems(Alignment.END);
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newPlaylistTracksButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    }

    private void updateAndRefresh(int value) {
        restSpotifyService.changeQuantityOfRecommendedTracks(value);
        RecommendedPlaylist recommendedPlaylist = restSpotifyService.getRecommendedPlaylist();
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
        updateButton.setVisible(false);
        initialGeneratedPlaylistTracksValue = numberField.getValue().intValue();
    }

    private void generateNewSetOfTracks(int value) {
        restSpotifyService.updateFetchRecommendedPlaylist(value);
        RecommendedPlaylist recommendedPlaylist = restSpotifyService.getRecommendedPlaylist();
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
        updateButton.setVisible(false);
        initialGeneratedPlaylistTracksValue = numberField.getValue().intValue();
    }

    private void setVisibleButton() {
        updateButton.setVisible(true);
    }

}
