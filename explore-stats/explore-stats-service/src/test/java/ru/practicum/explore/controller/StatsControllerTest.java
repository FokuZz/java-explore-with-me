package ru.practicum.explore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.service.StatsService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = {StatsController.class, ErrorHandler.class})
public class StatsControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;
    private EndpointHitDto endpointHitDto;
    private List<ViewStatsDto> hits;

    @BeforeEach
    public void init() {
        endpointHitDto = EndpointHitDto.builder()
                .id(1L)
                .app("testApp")
                .uri("/test")
                .ip("192.168.1.0")
                .timestamp(LocalDateTime.parse("2022-09-06T11:00:23"))
                .build();

        ViewStatsDto viewStatsDto = ViewStatsDto.builder()
                .app("app")
                .uri("/test")
                .hits(2)
                .build();

        hits = List.of(viewStatsDto);
    }

    @SneakyThrows
    @Test
    public void addEndpointHit_Normal() {
        when(statsService.addEndpointHit(any())).thenReturn(endpointHitDto);
        String json =
                "{\n" +
                        "  \"app\": \"testApp\",\n" +
                        "  \"uri\": \"/test\",\n" +
                        "  \"ip\": \"192.168.1.0\",\n" +
                        "  \"timestamp\": \"2022-09-06 11:00:23\"\n" +
                        "}";

        String result = mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statsService, times(1)).addEndpointHit(any());
        assertEquals(objectMapper.writeValueAsString(endpointHitDto), result);
    }

    @SneakyThrows
    @Test
    public void addEndpointHit_EmptyApp() {
        endpointHitDto.setApp("");

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHitDto)))
                .andExpect(status().isBadRequest());

        verify(statsService, never()).addEndpointHit(any());

        endpointHitDto.setApp("   ");
        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHitDto)))
                .andExpect(status().isBadRequest());

        verify(statsService, never()).addEndpointHit(any());
    }

    @SneakyThrows
    @Test
    public void addEndpointHit_EmptyUri() {
        endpointHitDto.setUri("");

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHitDto)))
                .andExpect(status().isBadRequest());

        verify(statsService, never()).addEndpointHit(any());

        endpointHitDto.setUri("   ");
        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHitDto)))
                .andExpect(status().isBadRequest());

        verify(statsService, never()).addEndpointHit(any());
    }

    @SneakyThrows
    @Test
    public void addEndpointHit_EmptyIp() {
        endpointHitDto.setIp("");

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHitDto)))
                .andExpect(status().isBadRequest());

        verify(statsService, never()).addEndpointHit(any());

        endpointHitDto.setIp("   ");
        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHitDto)))
                .andExpect(status().isBadRequest());

        verify(statsService, never()).addEndpointHit(any());
    }


    @SneakyThrows
    @Test
    public void addEndpointHit_TimeStampInPast() {
        endpointHitDto.setTimestamp(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointHitDto)))
                .andExpect(status().isBadRequest());

        verify(statsService, never()).addEndpointHit(any());
    }

    @SneakyThrows
    @Test
    public void getStats_Normal() {
        when(statsService.getHits(any(), any(), any(), any())).thenReturn(hits);
        String start = "2020-07-30 00:00:00";
        String end = "2025-07-30 00:00:00";

        String result = mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", "/test, /test1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statsService, times(1)).getHits(any(), any(), any(), any());
        assertEquals(objectMapper.writeValueAsString(hits), result);
    }

    @SneakyThrows
    @Test
    public void getStats_WrongStart() {
        String start = URLEncoder.encode("2020-07-30T00:00:00", StandardCharsets.UTF_8);
        String end = URLEncoder.encode("2025-07-30 00:00:00", StandardCharsets.UTF_8);

        mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", "/test, /test1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statsService, never()).getHits(any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    public void getStats_WrongEnd() {
        String start = URLEncoder.encode("2020-07-30 00:00:00", StandardCharsets.UTF_8);
        String end = URLEncoder.encode("2025-07-30T00:00:00", StandardCharsets.UTF_8);

        mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", "/test, /test1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statsService, never()).getHits(any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    public void getStats_EndBeforeStart() {
        String start = URLEncoder.encode("2020-07-30 00:00:00", StandardCharsets.UTF_8);
        String end = URLEncoder.encode("1990-07-30 00:00:00", StandardCharsets.UTF_8);

        mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", "/test, /test1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statsService, never()).getHits(any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    public void getStats_WrongUnique() {
        String start = URLEncoder.encode("2020-07-30 00:00:00", StandardCharsets.UTF_8);
        String end = URLEncoder.encode("1990-07-30 00:00:00", StandardCharsets.UTF_8);

        mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", "/test, /test1")
                        .param("unique", "wrongValue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statsService, never()).getHits(any(), any(), any(), any());
    }
}