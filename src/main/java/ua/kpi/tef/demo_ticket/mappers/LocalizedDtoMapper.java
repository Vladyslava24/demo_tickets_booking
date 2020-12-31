package ua.kpi.tef.demo_ticket.mappers;


@FunctionalInterface
public interface LocalizedDtoMapper <D, E>{
    D map(E e);
}
