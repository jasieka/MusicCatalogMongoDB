package com.zbdihd.projectnosql.ui.view.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.zbdihd.projectnosql.model.User;
import com.zbdihd.projectnosql.repository.UserRepository;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.util.StringUtils;

@Route(value="users", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Users View")
public class UsersView extends VerticalLayout {
    private final UserRepository userRepository;

    //Shows the User form used to provide employee information to create and edit.
    private final UserEditor editor;

    //Grid to display the list of Users
    Grid<User> grid;

    //Text field to enter the username based on which the gird will be filtered
    TextField filter;

    //Button to add a new User. Displays the UserEditor editor.
    private Button addNewBtn;


    public UsersView(UserRepository repo, UserEditor editor) {
        this.userRepository = repo;
        this.editor = editor;

        this.grid = new Grid<>(User.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New user", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("200px");
        grid.setColumns("id", "username", "password", "enabled");
        grid.getColumnByKey("password").setHeader("Encrypted password");
        grid.addColumn(User::setRolesToString).setHeader("Roles");

        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter by username");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listUsers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editUser(e.getValue());
        });


        addNewBtn.addClickListener(e -> editor.editUser(new User()));


        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listUsers(filter.getValue());
        });

        listUsers(null);

    }


    void listUsers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(userRepository.findAll());
        } else {
            grid.setItems(userRepository.findByUsernameStartsWithIgnoreCase(filterText));
        }
    }
}