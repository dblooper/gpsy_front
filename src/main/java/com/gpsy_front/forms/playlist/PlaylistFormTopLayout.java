package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.service.RestSpotifyService;
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
    private Label gridLabel = new Label("Playlists on your Spotify");
    private RestSpotifyService restSpotifyService = RestSpotifyService.getInstance();
    private PlaylistTrackForm playlistTrackForm = new PlaylistTrackForm(this);
    private CreatePlaylistForm createPlaylistForm = new CreatePlaylistForm(this);

    public PlaylistFormTopLayout() {

        userPlaylistsGrid.setColumns("name", "quantityOfTracks");
        userPlaylistsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        playlistContent.add(gridLabel, userPlaylistsGrid, textField);

        theMainContent.add(playlistContent, playlistTrackForm);

        playlistTrackForm.setPlaylist(null);
        userPlaylistsGrid.setItems(restSpotifyService.getPlaylistsFromApi());

        verticalLayout.add(theMainContent, createPlaylistForm);

        userPlaylistsGrid.asSingleSelect().addValueChangeListener(event -> {
            if(userPlaylistsGrid.asSingleSelect().getValue() != null) {
                textField.setText(userPlaylistsGrid.asSingleSelect().getValue().getName() + " selected");
            } else {
                textField.setText("No playlist selected");
            }
            playlistTrackForm.setPlaylist(userPlaylistsGrid.asSingleSelect().getValue());
        });

        add(verticalLayout);

        setSizeFull();
        gridLabel.setClassName("grid-title");
        gridLabel.setWidthFull();
        theMainContent.addClassName("forms-style");
        playlistTrackForm.setSizeFull();
        playlistTrackForm.setMargin(false);
        theMainContent.setSizeFull();
        verticalLayout.setPadding(false);

    }

    public Playlist getCurrentPlaylist() {
        return userPlaylistsGrid.asSingleSelect().getValue();
    }

    public void updateInformation(String information) {
        textField.setText(information);
    }

    public void refresh() {
        userPlaylistsGrid.setItems(restSpotifyService.getPlaylistsFromApi());
    }
}
