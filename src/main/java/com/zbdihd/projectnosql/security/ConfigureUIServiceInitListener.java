package com.zbdihd.projectnosql.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.zbdihd.projectnosql.ui.view.LoginView;
import org.springframework.stereotype.Component;

/*
Spring Security restricts access to content based on paths. Vaadin applications are single-page
applications. This means that they do not trigger a full browser refresh when you navigate between
views, even though the path does change.To secure a Vaadin application, we need to wire Spring
Security to the Vaadin navigation system.
 */
@Component //annotation registers the listener. Vaadin will pick it up on startup.
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {


    //we listen for the initialization of the UI (the internal root component in Vaadin) and then add a listener before every view transition.
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    //we reroute all requests to the login, if the user is not logged in
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}