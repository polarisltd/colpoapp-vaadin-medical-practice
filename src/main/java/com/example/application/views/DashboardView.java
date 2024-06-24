package com.example.application.views;

import com.example.application.data.SharedData;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class) // <1>
@PageTitle("Dashboard | Colposcope app")
public class DashboardView extends VerticalLayout {
    private final CrmService service;

    public DashboardView(CrmService service, SharedData sharedData) { // <2>
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // <3>

        H2 formTitle = (sharedData.getSelectedDoctor()!=null)?
                new H2("Hi Dr. "+sharedData.getSelectedDoctor().getVardsUzvardsDakteris()):
                new H2("Dashboard1");
        add(formTitle, getContactStats());
    }

    private Component getContactStats() {
        Span stats = new Span(service.countContacts() + " contacts"); // <4>
        stats.addClassNames(
            LumoUtility.FontSize.XLARGE,
            LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

//    private Chart getCompaniesChart() {
//        Chart chart = new Chart(ChartType.PIE);
//
//        DataSeries dataSeries = new DataSeries();
//        service.findAllCompanies().forEach(company ->
//            dataSeries.add(new DataSeriesItem(company.getName(), company.getEmployeeCount()))); // <5>
//        chart.getConfiguration().setSeries(dataSeries);
//        return chart;
//    }
}
