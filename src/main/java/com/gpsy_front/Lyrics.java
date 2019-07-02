package com.gpsy_front;

import com.gpsy_front.forms.lyrics.PopularTracksWithLyrics;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@Route(value = "lyrics", layout = MainViewWithMainBar.class)
public class Lyrics extends Div implements RouterLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    PopularTracksWithLyrics popularTracksWithLyrics = new PopularTracksWithLyrics();

    public Lyrics() {
        verticalLayout.add(popularTracksWithLyrics);
        add(verticalLayout);
    }
}


