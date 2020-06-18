package com.zbdihd.projectnosql.ui.view;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.zbdihd.projectnosql.model.Role;
import com.zbdihd.projectnosql.model.User;
import com.zbdihd.projectnosql.service.UserService;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Route(value="users", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Users View")
@CssImport("./styles/views/users/users-view.css")
public class UsersView extends Div implements AfterNavigationObserver {

    @Autowired
    private UserService userService;

    private Grid<User> grid;

    private TextField username = new TextField();
    private PasswordField password = new PasswordField();

    private Checkbox enabled = new Checkbox();
    private Select<String> selectRoles = new Select<>();

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private Binder<User> binder;

    //Text field to enter the username based on which the gird will be filtered
    private TextField filter = new TextField();

    public UsersView() {
        setId("users-view");

        // Configure Grid
        grid = new Grid<>(User.class);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setColumns("id", "username","password", "enabled");
        grid.getColumnByKey("password").setHeader("Encrypted password");
        grid.addColumn(User::setRolesToString).setHeader("Roles");

        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);


        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(User.class);


        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);



        save.addClickListener(e -> {

            User user = grid.asSingleSelect().getValue();


            Set<Role> roles = new HashSet<>();
            Role role = userService.getRole(selectRoles.getValue());
            roles.add(role);

            if (user == null) { //create new
                userService.saveUser(new User(username.getValue(),
                                            userService.getEncryptedPassword(password.getValue()),
                                            enabled.getValue(),
                                            roles));
                Notification.show("User Added");
            } else {
                user.setUsername(username.getValue());
                user.setPassword(userService.getEncryptedPassword(password.getValue()));
                user.setEnabled(enabled.getValue());
                user.setRoles(roles);

                userService.saveUser(user);
                Notification.show("User Edited");
            }
            refreshGrid();

        });

        // the grid valueChangeEvent will clear the form too
        cancel.addClickListener(e -> grid.asSingleSelect().clear());

        delete.addClickListener(e -> {
            User user = userService.findUserByUsername(username.getValue());
            if(user != null) {
                userService.deleteUserByUsername(user.getUsername());
                Notification.show("User Deleted");
            }

            refreshGrid();
        });

        //filter
        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setPadding(true);
        filter.setPlaceholder("Filter by username");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listUsers(e.getValue()));


        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(splitLayout);
        verticalLayout.setPadding(true);

        add(actions, verticalLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorDiv = new Div();
        editorDiv.setId("editor-layout");
        FormLayout formLayout = new FormLayout();

        addFormItem(editorDiv, formLayout, username, "Username");
        addFormItem(editorDiv, formLayout, password, "Password");
        addFormItem(editorDiv, formLayout, enabled, "Enabled");
        addFormItem(editorDiv, formLayout, selectRoles, "Role");

        createButtonLayout(editorDiv);
        splitLayout.addToSecondary(editorDiv);
    }

    private void createButtonLayout(Div editorDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        buttonLayout.add(save, cancel, delete);
        editorDiv.add(buttonLayout);
    }

    private void addFormItem(Div wrapper, FormLayout formLayout,
                             AbstractField field, String fieldName) {
        formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Lazy init of the grid items, happens only when we are sure the view will be
        refreshGrid();
    }

    public void refreshGrid(){
        grid.setItems(userService.getAllUsers());
    }

    private void populateForm(User value) {
        // Value can be null as well, that clears the form



        binder.readBean(value);


        List<String> rolesInString = new ArrayList<>();
        userService.getAllRoles().forEach(e -> rolesInString.add(e.getRole()));

        selectRoles.setItems(rolesInString);

        List<Role> role = new ArrayList<>(value.getRoles());
        selectRoles.setValue(role.get(0).getRole());
        password.setValue("");

        /*
        try {
            datePickerBirthDate.setValue(catalogMusicService.stringToDate(value.getBirthDate()));
            datePickerDateOfDeath.setValue(catalogMusicService.stringToDate(value.getDateOfDeath()));
        }
        catch (NullPointerException ex){
            //ex.printStackTrace();
        }*/

    }

    void listUsers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            refreshGrid();
        } else {
            grid.setItems(userService.findCustomByRegExUsername(filterText));
        }
    }

}