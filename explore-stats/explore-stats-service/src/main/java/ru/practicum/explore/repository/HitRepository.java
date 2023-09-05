package ru.practicum.explore.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.App;
import ru.practicum.explore.model.EndpointHit;

@Repository
@Slf4j
@RequiredArgsConstructor
public class HitRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EndpointHit addHit(EndpointHit endpointHit) {
        log.debug("Adding endpoint hit: {}.", endpointHit);
        getAppId(endpointHit.getApp());
        String sql = "INSERT INTO hits (app_id, uri, ip, sent) VALUES (:appId, :uri, :ip, :timestamp)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("appId", endpointHit.getApp().getAppId())
                .addValue("uri", endpointHit.getUri())
                .addValue("ip", endpointHit.getIp())
                .addValue("timestamp", endpointHit.getTimestamp());

        namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder, new String[]{"id"});
        endpointHit.setId(keyHolder.getKeyAs(Long.class));
        log.info("Endpoint hit added: {}.", endpointHit);
        return endpointHit;

    }

    private App getAppId(App app) {
        String sqlForAppId = "SELECT a.ID FROM APPS AS a WHERE NAME = :appName";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(app);
        try {
            Long appId = namedParameterJdbcTemplate.queryForObject(sqlForAppId, parameterSource, Long.class);
            app.setAppId(appId);
            log.info("App found: {}", app);
        } catch (EmptyResultDataAccessException e) {
            log.info("App name {} does not exist in DB. Adding app.", app);
            String sqlAddApp = "INSERT INTO apps (NAME) VALUES (:appName)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(sqlAddApp, parameterSource, keyHolder, new String[]{"id"});
            app.setAppId(keyHolder.getKeyAs(Long.class));
            log.info("App added: {}", app);
        }
        return app;
    }
}