package com.gpsy_front.forms;

import com.gpsy_front.domain.ParentTrack;
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

public class MostPopularTrackForm extends FormLayout implements ParentForm {

    private RestService restService = new RestService();
    VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistChoseForm playlistChoseForm = new PlaylistChoseForm(this);
    private Grid<PopularTrack> recentTracksGrid = new Grid<>(PopularTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Most popular tracks");

    public MostPopularTrackForm() {
        gridLabel.setClassName("grid-title");
        gridLabel.setSizeFull();
        recentTracksGrid.setColumns("title", "authors", "popularity");
        recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
            textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
            setVisiblePlaylistForm();
        });
        recentTracksGrid.setItems(restService.getPopularTracksFromApi());
        playlistChoseForm.setVisible(false);
        verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistChoseForm);

        verticalLayout.addClassName("forms-style");
        verticalLayout.setMinWidth("100%");
        add(verticalLayout);
        setSizeFull();

    }


    public int getGridSelectedItemsSize() {
        return this.recentTracksGrid.getSelectedItems().size();
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