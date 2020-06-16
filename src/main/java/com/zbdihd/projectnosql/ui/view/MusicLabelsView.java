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
import com.zbdihd.projectnosql.model.MusicLabel;
import com.zbdihd.projectnosql.service.CatalogMusicService;
import com.zbdihd.projectnosql.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Route(value = "music-labels", layout = MainView.class)
@PageTitle("Music Labels View")
@CssImport("./styles/views/musicLabels/music-labels-view.css")
public class MusicLabelsView extends Div implements AfterNavigationObserver {

    @Autowired
    private CatalogMusicService catalogMusicService;

    private Grid<MusicLabel> grid;

    //The name of the TextField variable must be the same as the variable corresponding to the field in the Genre class
    private TextField name = new TextField();

    
    private ComboBox<String> countryOfResidence = new ComboBox<>();
    private TextField chairmanOfTheBoard = new TextField();
    private DatePicker datePickerDateOfCreation = new DatePicker();

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private Binder<MusicLabel> binder;

    //Text field to enter the username based on which the gird will be filtered
    private TextField filter = new TextField();

    public MusicLabelsView() {
        setId("music-labels-view");

        // Configure Grid
        grid = new Grid<>(MusicLabel.class);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setColumns("id", "name","countryOfResidence", "chairmanOfTheBoard", "dateOfCreation");
        grid.getColumnByKey("name").setHeader("Music Label");

        grid.getColumnByKey("countryOfResidence").setHeader("Country Of Residence");
        grid.getColumnByKey("chairmanOfTheBoard").setHeader("Chairman Of The Board");
        grid.getColumnByKey("dateOfCreation").setHeader("Date Of Creation");

        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        datePickerDateOfCreation.setLocale(Locale.UK);
        datePickerDateOfCreation.setMax(LocalDate.now());


        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(MusicLabel.class);


        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);



        save.addClickListener(e -> {
            MusicLabel musicLabel = grid.asSingleSelect().getValue();

            if(musicLabel == null){ //create new
                catalogMusicService.saveMusicLabel(new MusicLabel(name.getValue(),
                                                    catalogMusicService.dateToString(datePickerDateOfCreation.getValue()),
                                                    countryOfResidence.getValue(),
                                                    chairmanOfTheBoard.getValue(),
                                                    new Date()));
                Notification.show("Music Label Added");
            }
            else {
                musicLabel.setName(name.getValue());
                musicLabel.setDateOfCreation(catalogMusicService.dateToString(datePickerDateOfCreation.getValue()));
                musicLabel.setCountryOfResidence(countryOfResidence.getValue());
                musicLabel.setChairmanOfTheBoard(chairmanOfTheBoard.getValue());
                musicLabel.setLastModifiedAt(new Date());
                catalogMusicService.saveMusicLabel(musicLabel);
                Notification.show("Music Label Edited");
            }

            refreshGrid();

        });

        // the grid valueChangeEvent will clear the form too
        cancel.addClickListener(e -> grid.asSingleSelect().clear());

        delete.addClickListener(e -> {
            MusicLabel musicLabel = catalogMusicService.findMusicLabelByName(name.getValue());
            if(musicLabel != null) {
                catalogMusicService.deleteMusicLabelByName(musicLabel.getName());
                Notification.show("Music Label Deleted");
            }

            refreshGrid();
        });

        //filter
        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setPadding(true);
        filter.setPlaceholder("Filter by music label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listMusicLabels(e.getValue()));


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

        addFormItem(editorDiv, formLayout, name, "Music Label name");
        addFormItem(editorDiv, formLayout, countryOfResidence, "Country Of Residence");
        addFormItem(editorDiv, formLayout, chairmanOfTheBoard, "Chairman Of The Board");
        addFormItem(editorDiv, formLayout, datePickerDateOfCreation, "Date Of Creation");


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
        grid.setItems(catalogMusicService.getAllMusicLabels());
    }

    private void populateForm(MusicLabel value) {
        // Value can be null as well, that clears the form
        countryOfResidence.setItems(catalogMusicService.getAllCountries());
        datePickerDateOfCreation.clear();
        binder.readBean(value);

        try {
            datePickerDateOfCreation.setValue(catalogMusicService.stringToDate(value.getDateOfCreation()));
        }
        catch (NullPointerException ex){
            //ex.printStackTrace();
        }

    }



    void listMusicLabels(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            refreshGrid();
        } else {
            grid.setItems(catalogMusicService.findByMusicLabelNameStartsWithIgnoreCase(filterText));
        }
    }

}