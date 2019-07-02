package com.gpsy_front;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.forms.tracks.MostPopularTrackForm;
import com.gpsy_front.forms.tracks.RecentTracksForm;
import com.gpsy_front.forms.tracks.RecommendedTrackForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "tracks", layout = MainViewWithMainBar.class)
public class Tracks extends Div {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private HorizontalLayout horizontalLayoutDown = new HorizontalLayout();

    private RecentTracksForm recentTracksForm;
    private MostPopularTrackForm mostPopularTrackForm;
    private RecommendedTrackForm recommendedTrackForm;
    private RestService restService = RestService.getInstance();

    public Tracks() {
        List<Playlist> playlists = restService.getPlaylistsFromApi();
        this.recentTracksForm = new RecentTracksForm(playlists);
        this.mostPopularTrackForm = new MostPopularTrackForm(playlists);
        this.recommendedTrackForm = new RecommendedTrackForm(playlists);

//        recommendedTrackForm.setPadding(false);
//        recommendedTrackForm.setMargin(false);
//
//        mostPopularTrackForm.setMargin(false);
//        mostPopularTrackForm.setPadding(false);

        horizontalLayout.add(recentTracksForm, mostPopularTrackForm);
        horizontalLayout.setSizeFull();
        horizontalLayoutDown.add(recommendedTrackForm);
//        recommendedTrackForm.setWidth("50%");
        horizontalLayoutDown.setSizeFull();
        verticalLayout.add(horizontalLayout, horizontalLayoutDown);
        verticalLayout.setPadding(false);
        add(verticalLayout);
    }
}
