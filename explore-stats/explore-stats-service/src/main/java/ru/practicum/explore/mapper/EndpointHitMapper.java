package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.model.App;
import ru.practicum.explore.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit mapFromDto(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(endpointHitDto.getId())
                .app(new App(endpointHitDto.getApp()))
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
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