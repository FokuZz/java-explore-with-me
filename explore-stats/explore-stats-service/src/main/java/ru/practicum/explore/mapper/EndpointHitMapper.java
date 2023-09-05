package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.model.App;
import ru.practicum.explore.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit mapFromDto(EndpointHitDto EndpointHitDto) {
        return EndpointHit.builder()
                .id(EndpointHitDto.getId())
                .app(new App(EndpointHitDto.getApp()))
                .uri(EndpointHitDto.getUri())
                .ip(EndpointHitDto.getIp())
                .timestamp(EndpointHitDto.getTimestamp())
                .build();
    }

    public static EndpointHitDto mapToDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp().getAppName())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }
}