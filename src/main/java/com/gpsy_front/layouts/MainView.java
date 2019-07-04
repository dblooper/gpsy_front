package com.gpsy_front.layouts;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "gpsy", layout = MainViewWithMainBar.class)
public class MainView extends VerticalLayout {

    MainView() {
        add(new Text("Hello to gpsy app!"));
        setAlignItems(Alignment.CENTER);
    }
}
