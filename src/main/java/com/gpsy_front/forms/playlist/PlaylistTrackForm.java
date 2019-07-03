package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.PlaylistTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;

public class PlaylistTrackForm extends VerticalLayout {

    private RestService restService = RestService.getInstance();
    private Grid<PlaylistTrack> playlistTrackGrid = new Grid<>(PlaylistTrack.class);
    private VerticalLayout mainContent = new VerticalLayout();
    private HorizontalLayout updateNameArea = new HorizontalLayout();
    private Button updateNameButton = new Button("Update Name");
    private Button deleteButton = new Button("Delete the tracks");
    private TextField name = new TextField("Playlist Name");
    private List<PlaylistTrack> tracks = new ArrayList<>();
    private PlaylistForm playlistForm;
    private Binder<Playlist> playlistBinder = new Binder<>(Playlist.class);

    public PlaylistTrackForm(PlaylistForm playlistForm) {
        this.playlistForm = playlistForm;

        playlistTrackGrid.setColumns("title", "authors");
        playlistTrackGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        playlistTrackGrid.setItems(tracks);
        updateNameArea.add(name, updateNameButton);
        updateNameArea.setAlignItems(Alignment.END);
        deleteButton.addClickListener(event ->{
            deleteTrack();
            playlistTrackGrid.setItems(tracks);
            playlistForm.refresh();
        });
        updateNameButton.addClickListener(event -> {
            updatePlaylist();
            playlistForm.refresh();
        });

        mainContent.add(updateNameArea, playlistTrackGrid, deleteButton);
        add(mainContent);
        mainContent.addClassName("forms-style");
        mainContent.setMargin(false);
        playlistBinder.bindInstanceFields(this);

        setSizeFull();
    }

    public void setTracks(List<PlaylistTrack> playlistTracks) {
        this.tracks = playlistTracks;
        playlistTrackGrid.setItems(tracks);
    }

    public void setPlaylist(Playlist playlist) {

        playlistBinder.setBean(playlist);

        if(playlist == null) {
            setVisible(false);
        }else {
            setTracks(playlist.getTracks());
            setVisible(true);
        }
    }

    void updatePlaylist() {
        Playlist playlist = playlistForm.getCurrentPlaylist();
        restService.updatePlaylistDetails(new Playlist(name.getValue(), playlist.getPlaylistStringId(), playlist.getTracks()));
    }

    public void deleteTrack() {
        restService.deleteTrackFromPlaylist(playlistForm.getCurrentPlaylist(), playlistTrackGrid.asMultiSelect().getSelectedItems());
    }
}
