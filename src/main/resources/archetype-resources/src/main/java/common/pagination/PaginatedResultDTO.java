package ${package}.common.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.experimental.Accessors;

import java.util.List;

public record PaginatedResultDTO<T>(
    @NotNull
    List<T> items,

    @Accessors(fluent = true)
    @JsonProperty("hasMoreItems")
    @NotNull
    boolean hasMoreItems,

    @NotNull
    @PositiveOrZero
    int itemsCount,

    String nextPageToken
) {
  public PaginatedResultDTO(List<T> items, String nextPageToken) {
    this(items, nextPageToken != null, items.size(), nextPageToken);
  }
}
