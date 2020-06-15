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
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.service.CatalogMusicService;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;

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
    private TextField numberOfAlbums = new TextField();


    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");

    private Binder<Genre> binder;

    public GenresView() {
        setId("genres-view");

        // Configure Grid
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        grid.addColumn(Genre::getName).setHeader("Genre");
        grid.addColumn(Genre::numberOfAlbums).setHeader("Number of Albums");


        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(Genre.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);
        // note that password field isn't bound since that property doesn't exist in

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
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
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
        buttonLayout.add(save, cancel);
        editorDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
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
        // shown to the user
        refreshGrid();
    }

    public void refreshGrid(){
        grid.setItems(catalogMusicService.getAllGenres());
    }

    private void populateForm(Genre value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);
    }

}