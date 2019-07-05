package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.RecommendedTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.forms.playlist.PlaylistChoseForm;
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
    private Grid<RecommendedTrack> recentTracksGrid = new Grid<>(RecommendedTrack.class);
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
        recentTracksGrid.setColumns("title", "authors");
        recentTracksGrid.addComponentColumn(track -> {
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
        recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
            setVisiblePlaylistForm();
        });
        recentTracksGrid.setItems(restService.getRecommendedTrcksFromApi());

        refreshButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        refreshButton.addClickListener(event ->
            recentTracksGrid.setItems(restService.getRecommendedTrcksFromApi()));

        playlistChoseForm.setVisible(false);
        verticalLayout.add(horizontalLayout, recentTracksGrid, textField, playlistChoseForm);
        verticalLayout.addClassName("forms-style");
        verticalLayout.setSizeFull();
        add(verticalLayout);
        setSizeFull();
    }

    private void setVisiblePlaylistForm() {
        if(recentTracksGrid.asMultiSelect().getSelectedItems().size() == 0) {
            playlistChoseForm.setVisible(false);
        } else {
            playlistChoseForm.setVisible(true);
        }
    }

    @Override
    public Grid<?> getGrid() {
        return this.recentTracksGrid;
    }

    public void saveAllToSpotify() {
        Set<ParentTrack> parentTracks = recentTracksGrid.asMultiSelect().getSelectedItems().stream()
                .map(track -> (ParentTrack)track)
                .collect(Collectors.toSet());
        restService.updatePlaylistWithPopularTrack(playlistChoseForm.getCurrentPlaylist(), parentTracks);
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }
}