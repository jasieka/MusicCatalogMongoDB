package com.zbdihd.projectnosql.ui.view;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.service.CatalogMusicService;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Route(value = "genres", layout = MainView.class)
@PageTitle("Genres View")
@CssImport("./styles/views/genres/genres-view.css")
public class GenresView extends Div implements AfterNavigationObserver {

    @Autowired
    private CatalogMusicService catalogMusicService;

    private Grid<Genre> grid;

    //The name of the TextField variable must be the same as the variable corresponding to the field in the Genre class
    private TextField name = new TextField();

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private Binder<Genre> binder;

    //Text field to enter the username based on which the gird will be filtered
    private TextField filter = new TextField();

    public GenresView() {
        setId("genres-view");

        // Configure Grid
        grid = new Grid<>(Genre.class);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setColumns("id", "name");
        grid.getColumnByKey("name").setHeader("Genre");
        grid.addColumn(Genre::numberOfAlbums).setHeader("Number of Albums");

        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);


        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(Genre.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        save.addClickListener(e -> {
            Genre genre = grid.asSingleSelect().getValue();

            if(genre == null){ //create new
                catalogMusicService.saveGenres(new Genre(name.getValue(), new ArrayList<String>()));
                Notification.show("Genre Added");
            }
            else {
                genre.setName(name.getValue());
                catalogMusicService.saveGenres(genre);
                Notification.show("Genre Edited");
            }

            refreshGrid();

        });

        // the grid valueChangeEvent will clear the form too
        cancel.addClickListener(e -> grid.asSingleSelect().clear());

        delete.addClickListener(e -> {
            Genre genre = catalogMusicService.findGenreByName(name.getValue());
            if(genre != null) {
                catalogMusicService.deleteGenreByName(genre.getName());
                Notification.show("Genre Deleted");
            }

            refreshGrid();
        });

        //filter
        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setPadding(true);
        filter.setPlaceholder("Filter by genre name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listGenres(e.getValue()));


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

        addFormItem(editorDiv, formLayout, name, "Genre name");

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
        grid.setItems(catalogMusicService.getAllGenres());
    }

    private void populateForm(Genre value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);
    }

    void listGenres(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            refreshGrid();
        } else {
            grid.setItems(catalogMusicService.findByGenreNameStartsWithIgnoreCase(filterText));
        }
    }

}