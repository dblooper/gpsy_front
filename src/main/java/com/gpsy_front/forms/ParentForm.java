package com.gpsy_front.forms;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;

public interface ParentForm {

    Grid<?> getGrid();

    Text getInformationText();

    void saveAllToSpotify();
}
