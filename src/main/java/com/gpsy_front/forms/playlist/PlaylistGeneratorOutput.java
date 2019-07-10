package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.RecommendedPlaylist;
import com.gpsy_front.domain.RecommendedTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;

public class PlaylistGeneratorOutput extends VerticalLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private Grid<RecommendedTrack> playlistTrackGrid = new Grid<>(RecommendedTrack.class);
    private Button updateButton = new Button("Reduce tracks");
    private Button newPlaylistTracksButton = new Button("Generate new set");
    NumberField numberField = new NumberField("Tracks quantity");
    private Label gridLabel = new Label("");
    private RestService restService = RestService.getInstance();

    public PlaylistGeneratorOutput() {

        gridLabel.setClassName("grid-title");

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
        horizontalLayout.setAlignItems(Alignment.END);
        verticalLayout.add(gridLabel, playlistTrackGrid, horizontalLayout, newPlaylistTracksButton);
        verticalLayout.addClassName("forms-style");

        RecommendedPlaylist recommendedPlaylist = restService.getRecommendedPlaylist();
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());

        numberField.setMin(1);
        numberField.setMax(50);
        numberField.setHasControls(true);
        numberField.setValue(recommendedPlaylist.getNumberOfTracks().doubleValue());
        numberField.addValueChangeListener(event -> setVisibleButton());

        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newPlaylistTracksButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.setVisible(false);

        updateButton.addClickListener(event -> updateAndRefresh(numberField.getValue().intValue()));
        newPlaylistTracksButton.addClickListener(event -> generateNewSetOfTracks(numberField.getValue().intValue()));
        add(verticalLayout);
    }

    private void updateAndRefresh(int value) {
        RecommendedPlaylist recommendedPlaylist = restService.changeQuantityOfRecommendedTracks(value);
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
        updateButton.setVisible(false);
    }

    private void generateNewSetOfTracks(int value) {
        RecommendedPlaylist recommendedPlaylist = restService.updateFetchRecommendedPlaylist(value);
        playlistTrackGrid.setItems(recommendedPlaylist.getPlaylistTracks());
        gridLabel.setText("Forecast for: " + recommendedPlaylist.getName() + " | Total: " + recommendedPlaylist.getNumberOfTracks());
    }

    void setVisibleButton() {
        updateButton.setVisible(true);
    }

}
