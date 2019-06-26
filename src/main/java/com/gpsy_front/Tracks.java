package com.gpsy_front;

import com.gpsy_front.forms.MostPopularTrackForm;
import com.gpsy_front.forms.RecentTracksForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "tracks", layout = MainViewWithMainBar.class)
public class Tracks extends Div {
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    private RecentTracksForm recentTracksForm = new RecentTracksForm();
    private MostPopularTrackForm mostPopularTrackForm = new MostPopularTrackForm();

    public Tracks() {
        horizontalLayout.add(recentTracksForm, mostPopularTrackForm);
        add(horizontalLayout);
        setSizeFull();
    }
}
