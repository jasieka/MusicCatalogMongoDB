package com.zbdihd.projectnosql.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "hello", layout = MainView.class)
@PageTitle("Hello World!")
@CssImport("./styles/style.css")
public class HelloGui extends VerticalLayout {


    public HelloGui() {
        TextField textFieldName = new TextField("Podaj imie:");
        Button buttonName = new Button("Hello", new Icon(VaadinIcon.AIRPLANE));
        Label labelName = new Label();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        Label labelName2 = new Label("Logged in as: " + auth.getName());

        buttonName.addClickListener(buttonClickEvent -> {
           labelName.setText("Hello world " + textFieldName.getValue());
           add(new Image("https://cdn.galleries.smcloud.net/t/galleries/gf-3jz5-pare-pNGd_los-zdjecie-ilustracyjne-664x442-nocrop.jpg", "Problem loading graphics"));
        });

        add(textFieldName, buttonName, labelName, labelName2);
    }
}
