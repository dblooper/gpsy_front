package com.gpsy_front;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@Route(value = "lyrics", layout = MainViewWithMainBar.class)
public class Lyrics extends Div implements RouterLayout {
    Text text = new Text("Fasss");
    public Lyrics() {
        add(text);
    }
}


