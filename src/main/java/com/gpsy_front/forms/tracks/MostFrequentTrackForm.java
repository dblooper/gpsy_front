package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.MostFrequentTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestSpotifyService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MostFrequentTrackForm extends VerticalLayout implements ParentForm {

    private RestSpotifyService restSpotifyService = RestSpotifyService.getInstance();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistChoseForm playlistChoseForm;
    private Grid<MostFrequentTrack> mostFrequentTracksGrid = new Grid<>(MostFrequentTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most Frequent Tracks");

    public MostFrequentTrackForm(List<Playlist> playlists) {

        playlistChoseForm = new PlaylistChoseForm(this, playlists);

        mostFrequentTracksGrid.setColumns("title", "artists", "popularity");
        mostFrequentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        mostFrequentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            if(!textField.getText().contains("Saved")) {
                textField.setText(("Total selected: " + mostFrequentTracksGrid.getSelectedItems().size()));
            }
            setVisiblePlaylistForm();
        });
        mostFrequentTracksGrid.setItems(restSpotifyService.getFrequentTracksFromApi());
        playlistChoseForm.setVisible(false);
        verticalLayout.add(gridLabel, mostFrequentTracksGrid, textField, playlistChoseForm);


        verticalLayout.setSizeFull();
        add(verticalLayout);

        setSizeFull();
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        verticalLayout.addClassName("forms-style");
        mostFrequentTracksGrid.setHeightByRows(true);

    }

    public int getGridSelectedItemsSize() {
        return this.mostFrequentTracksGrid.getSelectedItems().size();
    }

    private void setVisiblePlaylistForm() {
        if(mostFrequentTracksGrid.asMultiSelect().getSelectedItems().size() == 0) {
            playlistChoseForm.setVisible(false);
        } else {
            playlistChoseForm.setVisible(true);
            textField.setText(("Total selected: " + mostFrequentTracksGrid.getSelectedItems().size()));
        }
    }

    @Override
    public Grid<?> getGrid() {
        return this.mostFrequentTracksGrid;
    }

    @Override
    public void saveAllToSpotify() {
        Set<ParentTrack> parentTracks = mostFrequentTracksGrid.asMultiSelect().getSelectedItems().stream()
                .map(track -> (ParentTrack)track)
                .collect(Collectors.toSet());
        restSpotifyService.updatePlaylistWithPopularTrack(playlistChoseForm.getCurrentPlaylist(), parentTracks);
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }
}