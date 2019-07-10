package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.PlaylistTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlaylistTrackForm extends VerticalLayout {

    private RestService restService = RestService.getInstance();
    private Grid<PlaylistTrack> playlistTrackGrid = new Grid<>(PlaylistTrack.class);
    private VerticalLayout mainContent = new VerticalLayout();
    private HorizontalLayout updateNameArea = new HorizontalLayout();
    private Button updateNameButton = new Button("Update Name");
    private Button deleteButton = new Button("Delete the tracks");
    private TextField name = new TextField("Playlist Name");
    private List<PlaylistTrack> tracks = new ArrayList<>();
    private PlaylistFormTopLayout playlistFormTopLayout;
    private Binder<Playlist> playlistBinder = new Binder<>(Playlist.class);

    public PlaylistTrackForm(PlaylistFormTopLayout playlistFormTopLayout) {
        this.playlistFormTopLayout = playlistFormTopLayout;

        playlistTrackGrid.setColumns("title", "artists");
        playlistTrackGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        playlistTrackGrid.setItems(tracks);
        updateNameArea.add(name, updateNameButton);
        updateNameArea.setAlignItems(Alignment.END);
        deleteButton.addClickListener(event ->{
            deleteTrack();
            playlistTrackGrid.setItems(tracks);
            playlistFormTopLayout.refresh();
        });
        updateNameButton.addClickListener(event -> {
            updatePlaylist();
            playlistFormTopLayout.refresh();
        });

        mainContent.add(updateNameArea, playlistTrackGrid, deleteButton);
        add(mainContent);
        mainContent.addClassName("forms-style");
        mainContent.setMargin(false);
        playlistBinder.bindInstanceFields(this);

        updateNameButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setSizeFull();
        setPadding(false);
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
        Playlist playlist = playlistFormTopLayout.getCurrentPlaylist();
        restService.updatePlaylistDetails(new Playlist(name.getValue(), playlist.getPlaylistStringId(), playlist.getTracks()));
        playlistFormTopLayout.updateInformation(playlist.getName() + " has been updated!");
        Notification.show(playlist.getName() + " has been updated!");
    }

    public void deleteTrack() {
        Set<PlaylistTrack> trackToDelete = playlistTrackGrid.asMultiSelect().getSelectedItems();
        Playlist playlistToUpdate = playlistFormTopLayout.getCurrentPlaylist();
        restService.deleteTrackFromPlaylist(new Playlist(playlistToUpdate.getName(), playlistToUpdate.getPlaylistStringId(), new ArrayList<>(trackToDelete)));
        playlistFormTopLayout.updateInformation("Deleted: " + playlistTrackGrid.asMultiSelect().getSelectedItems().toString());
        Notification.show("Deleted: " + playlistTrackGrid.asMultiSelect().getSelectedItems().toString());
    }
}
