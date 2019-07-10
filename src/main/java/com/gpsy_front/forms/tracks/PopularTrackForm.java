package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.PopularTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PopularTrackForm extends VerticalLayout implements ParentForm {

    private RestService restService = RestService.getInstance();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistChoseForm playlistChoseForm;
    private Grid<PopularTrack> popularTracksGrid = new Grid<>(PopularTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most Popular Tracks acc. to Spotify");

    public PopularTrackForm(List<Playlist> playlists) {

        playlistChoseForm = new PlaylistChoseForm(this, playlists);
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();

        popularTracksGrid.setColumns("title", "artists", "popularity");
        popularTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        popularTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            textField.setText(("Total selected: " + popularTracksGrid.getSelectedItems().size()));
            setVisiblePlaylistForm();
        });
        popularTracksGrid.setItems(restService.getPopularTracks());
        playlistChoseForm.setVisible(false);
        verticalLayout.add(gridLabel, popularTracksGrid, textField, playlistChoseForm);

        verticalLayout.addClassName("forms-style");
        verticalLayout.setSizeFull();
        add(verticalLayout);
        setSizeFull();
        popularTracksGrid.setHeightByRows(true);
    }

    private void setVisiblePlaylistForm() {
        if(popularTracksGrid.asMultiSelect().getSelectedItems().size() == 0) {
            playlistChoseForm.setVisible(false);
        } else {
            playlistChoseForm.setVisible(true);
        }
    }

    @Override
    public Grid<?> getGrid() {
        return this.popularTracksGrid;
    }

    @Override
    public void saveAllToSpotify() {
        Set<ParentTrack> parentTracks = popularTracksGrid.asMultiSelect().getSelectedItems().stream()
                .map(track -> (ParentTrack)track)
                .collect(Collectors.toSet());
        restService.updatePlaylistWithPopularTrack(playlistChoseForm.getCurrentPlaylist(), parentTracks);
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }
}

