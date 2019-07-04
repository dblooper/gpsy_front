package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;

public class CreatePlaylistForm extends VerticalLayout {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistForm playlistForm;
    private RestService restService = RestService.getInstance();
    private TextField textField = new TextField();
    private Text text = new Text("");
    private Button button = new Button("Create a playlist");

    public CreatePlaylistForm(PlaylistForm playlistForm) {
        this.playlistForm = playlistForm;

        text.setText("Remember! You cannot delete playlist from spotify after creation!");
        textField.setPlaceholder("Name of playlist");

        button.addClickListener(event -> {
            restService.createNewPlaylist(new Playlist(textField.getValue(), "", new ArrayList<>()));
            playlistForm.refresh();
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        verticalLayout.add(text, textField, button);
        add(verticalLayout);
        setClassName("forms-style");
    }
}
