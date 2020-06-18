package com.zbdihd.projectnosql.ui.view;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.zbdihd.projectnosql.model.Album;
import com.zbdihd.projectnosql.service.CatalogMusicService;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.*;


@Route(value = "albums", layout = MainView.class)
@PageTitle("Albums View")
@CssImport("./styles/views/albums/albums-view.css")
public class AlbumsView extends Div implements AfterNavigationObserver {

    @Autowired
    private CatalogMusicService catalogMusicService;

    private Grid<Album> grid;

    //The name of the TextField variable must be the same as the variable corresponding to the field in the Genre class
    private TextField name = new TextField();

    private TextField imageURL = new TextField();
    private IntegerField releaseYear = new IntegerField();
    private IntegerField numberOfCDs = new IntegerField();
    private TextArea description = new TextArea();

    private ComboBox<String> comboBoxArtist = new ComboBox<>();
    private ComboBox<String> comboBoxMusicLabel = new ComboBox<>();


    private NumberField averageRating = new NumberField();
    private IntegerField rating = new IntegerField();


    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private Binder<Album> binder;

    //Text field to enter the username based on which the gird will be filtered
    private TextField filter = new TextField();

    public AlbumsView() {
        setId("albums-view");

        // Configure Grid
        grid = new Grid<>(Album.class);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setColumns("id", "name", "averageRating", "imageURL", "releaseYear", "numberOfCDs", "description");
        grid.getColumnByKey("name").setHeader("Album");
        grid.getColumnByKey("numberOfCDs").setHeader("Number Of CD's");
        grid.addColumn(Album::getArtistName).setHeader("Artist");
        grid.addColumn(Album::getMusicLabelName).setHeader("Music Label");

        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        grid.getColumnByKey("averageRating").setWidth("50px");
        grid.getColumnByKey("releaseYear").setWidth("50px");
        grid.getColumnByKey("numberOfCDs").setWidth("50px");


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        releaseYear.setHasControls(true);
        releaseYear.setMin(1900);
        releaseYear.setMax(calendar.get(Calendar.YEAR));

        numberOfCDs.setHasControls(true);
        numberOfCDs.setMin(1);
        numberOfCDs.setMax(10);

        description.getStyle().set("maxHeight", "150px");

        averageRating.setReadOnly(true);

        rating.setHasControls(true);
        rating.setPlaceholder("0 - 10");
        rating.setMin(0);
        rating.setMax(10);


        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(Album.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        save.addClickListener(e -> {


            Album album = grid.asSingleSelect().getValue();
            if (album == null) { //create new
                List<String> tracks = new ArrayList<String>();
                HashMap<String, Integer> ratings = new HashMap<String, Integer>();
                ratings.put(getLoggedUsername(), rating.getValue());

                catalogMusicService.saveAlbum(new Album(name.getValue(),
                                                        catalogMusicService.findArtistByName(comboBoxArtist.getValue()),
                                                        tracks,
                                                        ratings,
                                                        rating.getValue(),
                                                        imageURL.getValue(),
                                                        catalogMusicService.findMusicLabelByName(comboBoxMusicLabel.getValue()),
                                                        releaseYear.getValue(),
                                                        numberOfCDs.getValue(),
                                                        description.getValue(),
                                                        new Date()));
                    Notification.show("Album Added");
                }
            else {
                album.setName(name.getValue());
                album.setArtist(catalogMusicService.findArtistByName(comboBoxArtist.getValue()));
                //album.setTracks();
                if(rating.getValue() == null)
                    rating.setValue(0);
                try {
                    if(!album.getRatings().containsKey(getLoggedUsername())) //The album exists but is not rated
                        album.getRatings().put(getLoggedUsername(), rating.getValue());
                     else //The album exists rated. We want to change the rating
                       album.getRatings().computeIfPresent(getLoggedUsername(), (k, v) -> rating.getValue());

                } catch (NullPointerException ignored) { }

                album.refreshAverageRating();
                album.setImageURL(imageURL.getValue());
                album.setMusicLabel(catalogMusicService.findMusicLabelByName(comboBoxMusicLabel.getValue()));
                album.setReleaseYear(releaseYear.getValue());
                album.setNumberOfCDs(numberOfCDs.getValue());
                album.setDescription(description.getValue());
                album.setLastModifiedAt(new Date());
                catalogMusicService.saveAlbum(album);
                Notification.show("Album Edited");
            }

            refreshGrid();

        });

        // the grid valueChangeEvent will clear the form too
        cancel.addClickListener(e -> grid.asSingleSelect().clear());

        delete.addClickListener(e -> {
            Album album = catalogMusicService.findAlbumByName(name.getValue());
            if(album != null) {
                catalogMusicService.removeAlbumReferenceFromGenre(album);
                catalogMusicService.deleteAlbumByName(album.getName());
                Notification.show("Album Deleted");
            }

            refreshGrid();
        });

        //filter
        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setPadding(true);
        filter.setPlaceholder("Filter by album");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listAlbums(e.getValue()));


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

        addFormItem(editorDiv, formLayout, name, "Album name");
        addFormItem(editorDiv, formLayout, imageURL, "Image URL");
        addFormItem(editorDiv, formLayout, releaseYear, "Release Year");
        addFormItem(editorDiv, formLayout, numberOfCDs, "Number Of CDs");
        addFormItem(editorDiv, formLayout, description, "Description");
        addFormItem(editorDiv, formLayout, comboBoxArtist, "Artist");
        addFormItem(editorDiv, formLayout, comboBoxMusicLabel, "Music Label");
        addFormItem(editorDiv, formLayout, averageRating, "Average Rating");
        addFormItem(editorDiv, formLayout, rating, "Your rating");


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
        grid.setItems(catalogMusicService.getAllAlbums());
    }

    private void populateForm(Album value) {
        // Value can be null as well, that clears the form

        rating.clear();

        binder.readBean(value);


        try {
            rating.setValue(value.getRatings().get(getLoggedUsername()));
        } catch (NullPointerException ignored){ }



        try {
            List<String> listArtistsName = new ArrayList<String>();
            catalogMusicService.getAllArtists().forEach(e -> listArtistsName.add(e.getName()));
            comboBoxArtist.setItems(listArtistsName);
            if(value.getArtist() != null)
                comboBoxArtist.setValue(value.getArtistName());
        } catch (NullPointerException ignored){ }

        try {
            List<String> listMusicLabelsName = new ArrayList<String>();
            catalogMusicService.getAllMusicLabels().forEach(e -> listMusicLabelsName.add(e.getName()));
            comboBoxMusicLabel.setItems(listMusicLabelsName);

            if(value.getMusicLabel() != null)
                comboBoxMusicLabel.setValue(value.getMusicLabelName());
        }
        catch (NullPointerException ignored){ }

    }

    private String getLoggedUsername(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        return auth.getName();
    }

    void listAlbums(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            refreshGrid();
        } else {
            grid.setItems(catalogMusicService.findCustomAlbumByRegExName(filterText));
        }
    }

}