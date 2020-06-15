package com.zbdihd.projectnosql.ui;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.zbdihd.projectnosql.ui.view.GenresView;
import com.zbdihd.projectnosql.ui.view.users.UsersView;
import com.zbdihd.projectnosql.ui.view.HelloGui;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

public class MainView extends AppLayout implements BeforeEnterObserver {

    private Tabs tabs = new Tabs();
    private Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();


    public MainView()  {

        Image img = new Image("https://i.ibb.co/L5B5hJn/465-removebg-preview.png", "Music Catalog Logo");
        img.setHeight("44px");

        addMenuTab("Users", UsersView.class);
        addMenuTab("Genres", GenresView.class);
        addMenuTab("Hello World", HelloGui.class);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        Div b = new Div();
        b.setText("Logged in as: " + auth.getName() + "\t");
        Button btnSignOut = new Button("Sign Out", new Icon(VaadinIcon.SIGN_OUT));

        btnSignOut.addClickListener(buttonClickEvent -> {
            //SecurityContextHolder.clearContext();
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().navigate("login");
            //UI.getCurrent().getPage().executeJavaScript("window.location.href=''");
        });

        b.add(btnSignOut);

        b.getStyle().set("margin-left", "auto");
        b.getStyle().set("padding", "15px");

        addToNavbar(img, tabs, b);
    }


    private void addMenuTab(String label, Class<? extends Component> target) {
        Tab tab = new Tab(new RouterLink(label, target));
        navigationTargetToTab.put(target, tab);
        tabs.add(tab);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tabs.setSelectedTab(navigationTargetToTab.get(event.getNavigationTarget()));
    }
}