package com.example.application.views;

import com.example.application.data.KolposkopijaIzmeklejumsEntity;
import com.example.application.data.SharedData;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "", layout = MainLayout.class) // <1>
@PageTitle("Dashboard | Colposcope app")
public class DashboardView extends VerticalLayout {
    private final CrmService service;

    public DashboardView(CrmService service, SharedData sharedData) { // <2>
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // <3>

        H2 formTitle =
                new H2("Apmeklējumu statistika");
        add(formTitle,getVisitStats());
    }

    private Component getVisitStats() {
        Instant startDate = LocalDate.now().minusDays(45).atStartOfDay(ZoneId.systemDefault()).toInstant();
        List<KolposkopijaIzmeklejumsEntity> visits = service.findAllByVisitDateAfter(startDate);
//
         Map<LocalDate, Long> visitCounts = visits.stream()
                 .collect(Collectors.groupingBy(visit -> visit.getIzmeklejumaDatums().atZone(ZoneId.systemDefault())
                         .toLocalDate(), Collectors.counting()));

        Div container = new Div();
        container.addClassName("visit-stats-container");

        visitCounts.forEach((date, count) -> {
            Div card = new Div();
            card.addClassName("visit-card");

            var countSpan = new H2(count.toString() );
            countSpan.getStyle().set("font-weight", "bold");

            Span dateSpan = new Span(date.format(DateTimeFormatter.ofPattern("dd/MM")));

            card.add(countSpan, dateSpan);
            container.add(card);
        });

        return container;
    }

}
