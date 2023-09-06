package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ViewStatsDto implements Comparable<ViewStatsDto> {
    private String app;
    private String uri;
    private long hits;

    @Override
    public int compareTo(ViewStatsDto o) {
        return (int) (o.getHits() - hits);
    }
}
