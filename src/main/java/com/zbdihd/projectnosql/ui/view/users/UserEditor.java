package com.zbdihd.projectnosql.ui.view.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.zbdihd.projectnosql.model.Role;
import com.zbdihd.projectnosql.model.User;
import com.zbdihd.projectnosql.repository.UserRepository;
import com.zbdihd.projectnosql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringComponent // @SpringComponent is just an alias to Springs @Component annotation to avoid conflicts with Vaadins Component class.
@UIScope //The @UIScope binds the bean to the current Vaadin UI.
public class UserEditor extends VerticalLayout implements KeyNotifier {

    private final UserRepository repository;
    private User user;

    private final UserService userService;

    TextField username = new TextField("Username");
    TextField password = new TextField("Password");

    Checkbox enabled = new Checkbox("Enabled");

    Label labelRoles = new Label("Roles");
    ListBox<String> listBoxRoles = new ListBox<>();


    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<User> binder = new Binder<>(User.class);
    private ChangeHandler changeHandler;


    @Autowired
    public UserEditor(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;

        add(username, password, enabled, labelRoles, listBoxRoles, actions);


        List<String> list = new ArrayList<String>();
        userService.getAllRoles().forEach(e -> list.add(e.getRole()));
        listBoxRoles.setItems(list);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());


        save.addClickListener(e -> {
            user.setPassword(userService.getEncryptedPassword(password.getValue()));

            Role role = userService.getRole(listBoxRoles.getValue());
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);

            save();
        });

        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editUser(user));
        setVisible(false);
    }



    void delete() {
        repository.delete(user);
        changeHandler.onChange();
    }

    void save() {
        repository.save(user);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editUser(User c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            user = repository.findById(c.getId()).get();
        } else {
            user = c;
        }

        cancel.setVisible(persisted);
        binder.setBean(user);
        listBoxRoles.setValue(user.getRoles().iterator().next().getRole());
        setVisible(true);
        username.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
