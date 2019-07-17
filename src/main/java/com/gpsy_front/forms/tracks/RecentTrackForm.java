package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestSpotifyService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class RecentTrackForm extends VerticalLayout implements ParentForm {

    private RestSpotifyService restSpotifyService = RestSpotifyService.getInstance();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistChoseForm playlistChoseForm;
    private Grid<RecentTrack> recentTracksGrid = new Grid<>(RecentTrack.class);
    private Text textField = new Text("No track chosen");
    private Label gridLabel = new Label("Recently played");

        public RecentTrackForm(List<Playlist> playlists) {

            this.playlistChoseForm = new PlaylistChoseForm(this, playlists);

            recentTracksGrid.setColumns("title", "artists", "playDate");
            recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
                if(!textField.getText().contains("Saved")) {
                    textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
                }
                setVisiblePlaylistForm();
            });
            recentTracksGrid.setItems(restSpotifyService.getRecentTracksFromApi());
            playlistChoseForm.setVisible(false);
            verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistChoseForm);

            add(verticalLayout);
            setSizeFull();
            gridLabel.setClassName("grid-title");
            gridLabel.setSizeFull();
            verticalLayout.addClassName("forms-style");
            verticalLayout.setSizeFull();
//            recentTracksGrid.setHeightByRows(true);
        }

        private void setVisiblePlaylistForm() {
            if(recentTracksGrid.asMultiSelect().getSelectedItems().size() == 0) {
                playlistChoseForm.setVisible(false);
            } else {
                playlistChoseForm.setVisible(true);
                textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
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
        restSpotifyService.updatePlaylistWithPopularTrack(playlistChoseForm.getCurrentPlaylist(), parentTracks);
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }

}
