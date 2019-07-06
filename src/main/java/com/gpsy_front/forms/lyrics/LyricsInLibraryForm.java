package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.LyricsDto;
import com.gpsy_front.domain.LyricsLibrary;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class LyricsInLibraryForm extends VerticalLayout {

    private HorizontalLayout updateForm = new HorizontalLayout();
    private HorizontalLayout deleteForm = new HorizontalLayout();
    private TextField libraryName = new TextField("Library Name");
    private Button updateButton = new Button("Update Library Name");
    private Grid<LyricsDto> lyricsGrid = new Grid<>(LyricsDto.class);
    private Button deleteLyricsButton = new Button("Delete Lyrics");
    private Button deleteLibraryButton = new Button("Delete Library");
    private Binder<LyricsLibrary> lyricsLibraryBinder = new Binder<>(LyricsLibrary.class);
    private LyricsWindow lyricsWindow;

    public LyricsInLibraryForm(LyricsWindow lyricsWindow) {
        this.lyricsWindow = lyricsWindow;

        lyricsGrid.setColumns("title", "artist");
        lyricsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        lyricsLibraryBinder.bindInstanceFields(this);
        updateForm.add(libraryName, updateButton);
        updateForm.setAlignItems(Alignment.END);
        deleteForm.add(deleteLyricsButton, deleteLibraryButton);

        lyricsGrid.asSingleSelect().addValueChangeListener(event -> lyricsWindow.setLyrics(lyricsGrid.asSingleSelect().getValue()));

        add(updateForm, lyricsGrid, deleteForm);
        setVisible(false);
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
}
