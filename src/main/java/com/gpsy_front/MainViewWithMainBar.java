package com.gpsy_front;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;

@StyleSheet("frontend://style.css")
//@ParentLayout(com.gpsy_front.ParentLayout.class)
@Route("gpsy")
public class MainViewWithMainBar extends Div implements RouterLayout {
    private HorizontalLayout mainHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout logo = new HorizontalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();


    public MainViewWithMainBar() {
        Icon icon = new Icon(VaadinIcon.HEADPHONES);
        icon.setSize("50px");
        Label label = new Label("GPSY APP");
        label.setClassName("logo");
        addMenuElement(Playlists.class, "Playlists");
        addMenuElement(Tracks.class, "Tracks");
        addMenuElement(Lyrics.class, "Lyrics");
        logo.add(icon, label);
        logo.setAlignItems(FlexComponent.Alignment.CENTER);
        mainHorizontalLayout.add(logo, horizontalLayout);
        mainHorizontalLayout.setClassName("main-navbar-layout");
        mainHorizontalLayout.setPadding(true);
        mainHorizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainHorizontalLayout.setWidth("100%");
        horizontalLayout.setWidthFull();
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        horizontalLayout.setClassName("navbar-items");

        add(mainHorizontalLayout);

    }

    private void addMenuElement(Class<? extends Component> navigationTarget, String name) {
        RouterLink routerLink = new RouterLink(name, navigationTarget);
        routerLink.setClassName("router-link");
        horizontalLayout.add(routerLink);
    }

}
