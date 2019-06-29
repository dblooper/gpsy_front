package com.gpsy_front.forms;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.PopularTrack;
import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class MostPopularTrackForm extends FormLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    private Grid<PopularTrack> recentTracksGrid = new Grid<>(PopularTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most popular tracks");
    private Button acceptButton = new Button("Save");
    private ComboBox<Playlist> playlistSelect = new ComboBox<>("Playlist");
    private RestService restService = new RestService();
    private Binder<RecentTrack> binder = new Binder<>(RecentTrack.class);
    private Text textSave = new Text("Nothing saved");


    public MostPopularTrackForm() {
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        recentTracksGrid.setColumns("title", "authors", "popularity");
        recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            String message = String.format("Total selected: " + recentTracksGrid.getSelectedItems().size());
            textField.setText(message);
        });
        recentTracksGrid.setItems(restService.getPopularTracksFromApi());

        playlistSelect.setLabel("Playlist");
        List<Playlist> playlists = restService.getPlaylistsFromApi();
        playlistSelect.setItems(playlists);
        playlistSelect.setItemLabelGenerator(Playlist::getName);
        playlistSelect.setPlaceholder("Choose the playlist");
        playlistSelect.setItems(restService.getPlaylistsFromApi());
        acceptButton.addClickListener(event -> save());

        verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistSelect, acceptButton, textSave);
        verticalLayout.addClassName("forms-style");
        verticalLayout.setMinWidth("100%");
        add(verticalLayout);
        setSizeFull();

    }

    private void save() {
        StringBuilder stringBuilder = new StringBuilder();

        if (recentTracksGrid.asMultiSelect().getSelectedItems().isEmpty()) {
            textSave.setText("Nothing saved, no selected items");
        } else {

            stringBuilder.append("Total: ").append(recentTracksGrid.getSelectedItems().size()).append(" tracks =>");

            recentTracksGrid.asMultiSelect().getSelectedItems().stream()
                    .forEach(item -> stringBuilder.append("[ ").append(item.getTitle()).append(" ]"));

            stringBuilder.append("<= added to: ").append(playlistSelect.getValue().getName());

        }

        textSave.setText(stringBuilder.toString());
    }
}