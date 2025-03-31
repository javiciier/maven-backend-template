package ${package}.common.interfaces;

/**
 * Interface to force a DTO class to get an instance of the entity it represents.
 *
 * @param <T> Type of the entity represented by the DTO that inherits this interface.
 */
public interface EntityConversor<T> {

  /**
   * Converts the DTO to its corresponding entity.
   *
   * @return the entity corresponding to the DTO.
   */
  T toEntity();
}
