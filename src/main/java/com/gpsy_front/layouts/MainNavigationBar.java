package com.gpsy_front.layouts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;

@StyleSheet("frontend://style.css")
public class MainNavigationBar extends Div implements RouterLayout {

    private HorizontalLayout mainHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout logo = new HorizontalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private HorizontalLayout aboutMe = new HorizontalLayout();


    public MainNavigationBar() {

        addMenuElement(MainView.class, "GPSY", new Icon(VaadinIcon.HEADPHONES),logo, "logo");
        addMenuElement(Playlists.class, "Playlists", new Icon(VaadinIcon.INDENT), horizontalLayout, "router-link");
        addMenuElement(Tracks.class, "Tracks", new Icon(VaadinIcon.PLAY), horizontalLayout, "router-link");
        addMenuElement(Lyrics.class, "Lyrics", new Icon(VaadinIcon.CLIPBOARD_TEXT), horizontalLayout, "router-link");

        Anchor myGitHub = new Anchor("https://github.com/dkoplenski", "See my GitHub");
        myGitHub.setClassName("about");
        aboutMe.add(myGitHub);

        mainHorizontalLayout.add(logo, horizontalLayout, aboutMe);
        mainHorizontalLayout.setClassName("main-navbar-layout");
        horizontalLayout.setWidthFull();
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        horizontalLayout.setClassName("navbar-items");
        add(mainHorizontalLayout);
        setSizeFull();
    }

    private void addMenuElement(Class<? extends Component> navigationTarget, String name, Icon icon, HorizontalLayout horizontalLayout, String className) {

        RouterLink routerLink = new RouterLink("  " + name, navigationTarget);
        routerLink.setClassName(className);
        routerLink.addComponentAsFirst(icon);
        horizontalLayout.add(routerLink);

        if(className.equals("logo")) {
            icon.setSize("40px");
        }else {
            icon.setSize("30px");
        }
    }

}
