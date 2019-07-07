package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PlaylistFormTopLayout extends VerticalLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private VerticalLayout playlistContent = new VerticalLayout();
    HorizontalLayout theMainContent = new HorizontalLayout();
    private Grid<Playlist> userPlaylistsGrid = new Grid<>(Playlist.class);
    private Text textField = new Text("No playlist chosen");
    private Label gridLabel = new Label("Playlists on your spotify");
    private RestService restService = RestService.getInstance();
    private PlaylistTrackForm playlistTrackForm = new PlaylistTrackForm(this);
    private CreatePlaylistForm createPlaylistForm = new CreatePlaylistForm(this);

    public PlaylistFormTopLayout() {
        gridLabel.setClassName("grid-title");
        userPlaylistsGrid.setColumns("name", "quantityOfTracks");
        userPlaylistsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        playlistContent.add(gridLabel, userPlaylistsGrid, textField);
        playlistContent.addClassName("forms-style");

        theMainContent.add(playlistContent, playlistTrackForm);

        playlistTrackForm.setSizeFull();
        playlistTrackForm.setMargin(false);
        theMainContent.setSizeFull();
        playlistTrackForm.setPlaylist(null);
        userPlaylistsGrid.setItems(restService.getPlaylistsFromApi());

        verticalLayout.add(theMainContent, createPlaylistForm);
        verticalLayout.setPadding(false);

        userPlaylistsGrid.asSingleSelect().addValueChangeListener(event -> {
            playlistTrackForm.setPlaylist(userPlaylistsGrid.asSingleSelect().getValue());
        });
        add(verticalLayout);
        setSizeFull();
    }

    public Playlist getCurrentPlaylist() {
        return userPlaylistsGrid.asSingleSelect().getValue();
    }

    public void updateInformation(String information) {
        textField.setText(information);
    }

    public void refresh() {
        userPlaylistsGrid.setItems(restService.getPlaylistsFromApi());
    }
}
