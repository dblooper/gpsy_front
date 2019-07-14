package com.gpsy_front.layouts;

import com.gpsy_front.forms.lyrics.LyricsAndLibraryTopLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@Route(value = "lyrics", layout = MainNavigationBar.class)
public class Lyrics extends Div implements RouterLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    LyricsAndLibraryTopLayout lyricsAndLibraryTopLayout = new LyricsAndLibraryTopLayout();

    public Lyrics() {
        verticalLayout.add(lyricsAndLibraryTopLayout);
        add(verticalLayout);
        setClassName("main-frame");
    }
}


