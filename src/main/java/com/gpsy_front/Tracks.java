package com.gpsy_front;

import com.gpsy_front.domain.forms.RecentTracksForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "tracks", layout = MainViewWithMainBar.class)
public class Tracks extends Div {
    Div div = new Div();
    private RecentTracksForm recentTracksForm = new RecentTracksForm();

    public Tracks() {
        recentTracksForm.setSizeFull();
        add(recentTracksForm);


    }
}
