package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Locale;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Colposcope app");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout()); // <2>

        var header = new HorizontalLayout(new DrawerToggle(), logo, languageSelector(), logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo); // <4>
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header); 

    }

    public Component languageSelector() {
        Select<Locale> languageSelect = new Select<>();
        languageSelect.setLabel(getTranslation("language"));
        languageSelect.setItems(Locale.ENGLISH, new Locale("es", "ES")); // Add more locales as needed
        languageSelect.setItemLabelGenerator(Locale::getDisplayLanguage);
        languageSelect.addValueChangeListener(event -> {
            UI.getCurrent().setLocale(event.getValue());
            UI.getCurrent().getPage().reload(); // Reload the page to apply the new locale
        });

        return languageSelect;
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                //new RouterLink("List", ListView.class),
                new RouterLink("Dashboard", DashboardView.class),
                new RouterLink("PdfViewer", PdfViewerView.class),
                new RouterLink("Doctor Selector", DrSelectorView.class),
                new RouterLink("Add Visit", PatientVisitView.class)
        ));
    }
}