package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.LyricsDto;
import com.gpsy_front.domain.LyricsLibrary;
import com.gpsy_front.service.RestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

public class LyricsLibraryForm extends VerticalLayout {

    private RestService restService = RestService.getInstance();
    private Button addLibraryButton = new Button("Create Library");
    private TextField libraryName = new TextField("Library name");
    private Grid<LyricsLibrary> lyricsLibraryGrid = new Grid<>(LyricsLibrary.class);
    private LyricsInLibraryForm lyricsInLibraryForm;
    private LyricsWindow lyricsWindow;
    private VerticalLayout libraryLayout = new VerticalLayout();
    private HorizontalLayout createLibraryLayout = new HorizontalLayout();
    private HorizontalLayout libraryMainLayout = new HorizontalLayout();
    private Label mainLabel = new Label("Lyrics Library");

    public LyricsLibraryForm(LyricsWindow lyricsWindow) {
        this.lyricsWindow = lyricsWindow;
        this.lyricsInLibraryForm = new LyricsInLibraryForm(lyricsWindow, this);

        mainLabel.setClassName("grid-title");
        mainLabel.setSizeFull();

        lyricsLibraryGrid.setColumns("libraryName");
        lyricsLibraryGrid.setItems(restService.getLyricsLibrary());
        lyricsLibraryGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        lyricsLibraryGrid.asSingleSelect().addValueChangeListener(event ->
                    lyricsInLibraryForm.setLibrary(lyricsLibraryGrid.asSingleSelect().getValue()));

        addLibraryButton.addClickListener(event -> addLibrary());

        createLibraryLayout.add(libraryName, addLibraryButton);
        createLibraryLayout.setAlignItems(Alignment.END);

        libraryLayout.add(lyricsLibraryGrid);
        libraryLayout.setWidth("40%");

        libraryMainLayout.add(libraryLayout, lyricsInLibraryForm);
        libraryMainLayout.setWidthFull();

        add(mainLabel, libraryMainLayout, createLibraryLayout);
        setSizeFull();

    }

    private void addLibrary() {
        restService.createLibrary(new LyricsLibrary(libraryName.getValue(), new ArrayList<>()));
        refresh();
    }

    public void refresh() {
        lyricsLibraryGrid.setItems(restService.getLyricsLibrary());
    }

    public Grid<LyricsLibrary> getLyricsLibraryGrid() {
        return this.lyricsLibraryGrid;
    }
}
