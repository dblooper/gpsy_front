package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.SearchedTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FreeSearchTrackForm extends VerticalLayout implements ParentForm {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private RestService restService = RestService.getInstance();
    private HorizontalLayout mainLayout = new HorizontalLayout();
    private Button searchButton = new Button("Search");
    private TextField searchfield = new TextField("Search track");
    private Grid<SearchedTrack> searchedTrackGrid = new Grid<>(SearchedTrack.class);
    private PlaylistChoseForm playlistChoseForm;
    private Text textField = new Text("No track choosen");
    private Label searchLabel = new Label("Search for tracks");


    public FreeSearchTrackForm(List<Playlist> playlists) {

        this.playlistChoseForm = new PlaylistChoseForm(this, playlists);
        mainLayout.add(searchfield, searchButton);
        mainLayout.setAlignItems(Alignment.END);
        searchLabel.setClassName("grid-title");
        searchLabel.setSizeFull();

        searchButton.addClickListener(event -> searchForTracks());
        searchfield.setPlaceholder("Type title here");

        searchedTrackGrid.setColumns("title", "artists");
        searchedTrackGrid.addComponentColumn(track -> {
            if(track.getSample() != null) {
                Anchor anchor = new Anchor(track.getSample(), "Click");
                anchor.setTarget("tab");
                return anchor;
            } else {
                Anchor anchor = new Anchor("https://www.youtube.com/results?search_query=" + track.getTitle()+ " " + track.getArtists(), "Search on YT");
                anchor.setTarget("tab");
                return anchor;
            }
        }).setHeader("Try it out");
        searchedTrackGrid.setSelectionMode(Grid.SelectionMode.MULTI);


        searchedTrackGrid.asMultiSelect().addValueChangeListener(event -> {
            textField.setText(("Total selected: " + searchedTrackGrid.getSelectedItems().size()));
            setVisiblePlaylistForm();
        });

        playlistChoseForm.setVisible(false);

        verticalLayout.add(searchLabel, mainLayout, searchedTrackGrid, textField, playlistChoseForm);
        add(verticalLayout);
        setSizeFull();
        verticalLayout.setClassName("forms-style");
        verticalLayout.setSizeFull();
        searchedTrackGrid.setHeightByRows(true);
    }

    private void searchForTracks() {
        if(searchfield.getValue().length() > 0) {
           searchedTrackGrid.setItems(restService.getSearchedTracks(searchfield.getValue()));
        }
    }

    private void setVisiblePlaylistForm() {
        if(searchedTrackGrid.asMultiSelect().getSelectedItems().size() == 0) {
            playlistChoseForm.setVisible(false);
        } else {
            playlistChoseForm.setVisible(true);
        }
    }

    @Override
    public Grid<?> getGrid() {
        return this.searchedTrackGrid;
    }

    @Override
    public void saveAllToSpotify() {
        Set<ParentTrack> parentTracks = searchedTrackGrid.asMultiSelect().getSelectedItems().stream()
                .map(track -> (ParentTrack)track)
                .collect(Collectors.toSet());
        restService.updatePlaylistWithPopularTrack(playlistChoseForm.getCurrentPlaylist(), parentTracks);
        searchedTrackGrid.setItems(new ArrayList<>());
        searchfield.setValue("");
    }

    @Override
    public Text getInformationText() {
        return this.textField;
    }
}
