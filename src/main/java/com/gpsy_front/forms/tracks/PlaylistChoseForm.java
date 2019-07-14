package com.gpsy_front.forms.tracks;

import com.gpsy_front.domain.Playlist;
import com.gpsy_front.forms.ParentForm;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class PlaylistChoseForm extends FormLayout {
    private ParentForm parentForm;
    private VerticalLayout verticalLayout = new VerticalLayout();
    private Button acceptButton = new Button("Save");
    private ComboBox<Playlist> playlistSelect = new ComboBox<>("Playlist");

    public PlaylistChoseForm(ParentForm parentForm, List<Playlist> playlists) {
        this.parentForm = parentForm;
        playlistSelect.setLabel("Playlist");
        playlistSelect.setItems(playlists);
        playlistSelect.setItemLabelGenerator(Playlist::getName);
        playlistSelect.setPlaceholder("Choose the playlist");
        playlistSelect.setItems(playlists);

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
            stringBuilder.append(parentForm.getGrid().asMultiSelect().getSelectedItems().size())
                    .append(" tracks to playlist: ")
                    .append(playlist.getName());
            outputText = stringBuilder.toString();
        }
        parentForm.saveAllToSpotify();
        parentForm.getInformationText().setText(outputText);
        Notification.show(outputText);
        parentForm.getGrid().asMultiSelect().deselectAll();
    }

}
