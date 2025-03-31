package ${package}.common.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class to store a list of elements
 *
 * @param <T> Type of the elements being stored
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResult<T> {

  /**
   * Stored elements
   */
  private List<T> items;

  /**
   * Indicates if there are more items to be handled
   */
  @Accessors(fluent = true)
  @JsonProperty("hasMoreItems")
  private boolean hasMoreItems;

  private String nextPageToken;

  /**
   * Ammount of elements contained
   */
  private int itemsCount;

  public PaginatedResult(List<T> items, String nextPageToken) {
    this.items = items;
    this.itemsCount = items.size();
    this.hasMoreItems = nextPageToken != null;
    this.nextPageToken = nextPageToken;
  }

  public static <T> PaginatedResult<T> empty() {
    return new PaginatedResult<>(new ArrayList<>(0), null);
  }
}
