package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.MostFrequentTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MostFrequentTrackForm extends VerticalLayout implements ParentForm {

    private RestService restService = RestService.getInstance();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistChoseForm playlistChoseForm;
    private Grid<MostFrequentTrack> recentTracksGrid = new Grid<>(MostFrequentTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most Frequent Tracks");

    public MostFrequentTrackForm(List<Playlist> playlists) {

        playlistChoseForm = new PlaylistChoseForm(this, playlists);
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();

        recentTracksGrid.setColumns("title", "artists", "popularity");
        recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            if(!textField.getText().contains("Saved")) {
                textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
            }
            setVisiblePlaylistForm();
        });
        recentTracksGrid.setItems(restService.getFrequentTracksFromApi());
        playlistChoseForm.setVisible(false);
        verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistChoseForm);

        verticalLayout.addClassName("forms-style");
        verticalLayout.setSizeFull();
        add(verticalLayout);
        setSizeFull();
        recentTracksGrid.setHeightByRows(true);

    }

    public int getGridSelectedItemsSize() {
        return this.recentTracksGrid.getSelectedItems().size();
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
        restService.updatePlaylistWithPopularTrack(playlistChoseForm.getCurrentPlaylist(), parentTracks);
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }
}