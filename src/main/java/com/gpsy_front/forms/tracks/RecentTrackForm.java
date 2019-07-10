package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class RecentTrackForm extends VerticalLayout implements ParentForm {

    private RestService restService = RestService.getInstance();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistChoseForm playlistChoseForm;
    private Grid<RecentTrack> recentTracksGrid = new Grid<>(RecentTrack.class);
    private Text textField = new Text("No track chosen");
    private Label gridLabel = new Label("Recently played");

        public RecentTrackForm(List<Playlist> playlists) {
            this.playlistChoseForm = new PlaylistChoseForm(this, playlists);
            gridLabel.setClassName("grid-title");
            gridLabel.setSizeFull();
            recentTracksGrid.setColumns("title", "artists", "playDate");
            recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
                textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
                setVisiblePlaylistForm();
            });
            recentTracksGrid.setItems(restService.getRecentTracksFromApi());
            playlistChoseForm.setVisible(false);
            verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistChoseForm);

            verticalLayout.addClassName("forms-style");
            verticalLayout.setSizeFull();
            add(verticalLayout);
            setSizeFull();
            recentTracksGrid.setHeightByRows(true);
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

    @Override
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
