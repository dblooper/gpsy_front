package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PlaylistForm extends VerticalLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private Grid<Playlist> userPlaylistsGrid = new Grid<>(Playlist.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Your current playlists");
    private RestService restService = RestService.getInstance();
    private PlaylistTrackForm playlistTrackForm = new PlaylistTrackForm(this);

    public PlaylistForm() {
        gridLabel.setClassName("grid-title");
        userPlaylistsGrid.setColumns("name", "quantityOfTracks");
        userPlaylistsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        verticalLayout.add(gridLabel, userPlaylistsGrid, textField);
        verticalLayout.addClassName("forms-style");

        HorizontalLayout theMainContent = new HorizontalLayout(verticalLayout, playlistTrackForm);
        playlistTrackForm.setSizeFull();
        playlistTrackForm.setMargin(false);
        theMainContent.setSizeFull();
        playlistTrackForm.setPlaylist(null);
        userPlaylistsGrid.setItems(restService.getPlaylistsFromApi());

        userPlaylistsGrid.asSingleSelect().addValueChangeListener(event -> playlistTrackForm.setPlaylist(userPlaylistsGrid.asSingleSelect().getValue()));
        add(theMainContent);
        setSizeFull();
    }

    public Playlist getCurrentPlaylist() {
        return userPlaylistsGrid.asSingleSelect().getValue();
    }

    public void refresh() {
        userPlaylistsGrid.setItems(restService.getPlaylistsFromApi());
    }
}
