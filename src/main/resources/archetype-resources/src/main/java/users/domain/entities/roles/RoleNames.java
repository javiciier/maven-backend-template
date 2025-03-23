package ${package}.users.domain.entities.roles;

public enum RoleNames {
  ADMIN("admin"),
  BASIC("basic"),
  PREMIUM("premium");

  private final String name;

  RoleNames(String name) {
    this.name = name;
  }
}
