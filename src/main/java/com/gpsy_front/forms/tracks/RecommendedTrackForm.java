package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.RecommendedTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecommendedTrackForm extends VerticalLayout implements ParentForm {

    private RestService restService = RestService.getInstance();
    private PlaylistChoseForm playlistChoseForm;
    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private Grid<RecommendedTrack> recommendedTracksGrid = new Grid<>(RecommendedTrack.class);
    private Text textField = new Text("No track chosen");
    private Label gridLabel = new Label("Recommended Tracks");
    private Button refreshButton = new Button("Refresh");

    public RecommendedTrackForm(List<Playlist> playlists) {
        this.playlistChoseForm = new PlaylistChoseForm(this, playlists);
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();

        horizontalLayout.add(gridLabel, refreshButton);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setSizeFull();
        recommendedTracksGrid.setColumns("title", "artists");
        recommendedTracksGrid.addComponentColumn(track -> {
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

        recommendedTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        recommendedTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            textField.setText(("Total selected: " + recommendedTracksGrid.getSelectedItems().size()));
            setVisiblePlaylistForm();
        });
        recommendedTracksGrid.setItems(restService.getRecommendedTrcksFromApi());

        refreshButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        refreshButton.addClickListener(event ->
            recommendedTracksGrid.setItems(restService.getRecommendedTrcksFromApi()));

        playlistChoseForm.setVisible(false);
        verticalLayout.add(horizontalLayout, recommendedTracksGrid, textField, playlistChoseForm);
        verticalLayout.addClassName("forms-style");
        verticalLayout.setSizeFull();
        add(verticalLayout);
        setSizeFull();
        recommendedTracksGrid.setHeightByRows(true);
    }

    private void setVisiblePlaylistForm() {
        if(recommendedTracksGrid.asMultiSelect().getSelectedItems().size() == 0) {
            playlistChoseForm.setVisible(false);
        } else {
            playlistChoseForm.setVisible(true);
        }
    }

    @Override
    public Grid<?> getGrid() {
        return this.recommendedTracksGrid;
    }

    public void saveAllToSpotify() {
        Set<ParentTrack> parentTracks = recommendedTracksGrid.asMultiSelect().getSelectedItems().stream()
                .map(track -> (ParentTrack)track)
                .collect(Collectors.toSet());
        restService.updatePlaylistWithPopularTrack(playlistChoseForm.getCurrentPlaylist(), parentTracks);
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }
}
