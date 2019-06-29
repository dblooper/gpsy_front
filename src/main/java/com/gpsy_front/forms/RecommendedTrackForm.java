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

public class RecommendedTrackForm extends FormLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    private Grid<RecommendedTrack> recentTracksGrid = new Grid<>(RecommendedTrack.class);
    private Text textField = new Text("No track chosen");
    private Label gridLabel = new Label("Recommended Tracks");
    private Button acceptButton = new Button("Save");
    private ComboBox<String> playlistSelect = new ComboBox<>("Playlist");
    private RestService restService = new RestService();
    private Text textSave = new Text("");


    public RecommendedTrackForm() {
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        recentTracksGrid.setColumns("titles", "authors");
        recentTracksGrid.addComponentColumn(track -> {
                    if(track.getSample() != null) {
                        Anchor anchor = new Anchor(track.getSample(), "Click");
                        anchor.setTarget("tab");
                        return anchor;
                    } else
                        return new Anchor("No link");
        }).setHeader("Try it out");
        recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            String message = String.format("Selection changed from %s to %s",
                    event.getOldValue(), event.getValue());
            textField.setText(message);
        });
        recentTracksGrid.setItems(restService.getRecommendedTrcksFromApi());

        playlistSelect.setLabel("Playlist");
        playlistSelect.setPlaceholder("Choose the playlist");
        playlistSelect.setItems("Weekly", "Mothly");

        acceptButton.addClickListener(event -> save());

        verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistSelect, acceptButton, textSave);
        verticalLayout.addClassName("forms-style");
        verticalLayout.setMinWidth("100%");
        add(verticalLayout);
        setSizeFull();
    }

    private void save() {
        StringBuilder stringBuilder = new StringBuilder("Saved: ");
        String outputText;

        if(recentTracksGrid.asMultiSelect().getSelectedItems().isEmpty()) {
            outputText = "Nothing saved, no selected items";
        }else {
            recentTracksGrid.asMultiSelect().getSelectedItems().stream()
                    .forEach(item -> stringBuilder.append("[ ").append(item.getTitles()).append(" ] "));
            outputText = stringBuilder.toString();
        }
        textSave.setText(outputText);
    }
}
