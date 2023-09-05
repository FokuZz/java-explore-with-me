package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class StatsController {
    private final StatsService statsService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto addEndpointHit(@RequestBody @Validated EndpointHitDto endpointHitRequestDto) {
        log.info("Adding endpoint hit: {}.", endpointHitRequestDto);
        EndpointHitDto endpointHit = statsService.addEndpointHit(endpointHitRequestDto);
        log.info("Endpoint hit added: {}.", endpointHit);
        return endpointHit;
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                                 @RequestParam String end,
                                                 @RequestParam(required = false) List<String> uris,
                                                 @RequestParam(required = false, defaultValue = "false") Boolean unique) {

        LocalDateTime startDate = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(end, dateTimeFormatter);

        if (startDate.isAfter(endDate)) {
            log.warn("Start date {} must be before end date {}.", startDate, endDate);
            throw new IllegalArgumentException(String.format("Start date %s must be before end date %s.", startDate, endDate));
        }
        log.info("Getting hits from {} to {} for uris: {}. Unique: {}", startDate, endDate, uris, unique);
        List<ViewStatsDto> hits = statsService.getHits(startDate, endDate, uris, unique);
        log.info("Hits: {}", hits);
        return hits;
    }
}
