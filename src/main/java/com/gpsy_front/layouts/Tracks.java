package com.gpsy_front.layouts;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.forms.tracks.MostFrequentTrackForm;
import com.gpsy_front.forms.tracks.PopularTrackForm;
import com.gpsy_front.forms.tracks.RecentTrackForm;
import com.gpsy_front.forms.tracks.RecommendedTrackForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "tracks", layout = MainViewWithMainBar.class)
public class Tracks extends Div {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private HorizontalLayout horizontalLayoutDown = new HorizontalLayout();

    private RecentTrackForm recentTrackForm;
    private MostFrequentTrackForm mostFrequentTrackForm;
    private PopularTrackForm popularTrackForm;
    private RecommendedTrackForm recommendedTrackForm;
    private RestService restService = RestService.getInstance();

    public Tracks() {
        List<Playlist> playlists = restService.getPlaylistsFromApi();
        this.recentTrackForm = new RecentTrackForm(playlists);
        this.mostFrequentTrackForm = new MostFrequentTrackForm(playlists);
        this.recommendedTrackForm = new RecommendedTrackForm(playlists);
        this.popularTrackForm = new PopularTrackForm(playlists);

//        recommendedTrackForm.setPadding(false);
//        recommendedTrackForm.setMargin(false);
//
//        mostFrequentTrackForm.setMargin(false);
//        mostFrequentTrackForm.setPadding(false);

        horizontalLayout.add(recentTrackForm, mostFrequentTrackForm);
        horizontalLayout.setSizeFull();
        horizontalLayoutDown.add(recommendedTrackForm, popularTrackForm);
//        recommendedTrackForm.setWidth("50%");
        horizontalLayoutDown.setSizeFull();
        verticalLayout.add(horizontalLayout, horizontalLayoutDown);
        verticalLayout.setPadding(false);
        add(verticalLayout);
    }
}
