package ru.practicum.explore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.model.App;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.repository.HitRepository;
import ru.practicum.explore.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {
    @Mock
    private StatsRepository statsRepository;
    @Mock
    private HitRepository hitRepository;

    @InjectMocks
    private StatsService statsService;
    private EndpointHitDto endpointHitDto;
    private EndpointHit endpointHit;

    @BeforeEach
    public void init() {
        endpointHitDto = EndpointHitDto.builder()
                .id(1L)
                .app("app")
                .uri("/uri")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.now())
                .build();

        endpointHit = EndpointHit.builder()
                .id(1L)
                .app(new App(endpointHitDto.getApp()))
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    @Test
    public void addEndpoint_Normal() {
        when(hitRepository.addHit(any())).thenReturn(endpointHit);

        EndpointHitDto result = statsService.addEndpointHit(endpointHitDto);

        assertEquals(endpointHitDto, result);
    }

    @Test
    public void getHitsAllWithUriList_Normal() {
        List<ViewStatsDto> hits = new ArrayList<>();
        ViewStatsDto hit = ViewStatsDto.builder()
                .app(endpointHit.getApp().getAppName())
                .uri(endpointHit.getUri())
                .hits(1)
                .build();
        hits.add(hit);

        when(statsRepository.getAllHitsByUri(any(), any(), any())).thenReturn(hits);

        List<ViewStatsDto> result = statsService.getHits(LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().minusMinutes(1), new ArrayList<>(), false);

        assertEquals(hits, result);
        verify(statsRepository, times(1)).getAllHitsByUri(any(), any(), any());
    }

    @Test
    public void getHitsAllUriListNull_Normal() {
        List<ViewStatsDto> hits = new ArrayList<>();
        ViewStatsDto hit = ViewStatsDto.builder()
                .app(endpointHit.getApp().getAppName())
                .uri(null)
                .hits(1)
                .build();
        hits.add(hit);

        when(statsRepository.getAllHits(any(), any())).thenReturn(hits);

        List<ViewStatsDto> result = statsService.getHits(LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().minusMinutes(1), null, false);

        assertEquals(hits, result);
        verify(statsRepository, times(1)).getAllHits(any(), any());
    }

    @Test
    public void getHitsUnique_Normal() {
        List<ViewStatsDto> hits = new ArrayList<>();
        ViewStatsDto hit = ViewStatsDto.builder()
                .app(endpointHit.getApp().getAppName())
                .uri(endpointHit.getUri())
                .hits(1)
                .build();
        hits.add(hit);

        when(statsRepository.getUniqueHitsByUri(any(), any(), any())).thenReturn(hits);

        List<ViewStatsDto> result = statsService.getHits(LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().minusMinutes(1), new ArrayList<>(), true);

        assertEquals(hits, result);
        verify(statsRepository, times(1)).getUniqueHitsByUri(any(), any(), any());
    }

    @Test
    public void getHitsUniqueUriListNull_Normal() {
        List<ViewStatsDto> hits = new ArrayList<>();
        ViewStatsDto hit = ViewStatsDto.builder()
                .app(endpointHit.getApp().getAppName())
                .uri(null)
                .hits(1)
                .build();
        hits.add(hit);

        when(statsRepository.getUniqueHits(any(), any())).thenReturn(hits);

        List<ViewStatsDto> result = statsService.getHits(LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().minusMinutes(1), null, true);

        assertEquals(hits, result);
        verify(statsRepository, times(1)).getUniqueHits(any(), any());
    }

}