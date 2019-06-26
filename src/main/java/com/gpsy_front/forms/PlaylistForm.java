package com.gpsy_front.forms;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.service.RESTService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PlaylistForm extends FormLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    private Grid<Playlist> userPlaylistsGrid = new Grid<>(Playlist.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Playlists");
    private Button acceptButton = new Button("Save");
    private ComboBox<String> playlistTracks = new ComboBox<>("Playlist");
    private RESTService restService = new RESTService();
    private PlaylistTrackForm playlistTrackForm = new PlaylistTrackForm(this);

    public PlaylistForm() {
        gridLabel.setId("grid-title");
        userPlaylistsGrid.setColumns("name", "quantityOfTracks");
        userPlaylistsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

//        userPlaylistsGrid.asMultiSelect().addValueChangeListener(event -> {
//            String message = String.format("Selection changed from %s to %s",
//                    event.getOldValue(), event.getValue());
//            textField.setText(message);
//        });

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
    }
}
