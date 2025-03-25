package ${package}.users.domain.entities.roles;

import lombok.Getter;

@Getter
public enum RoleNames {
  ADMIN("ADMIN"),
  BASIC("BASIC"),
  PREMIUM("PREMIUM");

  private final String name;

  RoleNames(String name) {
    this.name = name.toUpperCase();
  }
}
