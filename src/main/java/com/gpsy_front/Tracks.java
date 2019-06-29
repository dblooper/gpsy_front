package com.gpsy_front;

import com.gpsy_front.domain.RecommendedTrack;
import com.gpsy_front.forms.MostPopularTrackForm;
import com.gpsy_front.forms.RecentTracksForm;
import com.gpsy_front.forms.RecommendedTrackForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "tracks", layout = MainViewWithMainBar.class)
public class Tracks extends Div {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private HorizontalLayout horizontalLayoutDown = new HorizontalLayout();
    private RecentTracksForm recentTracksForm = new RecentTracksForm();
    private MostPopularTrackForm mostPopularTrackForm = new MostPopularTrackForm();
    private RecommendedTrackForm recommendedTrackForm = new RecommendedTrackForm();

    public Tracks() {
        horizontalLayout.add(recentTracksForm, mostPopularTrackForm);
        horizontalLayout.setSizeFull();
        horizontalLayoutDown.add(recommendedTrackForm);
        recommendedTrackForm.setWidth("50%");
        horizontalLayoutDown.setSizeFull();
//        horizontalLayoutDown.addClassName("horizontalLayoutAlignment");
        horizontalLayoutDown.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(horizontalLayout, horizontalLayoutDown);
        add(verticalLayout);
        setSizeFull();
    }
}
