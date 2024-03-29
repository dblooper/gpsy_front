package com.gpsy_front.layouts;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.forms.tracks.*;
import com.gpsy_front.service.RestSpotifyService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "tracks", layout = NavigationBar.class)
public class Tracks extends Div {

    private VerticalLayout verticalLayout1 = new VerticalLayout();
    private VerticalLayout verticalLayout2 = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private RecentTrackForm recentTrackForm;
    private MostFrequentTrackForm mostFrequentTrackForm;
    private PopularTrackForm popularTrackForm;
    private RecommendedTrackForm recommendedTrackForm;
    private RestSpotifyService restSpotifyService = RestSpotifyService.getInstance();
    private FreeSearchTrackForm freeSearchTrackForm;

    public Tracks() {
        List<Playlist> playlists = restSpotifyService.getPlaylistsFromApi();
        this.recentTrackForm = new RecentTrackForm(playlists);
        this.mostFrequentTrackForm = new MostFrequentTrackForm(playlists);
        this.recommendedTrackForm = new RecommendedTrackForm(playlists);
        this.popularTrackForm = new PopularTrackForm(playlists);
        this.freeSearchTrackForm = new FreeSearchTrackForm(playlists);

        verticalLayout1.add(recentTrackForm, recommendedTrackForm);
        verticalLayout1.setSizeFull();
        verticalLayout2.add(freeSearchTrackForm, mostFrequentTrackForm, popularTrackForm);

        horizontalLayout.add(verticalLayout1, verticalLayout2);

        add(horizontalLayout);
        setClassName("main-frame");
        freeSearchTrackForm.setHeight("20%");
        verticalLayout2.setSizeFull();
        verticalLayout1.setPadding(false);
        verticalLayout2.setPadding(false);
        verticalLayout1.setMargin(false);
        horizontalLayout.setPadding(false);
    }
}
