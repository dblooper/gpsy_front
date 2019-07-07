package com.gpsy_front.layouts;

import com.gpsy_front.forms.playlist.PlaylistFormTopLayout;
import com.gpsy_front.forms.playlist.PlaylistGeneratorOutput;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "playlists", layout = MainViewWithMainBar.class)
public class Playlists extends Div {

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    VerticalLayout verticalLayoutLeft = new VerticalLayout();
    VerticalLayout verticalLayoutRight = new VerticalLayout();
    private PlaylistFormTopLayout playlistFormTopLayout = new PlaylistFormTopLayout();
    private PlaylistGeneratorOutput playlistGeneratorOutput = new PlaylistGeneratorOutput();

    public Playlists() {

        verticalLayoutLeft.add(playlistFormTopLayout);
        verticalLayoutLeft.setSizeFull();
        verticalLayoutRight.add(playlistGeneratorOutput);
        verticalLayoutRight.setSizeFull();
        horizontalLayout.add(playlistFormTopLayout, playlistGeneratorOutput);
        add(horizontalLayout);
    }
}
