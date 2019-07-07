package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.LyricsDto;
import com.gpsy_front.domain.LyricsLibrary;
import com.gpsy_front.service.LyricsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;

public class LyricsInLibraryForm extends VerticalLayout {

    private HorizontalLayout updateForm = new HorizontalLayout();
    private HorizontalLayout deleteForm = new HorizontalLayout();
    private TextField libraryName = new TextField("Library Name");
    private Button updateButton = new Button("Update Name");
    private Button addLyricsButton = new Button("Add Viewed Lyrics");
    private Button deleteLyricsButton = new Button("Delete Lyrics");
    private Button deleteLibraryButton = new Button("Delete Library");
    private Grid<LyricsDto> lyricsGrid = new Grid<>(LyricsDto.class);
    private Binder<LyricsLibrary> lyricsLibraryBinder = new Binder<>(LyricsLibrary.class);
    private LyricsWindow lyricsWindow;
    private LyricsService lyricsService = LyricsService.getInstance();
    private LyricsLibraryForm lyricsLibraryForm;

    public LyricsInLibraryForm(LyricsWindow lyricsWindow, LyricsLibraryForm lyricsLibraryForm) {
        this.lyricsWindow = lyricsWindow;
        this.lyricsLibraryForm = lyricsLibraryForm;

        lyricsWindow.getLyricsTextArea().addValueChangeListener(event -> showAddVievedLyricsBitton());

        lyricsGrid.setColumns("title", "artist");
        lyricsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        lyricsGrid.addSelectionListener(event -> showDeleteButton());

        lyricsLibraryBinder.bindInstanceFields(this);
        updateForm.add(libraryName, updateButton, addLyricsButton);
        updateForm.setAlignItems(Alignment.END);
        updateButton.addClickListener(event -> updateLibraryName());

        addLyricsButton.addClickListener(event -> addCurrentlyDisplayedLyricsToLibrary());

        deleteForm.add(deleteLyricsButton, deleteLibraryButton);

        lyricsGrid.asSingleSelect().addValueChangeListener(event -> setLyricsInWindow());

        deleteLibraryButton.addClickListener(event -> deleteLibrary());
        deleteLyricsButton.addClickListener(event -> deleteLyrics());

        add(updateForm, lyricsGrid, deleteForm);
        setVisible(false);
        addLyricsButton.setVisible(false);
        deleteLyricsButton.setVisible(false);
    }

    private void setLyrics(List<LyricsDto> lyrics) {
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
        lyricsService.deleteLibrary(lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue().getId());
        lyricsLibraryForm.refresh();
    }

    private void deleteLyrics() {
        LyricsLibrary currentLibrarySelection = lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue();
        List<LyricsDto> lyricsToAdd = new ArrayList<>();
        lyricsToAdd.add(lyricsGrid.asSingleSelect().getValue());
        lyricsService.deleteLyricsFromLibrary(new LyricsLibrary(currentLibrarySelection.getId(),
                                                                currentLibrarySelection.getLibraryName(),
                                                                lyricsToAdd));
        lyricsLibraryForm.refresh();
    }

    private void setLyricsInWindow() {
        lyricsWindow.setLyrics(lyricsGrid.asSingleSelect().getValue());
    }

    private void updateLibraryName() {
        LyricsLibrary currentLibrary = lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue();
        lyricsService.updateLibraryName(new LyricsLibrary(currentLibrary.getId(), libraryName.getValue(), new ArrayList<>()));
        lyricsLibraryForm.refresh();
    }

    private void addCurrentlyDisplayedLyricsToLibrary() {
        LyricsLibrary currentLibrary = lyricsLibraryForm.getLyricsLibraryGrid().asSingleSelect().getValue();
        List<LyricsDto> lyricstToAddList = new ArrayList<>();
        lyricstToAddList.add(lyricsWindow.getCurrentLyrics());
        lyricsService.addLyricsToLibrary(new LyricsLibrary(currentLibrary.getId(), currentLibrary.getLibraryName(),lyricstToAddList));
        lyricsLibraryForm.refresh();
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
