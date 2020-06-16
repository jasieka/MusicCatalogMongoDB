package com.zbdihd.projectnosql.ui.view;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.zbdihd.projectnosql.model.Artist;
import com.zbdihd.projectnosql.service.CatalogMusicService;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;


@Route(value = "artists", layout = MainView.class)
@PageTitle("Artists View")
@CssImport("./styles/views/artists/artists-view.css")
public class ArtistsView extends Div implements AfterNavigationObserver {

    @Autowired
    private CatalogMusicService catalogMusicService;

    private Grid<Artist> grid;

    //The name of the TextField variable must be the same as the variable corresponding to the field in the Genre class
    private TextField name = new TextField();


    private ComboBox<String> country = new ComboBox<>();

    private DatePicker datePickerBirthDate = new DatePicker();
    private DatePicker datePickerDateOfDeath = new DatePicker();

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private Binder<Artist> binder;

    //Text field to enter the username based on which the gird will be filtered
    private TextField filter = new TextField();

    public ArtistsView() {
        setId("music-labels-view");

        // Configure Grid
        grid = new Grid<>(Artist.class);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setColumns("id", "name","country", "birthDate", "dateOfDeath");
        grid.getColumnByKey("name").setHeader("Artist");



       // grid.getColumnByKey("chairmanOfTheBoard").setHeader("Chairman Of The Board");
       // grid.getColumnByKey("dateOfCreation").setHeader("Date Of Creation");

        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        datePickerBirthDate.setLocale(Locale.UK);
        datePickerDateOfDeath.setLocale(Locale.UK);


        datePickerBirthDate.setMax(LocalDate.now());
        datePickerDateOfDeath.setMax(LocalDate.now());


        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(Artist.class);


        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);



        save.addClickListener(e -> {


            if(validateDates(datePickerBirthDate.getValue(), datePickerDateOfDeath.getValue())) {

                Artist artist = grid.asSingleSelect().getValue();
                if (artist == null) { //create new
                    catalogMusicService.saveArtist(new Artist(name.getValue(),
                            country.getValue(),
                            catalogMusicService.dateToString(datePickerBirthDate.getValue()),
                            catalogMusicService.dateToString(datePickerDateOfDeath.getValue()),
                            new Date()));
                    Notification.show("Artist Added");
                } else {
                    artist.setName(name.getValue());
                    artist.setCountry(country.getValue());
                    artist.setBirthDate(catalogMusicService.dateToString(datePickerBirthDate.getValue()));
                    artist.setDateOfDeath(catalogMusicService.dateToString(datePickerDateOfDeath.getValue()));
                    artist.setLastModifiedAt(new Date());
                    catalogMusicService.saveArtist(artist);
                    Notification.show("Artist Edited");
                }
            }
            else
                Notification.show("Date of birth cannot be after the date of death");
            refreshGrid();

        });

        // the grid valueChangeEvent will clear the form too
        cancel.addClickListener(e -> grid.asSingleSelect().clear());

        delete.addClickListener(e -> {
            Artist artist = catalogMusicService.findArtistByName(name.getValue());
            if(artist != null) {
                catalogMusicService.deleteArtistByName(artist.getName());
                Notification.show("Artist Deleted");
            }

            refreshGrid();
        });

        //filter
        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setPadding(true);
        filter.setPlaceholder("Filter by artist");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listArtist(e.getValue()));


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

        addFormItem(editorDiv, formLayout, name, "Artist name");
        addFormItem(editorDiv, formLayout, country, "Country");
        addFormItem(editorDiv, formLayout, datePickerBirthDate, "Birth Date");
        addFormItem(editorDiv, formLayout, datePickerDateOfDeath, "Date Of Death");


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
        grid.setItems(catalogMusicService.getAllArtists());
    }

    private void populateForm(Artist value) {
        // Value can be null as well, that clears the form
        country.setItems(catalogMusicService.getAllCountries());

        datePickerBirthDate.clear();
        datePickerDateOfDeath.clear();


        binder.readBean(value);

        try {
            datePickerBirthDate.setValue(catalogMusicService.stringToDate(value.getBirthDate()));
            datePickerDateOfDeath.setValue(catalogMusicService.stringToDate(value.getDateOfDeath()));
        }
        catch (NullPointerException ex){
            //ex.printStackTrace();
        }

    }

    private boolean validateDates(LocalDate date1, LocalDate date2)
    {
        if(date1 == null || date2 == null)
            return true;
        else return date2.isAfter(date1);
    }


    void listArtist(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            refreshGrid();
        } else {
            grid.setItems(catalogMusicService.findCustomByRegExName(filterText));
        }
    }

}