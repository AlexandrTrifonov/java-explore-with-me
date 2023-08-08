package ru.practicum.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatsDto {
    String app;
    String uri;
    Long hits;

//    @Override
//    public String toString() {
//        return "ViewStats{" +
//                "app='" + app + '\'' +
//                ", uri='" + uri + '\'' +
//                ", hits=" + hits +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ViewStatsDto viewStats = (ViewStatsDto) o;
//        return Objects.equals(app, viewStats.app) && Objects.equals(uri, viewStats.uri) && Objects.equals(hits, viewStats.hits);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(app, uri, hits);
//    }
}
