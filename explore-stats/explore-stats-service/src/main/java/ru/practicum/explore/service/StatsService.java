package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.mapper.EndpointHitMapper;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.repository.HitRepository;
import ru.practicum.explore.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StatsService {
    private final StatsRepository statsRepository;
    private final HitRepository hitRepository;

    @Autowired
    public StatsService(StatsRepository statsRepository, HitRepository hitRepository) {
        this.statsRepository = statsRepository;
        this.hitRepository = hitRepository;
    }

    public EndpointHitDto addEndpointHit(EndpointHitDto endpointHit) {
        log.info("Adding endpoint hit: {}.", endpointHit);
        EndpointHit savedEndpoint = hitRepository.addHit(EndpointHitMapper.mapFromDto(endpointHit));
        log.info("Endpoint added: {}.", savedEndpoint);
        return EndpointHitMapper.mapToDto(savedEndpoint);
    }

    public List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uriList, Boolean unique) {
        log.info("Getting hit from {} to {}. Unique {}", start, end, unique);
        List<ViewStatsDto> hits;
        if (unique) {
            hits = getUniqueHits(start, end, uriList);
        } else {
            hits = getAllHits(start, end, uriList);
        }
        log.info("Query result: {}", hits);
        return hits;
    }

    private List<ViewStatsDto> getUniqueHits(LocalDateTime start, LocalDateTime end, List<String> uriList) {
        List<ViewStatsDto> hits;
        if (uriList == null) {
            log.info("Uri list is null");
            hits = statsRepository.getUniqueHits(start, end);
        } else {
            log.info("Uri list: {}", uriList);
            hits = statsRepository.getUniqueHitsByUri(start, end, uriList);
        }
        return hits;
    }

    private List<ViewStatsDto> getAllHits(LocalDateTime start, LocalDateTime end, List<String> uriList) {
        List<ViewStatsDto> hits;
        if (uriList == null) {
            log.info("Uri list is null");
            hits = statsRepository.getAllHits(start, end);
        } else {
            log.info("Uri list: {}", uriList);
            hits = statsRepository.getAllHitsByUri(start, end, uriList);
        }
        return hits;
    }
}
