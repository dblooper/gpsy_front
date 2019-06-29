package com.gpsy_front.forms;

import com.gpsy_front.Tracks;
import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.domain.RecommendedTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendedTrackForm extends FormLayout implements ParentForm{

    private RestService restService = new RestService();
    private PlaylistChoseForm playlistChoseForm = new PlaylistChoseForm(this);
    private VerticalLayout verticalLayout = new VerticalLayout();
    private Grid<RecommendedTrack> recentTracksGrid = new Grid<>(RecommendedTrack.class);
    private Text textField = new Text("No track chosen");
    private Label gridLabel = new Label("Recommended Tracks");
    private Button button = new Button("Refresh");




    public RecommendedTrackForm() {
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        recentTracksGrid.setColumns("title", "authors");
        recentTracksGrid.addComponentColumn(track -> {
                    if(track.getSample() != null) {
                        Anchor anchor = new Anchor(track.getSample(), "Click");
                        anchor.setTarget("tab");
                        return anchor;
                    } else {
                        Anchor anchor = new Anchor("https://www.youtube.com", "Search on YT");
                        anchor.setTarget("tab");
                        return anchor;
                    }
        }).setHeader("Try it out");
        recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
            setVisiblePlaylistForm();
        });
        recentTracksGrid.setItems(restService.getRecommendedTrcksFromApi());

        button.addClickListener(event ->
            recentTracksGrid.setItems(restService.getRecommendedTrcksFromApi()));
        playlistChoseForm.setVisible(false);
        verticalLayout.add(gridLabel, recentTracksGrid, textField, button, playlistChoseForm);
        verticalLayout.addClassName("forms-style");
        verticalLayout.setMinWidth("100%");
        add(verticalLayout);
        setSizeFull();
    }

    private void setVisiblePlaylistForm() {
        if(recentTracksGrid.asMultiSelect().getSelectedItems().size() == 0) {
            playlistChoseForm.setVisible(false);
        } else {
            playlistChoseForm.setVisible(true);
        }
    }

    @Override
    public Grid<?> getGrid() {
        return this.recentTracksGrid;
    }

    @Override
    public void saveAllToSpotify() {
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }
}
