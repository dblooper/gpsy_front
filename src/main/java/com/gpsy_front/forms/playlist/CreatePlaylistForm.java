package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.service.RestSpotifyService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;

public class CreatePlaylistForm extends VerticalLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistFormTopLayout playlistFormTopLayout;
    private RestSpotifyService restSpotifyService = RestSpotifyService.getInstance();
    private TextField textField = new TextField();
    private Text text = new Text("");
    private Button button = new Button("Create a playlist");

    public CreatePlaylistForm(PlaylistFormTopLayout playlistFormTopLayout) {
        this.playlistFormTopLayout = playlistFormTopLayout;

        text.setText("Remember! Due to Spotify policy, you cannot delete playlist from Spotify after creation!");
        textField.setPlaceholder("Name of playlist");

        button.addClickListener(event -> {
            String newPlaylistName = textField.getValue();
            restSpotifyService.createNewPlaylist(new Playlist(newPlaylistName, "", new ArrayList<>()));
            playlistFormTopLayout.refresh();
            Notification.show("A new playlist: " + newPlaylistName + " has been created on your Spotify account!");
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        verticalLayout.add(text, textField, button);
        add(verticalLayout);
        setClassName("forms-style");
    }
}
