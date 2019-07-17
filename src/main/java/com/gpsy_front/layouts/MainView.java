package com.gpsy_front.layouts;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "gpsy", layout = NavigationBar.class)
public class MainView extends VerticalLayout {

    private Label welcomeText = new Label("Hello to GPSY app!");
    private Label shortInfoText = new Label("See below a short description of the functionality and... ENJOY!");
    private Image img = new Image("/manual/manual_front_page.png", "Alternative");

    MainView() {
        add(welcomeText);
        add(shortInfoText);
        setAlignItems(Alignment.CENTER);
        add(img);

        welcomeText.addClassName("grid-title");
        welcomeText.setWidthFull();
        img.setSizeFull();
        img.setClassName("forms-style");
    }
}
