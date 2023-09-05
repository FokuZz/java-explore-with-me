package ru.practicum.explore.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.dto.ViewStatsDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class StatsRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StatsRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<ViewStatsDto> getUniqueHitsByUri(LocalDateTime start, LocalDateTime end, List<String> uriList) {
        log.info("Getting unique endpoints hit from {} to {} for the uris: {}", start, end, uriList);
        String sql = "SELECT a.NAME as app, e.uri as uri, COUNT(DISTINCT e.ip) as hits FROM hits as e " +
                "LEFT JOIN apps as a ON e.app_id = a.ID WHERE e.sent BETWEEN :start AND :end AND e.uri " +
                "IN (:uriList) GROUP BY a.NAME, e.uri  ORDER BY hits DESC";
        SqlParameterSource parameterSource = new MapSqlParameterSource("uriList", uriList)
                .addValue("start", start)
                .addValue("end", end);
        List<ViewStatsDto> allHits = namedParameterJdbcTemplate.query(sql, parameterSource, (rs, rowNum) ->
                mapHit(rs));
        log.info("Query result: {}", allHits);
        return allHits;
    }

    public List<ViewStatsDto> getAllHitsByUri(LocalDateTime start, LocalDateTime end, List<String> uriList) {
        log.info("Getting all endpoints hit from {} to {} for the uris: {}", start, end, uriList);

        String sql = "SELECT a.NAME as app, e.uri as uri, COUNT(e.ip) as hits FROM hits as e " +
                "LEFT JOIN apps as a ON e.app_id = a.ID WHERE e.sent BETWEEN :start AND :end AND e.uri " +
                "IN (:uriList) GROUP BY a.NAME, e.uri ORDER BY hits DESC";

        SqlParameterSource parameterSource = new MapSqlParameterSource("uriList", uriList)
                .addValue("start", start)
                .addValue("end", end);
        List<ViewStatsDto> allHits = namedParameterJdbcTemplate.query(sql, parameterSource, (rs, rowNum) ->
                mapHit(rs));
        log.info("Query result: {}", allHits);
        return allHits;
    }

    public List<ViewStatsDto> getAllHits(LocalDateTime start, LocalDateTime end) {
        log.info("Getting all endpoints hit from {} to {}.", start, end);
        String sql = "SELECT a.NAME as app, e.uri as uri, COUNT(e.ip) as hits FROM hits as e " +
                "LEFT JOIN apps as a ON e.app_id = a.ID WHERE e.sent BETWEEN :start AND :end " +
                "GROUP BY a.NAME, e.uri ORDER BY hits DESC";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("start", start)
                .addValue("end", end);
        List<ViewStatsDto> allHits = namedParameterJdbcTemplate.query(sql, parameterSource, (rs, rowNum) ->
                mapHit(rs));
        log.info("Query result: {}", allHits);
        return allHits;
    }

    public List<ViewStatsDto> getUniqueHits(LocalDateTime start, LocalDateTime end) {
        log.info("Getting unique endpoints hit from {} to {}.", start, end);
        String sql = "SELECT a.NAME as app, eh.uri as uri, COUNT(DISTINCT eh.ip) as hits FROM hits as eh " +
                "LEFT JOIN apps as a ON eh.app_id = a.NAME WHERE eh.sent BETWEEN :start AND :end " +
                "GROUP BY a.NAME, eh.uri, eh.uri ORDER BY hits DESC";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("start", start)
                .addValue("end", end);
        List<ViewStatsDto> allHits = namedParameterJdbcTemplate.query(sql, parameterSource, (rs, rowNum) ->
                mapHit(rs));
        log.info("Query result: {}", allHits);
        return allHits;
    }

    private ViewStatsDto mapHit(ResultSet rs) throws SQLException {
        return ViewStatsDto.builder()
                .app(rs.getString("app"))
                .uri(rs.getString("uri"))
                .hits(rs.getInt("hits"))
                .build();
    }
}
