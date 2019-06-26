package com.gpsy_front;

import com.gpsy_front.forms.PlaylistForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "playlists", layout = MainViewWithMainBar.class)
public class Playlists extends Div {

    private PlaylistForm playlistForm = new PlaylistForm();

    public Playlists() {
        add(playlistForm);
    }
}
