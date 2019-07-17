package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.Lyrics;
import com.gpsy_front.domain.LyricsLibrary;
import com.gpsy_front.service.RestLyricsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;

public class LyricsInLibraryForm extends VerticalLayout {

    private HorizontalLayout updateForm = new HorizontalLayout();
    private VerticalLayout deleteForm = new VerticalLayout();
    private HorizontalLayout buttonsForm = new HorizontalLayout();
    private VerticalLayout lyricsButtonsForm = new VerticalLayout();
    private TextField libraryName = new TextField("Library Name");
    private Button updateButton = new Button("Update Name");
    private Button addLyricsButton = new Button("Add Shown Lyrics");
    private Button deleteLyricsButton = new Button("Delete Lyrics");
    private Button deleteLibraryButton = new Button("Delete Library");
    private Grid<Lyrics> lyricsGrid = new Grid<>(Lyrics.class);
    private Binder<LyricsLibrary> lyricsLibraryBinder = new Binder<>(LyricsLibrary.class);
    private LyricsWindow lyricsWindow;
    private RestLyricsService restLyricsService = RestLyricsService.getInstance();
    private LyricsLibraryForm lyricsLibraryForm;

    public LyricsInLibraryForm(LyricsWindow lyricsWindow, LyricsLibraryForm lyricsLibraryForm) {

        this.lyricsWindow = lyricsWindow;
        this.lyricsLibraryForm = lyricsLibraryForm;

        lyricsWindow.getLyricsTextArea().addValueChangeListener(event -> showAddVievedLyricsBitton());

        lyricsGrid.setColumns("title", "artists");
        lyricsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        lyricsGrid.addSelectionListener(event -> showDeleteButton());

        lyricsLibraryBinder.bindInstanceFields(this);
        updateForm.add(libraryName, updateButton);

        updateButton.addClickListener(event -> updateLibraryName());

        addLyricsButton.addClickListener(event -> addCurrentlyDisplayedLyricsToLibrary());

        deleteForm.add(deleteLibraryButton);
        lyricsButtonsForm.add(addLyricsButton, deleteLyricsButton);

        buttonsForm.add(deleteForm, lyricsButtonsForm);

        lyricsGrid.asSingleSelect().addValueChangeListener(event -> setLyricsInWindow());

        deleteLibraryButton.addClickListener(event -> deleteLibrary());
        deleteLyricsButton.addClickListener(event -> deleteLyrics());

        add(updateForm, lyricsGrid, buttonsForm);
        setVisible(false);
        lyricsGrid.setSizeFull();
        addLyricsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteLyricsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteLibraryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addLyricsButton.setVisible(false);
        deleteLyricsButton.setVisible(false);
        lyricsButtonsForm.setAlignItems(Alignment.END);
        buttonsForm.setSizeFull();
        updateForm.setAlignItems(Alignment.END);
        deleteForm.setSizeFull();
        deleteForm.setPadding(false);
        lyricsButtonsForm.setPadding(false);
        lyricsButtonsForm.setMargin(false);
        lyricsButtonsForm.setPadding(false);

    }

    private void setLyrics(List<Lyrics> lyrics) {
        this.lyricsGrid.setItems(lyrics);
    }

    public void setLibrary(LyricsLibrary library) {
        lyricsLibraryBinder.setBean(library);
        if(library == null) {
            setVisible(false);
        } else {
            setVisible(true);
            setLyrics(library.getLyrics());
        }
    }

    private void deleteLibrary() {
        LyricsLibrary libraryToDelete= lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue();
        restLyricsService.deleteLibrary(libraryToDelete.getId());
        lyricsLibraryForm.refresh();
        Notification.show("Library: " + libraryToDelete.getLibraryName() + " has been deleted");
    }

    private void deleteLyrics() {
        LyricsLibrary currentLibrarySelection = lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue();
        List<Lyrics> lyricsToDelete = new ArrayList<>();
        lyricsToDelete.add(lyricsGrid.asSingleSelect().getValue());
        restLyricsService.deleteLyricsFromLibrary(new LyricsLibrary(currentLibrarySelection.getId(),
                                                                currentLibrarySelection.getLibraryName(),
                                                                lyricsToDelete));
        lyricsLibraryForm.refresh();
        Notification.show("Lyrics: " + lyricsToDelete.get(0).getTitle() + " have been deleted from: " + currentLibrarySelection.getLibraryName());
    }

    private void setLyricsInWindow() {
        lyricsWindow.setLyrics(lyricsGrid.asSingleSelect().getValue());

        if(lyricsGrid.asSingleSelect().getValue() == null) {
            addLyricsButton.setVisible(false);
        } else {
            addLyricsButton.setVisible(true);
        }
    }

    private void updateLibraryName() {
        if(libraryName.getValue().length() > 3) {
            LyricsLibrary currentLibrary = lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue();
            String newLibraryName = libraryName.getValue();
            restLyricsService.updateLibraryName(new LyricsLibrary(currentLibrary.getId(), newLibraryName, new ArrayList<>()));
            lyricsLibraryForm.refresh();
            Notification.show("Library name has been updated to: " + newLibraryName);
        }else {
            Notification.show("You must type at least 4 characters to proceed!",3000, Notification.Position.TOP_START);
        }
    }

    public void addCurrentlyDisplayedLyricsToLibrary() {
        LyricsLibrary currentLibrary = lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue();
        List<Lyrics> lyricstToAddList = new ArrayList<>();
        lyricstToAddList.add(lyricsWindow.getCurrentLyrics());
        restLyricsService.addLyricsToLibrary(new LyricsLibrary(currentLibrary.getId(), currentLibrary.getLibraryName(),lyricstToAddList));
        lyricsLibraryForm.refresh();
        Notification.show("Currently shown lyrics: " + lyricstToAddList.get(0).getTitle() + "have been saved to: " + currentLibrary.getLibraryName());
    }

    private void showDeleteButton() {
        if(lyricsGrid.asSingleSelect().getValue() == null) {
            deleteLyricsButton.setVisible(false);
        }

        deleteLyricsButton.setVisible(true);
    }

    private void showAddVievedLyricsBitton() {
        if(lyricsWindow.getLyricsTextArea().getValue().length() == 0) {
            addLyricsButton.setVisible(false);
        }
        addLyricsButton.setVisible(true);
    }

}
