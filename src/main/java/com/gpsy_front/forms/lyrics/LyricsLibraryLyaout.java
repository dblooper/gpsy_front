package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.LyricsDto;
import com.gpsy_front.domain.LyricsLibrary;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

public class LyricsLibraryLyaout extends VerticalLayout {

    private RestService restService = RestService.getInstance();
    private Button addLibraryButton = new Button("Create Library");
    private Button addLyricsButton = new Button("Add Lyrics");
    private TextField libraryName = new TextField("Library name");
    private Grid<LyricsLibrary> lyricsLibraryGrid = new Grid<>(LyricsLibrary.class);
    private LyricsInLibraryForm lyricsInLibraryForm;
    private LyricsWindow lyricsWindow;
    private VerticalLayout libraryLayout = new VerticalLayout();
    private HorizontalLayout createLibraryLayout = new HorizontalLayout();
    private HorizontalLayout libraryMainLayout = new HorizontalLayout();
    private Label mainLabel = new Label("Lyrics Library");

    public LyricsLibraryLyaout(LyricsWindow lyricsWindow) {
        this.lyricsWindow = lyricsWindow;
        this.lyricsInLibraryForm = new LyricsInLibraryForm(lyricsWindow);

        mainLabel.setClassName("grid-title");
        mainLabel.setSizeFull();

        lyricsLibraryGrid.setColumns("libraryName");
        lyricsLibraryGrid.setItems(restService.fetchLyricsLibrary());
        lyricsLibraryGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        lyricsLibraryGrid.asSingleSelect().addValueChangeListener(event ->
                    lyricsInLibraryForm.setLibrary(lyricsLibraryGrid.asSingleSelect().getValue()));
        addLibraryButton.addClickListener(event -> addLibrary());
        addLyricsButton.addClickListener(event -> addCurrentlyDisplayedLyricsToLibrary(lyricsLibraryGrid.asSingleSelect().getValue()));

        createLibraryLayout.add(libraryName, addLibraryButton);
        createLibraryLayout.setAlignItems(Alignment.END);

        libraryLayout.add(lyricsLibraryGrid, addLyricsButton);
        libraryLayout.setWidth("40%");

        libraryMainLayout.add(libraryLayout, lyricsInLibraryForm);
        libraryMainLayout.setWidthFull();

        add(mainLabel, libraryMainLayout, createLibraryLayout);
        setSizeFull();

    }

    private void addLibrary() {
        restService.addLibrary(new LyricsLibrary(libraryName.getValue(), new ArrayList<>()));
        refresh();
    }

    private void refresh() {
        lyricsLibraryGrid.setItems(restService.fetchLyricsLibrary());
    }

    private void addCurrentlyDisplayedLyricsToLibrary(LyricsLibrary library) {
        List<LyricsDto> lyricstToAddList = new ArrayList<>();
        lyricstToAddList.add(lyricsWindow.getCurrentLyrics());
        restService.addLyricsToLibrary(new LyricsLibrary(library.getLibraryName(),lyricstToAddList, library.getLibraryCode()));
        refresh();
    }
}
