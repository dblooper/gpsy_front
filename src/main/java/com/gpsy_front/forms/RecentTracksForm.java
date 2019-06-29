package com.gpsy_front.forms;

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


public class RecentTracksForm extends FormLayout implements ParentForm {

    private RestService restService = new RestService();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private PlaylistChoseForm playlistChoseForm = new PlaylistChoseForm(this);
    private Grid<RecentTrack> recentTracksGrid = new Grid<>(RecentTrack.class);
    private Text textField = new Text("No track chosen");
    private Label gridLabel = new Label("Recently played");

        public RecentTracksForm() {
            gridLabel.setClassName("grid-title");
            gridLabel.setSizeFull();
            recentTracksGrid.setColumns("title", "authors", "playDate");
            recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
                textField.setText(("Total selected: " + recentTracksGrid.getSelectedItems().size()));
                setVisiblePlaylistForm();
            });
            recentTracksGrid.setItems(restService.getRecentTracksFromApi());
            playlistChoseForm.setVisible(false);
            verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistChoseForm);

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
