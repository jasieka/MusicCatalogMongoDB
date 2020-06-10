package com.zbdihd.projectnosql.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("hello")
@CssImport("./styles/style.css")
public class HelloGui extends VerticalLayout {

    public HelloGui() {
        TextField textFieldName = new TextField("Podaj imie:");
        Button buttonName = new Button("Hello", new Icon(VaadinIcon.AIRPLANE));
        Label labelName = new Label();

        buttonName.addClickListener(buttonClickEvent -> {
           labelName.setText("Hello world " + textFieldName.getValue());
           add(new Image("https://cdn.galleries.smcloud.net/t/galleries/gf-3jz5-pare-pNGd_los-zdjecie-ilustracyjne-664x442-nocrop.jpg", "Problem loading graphics"));
        });

        add(textFieldName, buttonName, labelName);
    }
}
