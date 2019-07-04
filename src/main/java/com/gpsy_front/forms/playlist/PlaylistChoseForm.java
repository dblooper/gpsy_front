package com.gpsy_front.forms.playlist;

import com.gpsy_front.domain.ParentTrack;
import com.gpsy_front.domain.Playlist;
import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class PlaylistChoseForm extends FormLayout {
    private ParentForm parentForm;
    private VerticalLayout verticalLayout = new VerticalLayout();
    private Button acceptButton = new Button("Save");
    private ComboBox<Playlist> playlistSelect = new ComboBox<>("Playlist");
    private RestService restService = RestService.getInstance();
    private Binder<RecentTrack> binder = new Binder<>(RecentTrack.class);

    public PlaylistChoseForm(ParentForm parentForm, List<Playlist> playlists) {
        this.parentForm = parentForm;
        playlistSelect.setLabel("Playlist");
        playlistSelect.setItems(playlists);
        playlistSelect.setItemLabelGenerator(Playlist::getName);
        playlistSelect.setPlaceholder("Choose the playlist");
        playlistSelect.setItems(restService.getPlaylistsFromApi());

        acceptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        acceptButton.addClickListener(event -> save(playlistSelect.getValue()));

        verticalLayout.add(playlistSelect, acceptButton);
        add(verticalLayout);
        setSizeFull();
    }

    public Playlist getCurrentPlaylist() {
        return playlistSelect.getValue();
    }

    private void save(Playlist playlist) {
        StringBuilder stringBuilder = new StringBuilder("Saved: ");
        String outputText;

        if(parentForm.getGrid().asMultiSelect().getSelectedItems().size() == 0 || playlist == null) {
            outputText = "Nothing saved, no selected items";
        }else {
            parentForm.saveAllToSpotify();
            stringBuilder.append(parentForm.getGrid().asMultiSelect().getSelectedItems().size())
                    .append(" trackst to playlist: ")
                    .append(playlist.getName());
            parentForm.getGrid().asMultiSelect().deselectAll();
            outputText = stringBuilder.toString();
        }
        parentForm.getInformationText().setText(outputText);
    }

}
