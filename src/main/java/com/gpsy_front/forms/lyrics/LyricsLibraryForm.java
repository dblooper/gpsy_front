package com.gpsy_front.forms.lyrics;

import com.gpsy_front.domain.LyricsLibrary;
import com.gpsy_front.service.RestLyricsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;

public class LyricsLibraryForm extends VerticalLayout {

    private RestLyricsService restLyricsService = RestLyricsService.getInstance();
    private Button addLibraryButton = new Button("Create Library");
    private TextField libraryName = new TextField("Library name");
    private Grid<LyricsLibrary> lyricsLibraryGrid = new Grid<>(LyricsLibrary.class);
    private LyricsInLibraryForm lyricsInLibraryForm;
    private VerticalLayout libraryLayout = new VerticalLayout();
    private HorizontalLayout createLibraryLayout = new HorizontalLayout();
    private HorizontalLayout libraryMainLayout = new HorizontalLayout();
    private Label mainLabel = new Label("Lyrics Library");

    public LyricsLibraryForm(LyricsWindow lyricsWindow) {

        this.lyricsInLibraryForm = new LyricsInLibraryForm(lyricsWindow, this);

        lyricsLibraryGrid.setColumns("libraryName");
        lyricsLibraryGrid.setItems(restLyricsService.getLyricsLibrary());
        lyricsLibraryGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        lyricsLibraryGrid.asSingleSelect().addValueChangeListener(event ->
                    lyricsInLibraryForm.setLibrary(lyricsLibraryGrid.asSingleSelect().getValue()));

        addLibraryButton.addClickListener(event -> addLibrary());

        createLibraryLayout.add(libraryName, addLibraryButton);

        libraryLayout.add(lyricsLibraryGrid);

        libraryMainLayout.add(libraryLayout, lyricsInLibraryForm);
        libraryMainLayout.setWidthFull();

        add(mainLabel, libraryMainLayout, createLibraryLayout);

        setSizeFull();
        libraryLayout.setWidth("100%");
        createLibraryLayout.setAlignItems(Alignment.END);
        mainLabel.setClassName("grid-title");
        mainLabel.setSizeFull();
        addLibraryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void addLibrary() {
        String newLibraryName = libraryName.getValue();
        restLyricsService.createLibrary(new LyricsLibrary(newLibraryName, new ArrayList<>()));
        refresh();
        Notification.show("New library: " + newLibraryName + " has been created");
    }

    public void refresh() {
        lyricsLibraryGrid.setItems(restLyricsService.getLyricsLibrary());
    }

    public Grid<LyricsLibrary> getLyricsLibraryGrid() {
        return this.lyricsLibraryGrid;
    }
}
