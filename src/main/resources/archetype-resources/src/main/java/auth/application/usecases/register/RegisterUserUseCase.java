package ${package}.auth.application.usecases.register;

import ${package}.auth.application.utils.AuthUtils;
import ${package}.auth.infrastructure.dto.conversors.AuthConversor;
import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.users.domain.entities.ContactInfo;
import ${package}.users.domain.entities.Credential;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserAlreadyExistsException;
import ${package}.users.domain.repositories.CredentialRepository;
import ${package}.users.domain.repositories.UserRepository;
import ${package}.users.domain.repositories.roles.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
@Lazy
public class RegisterUserUseCase {

  private final RoleNames DEFAULT_NEW_USER_ROLE = RoleNames.BASIC;

  // region DEPENDENCIES
  private final UserRepository userRepository;
  private final CredentialRepository credentialRepository;
  private final RoleRepository roleRepository;
  private final AuthUtils authUtils;

  // endregion DEPENDENCIES

  // region USE CASES

  /**
   * Register a new user.
   *
   * @param paramsDTO New user information
   * @return A new registered user
   * @throws UserAlreadyExistsException User already exists
   */
  public User register(RegisterUserParamsDTO paramsDTO) throws UserAlreadyExistsException {
    String nickname = paramsDTO.nickname();
    log.debug("Starting registration of a new user with nickname '{}'", nickname);

    // Check if another user with the same nickname exists
    if (doesUserWithNicknameExist(nickname)) {
      log.debug("Register new user failed because an user with nickname '{}' already existed");
      throw new UserAlreadyExistsException(nickname);
    }

    // Create user data
    log.debug("Registering new user with nickname '{}'", nickname);
    User user = AuthConversor.fromRegisterUserParamsDTO(paramsDTO);
    createCredentialForUser(paramsDTO, user);
    createContactInfoForUser(paramsDTO, user);

    // Assign default data for a new user
    user.assignRole(this.getDefaultUserRole());
    user.setRegisteredAt(LocalDateTime.now());
    user = userRepository.save(user);

    log.debug("User with nickname '{}' registered successfuly. User ID assigned is '{}'",
        nickname, user.getUserID()
    );
    return user;
  }

  // endregion USE CASES

  // region AUXILIAR METHODS
  private boolean doesUserWithNicknameExist(String nickname) {
    return credentialRepository.existsByNicknameIgnoreCase(nickname);
  }

  private User createCredentialForUser(RegisterUserParamsDTO dto, User user) {
    String encryptedPassword = authUtils.encryptPassword(dto.plainPassword());
    Credential credential = Credential.builder()
        .nickname(dto.nickname())
        .passwordHash(encryptedPassword)
        .user(user)
        .build();

    user.assignCredential(credential);

    return user;
  }

  private User createContactInfoForUser(RegisterUserParamsDTO dto, User user) {
    ContactInfo contactInfo = ContactInfo.builder()
        .email(dto.email().toLowerCase())
        .phoneNumber(dto.phoneNumber())
        .user(user)
        .build();

    user.assignContactInfo(contactInfo);

    return user;
  }

  private final Role getDefaultUserRole() {
    return roleRepository.findByName(DEFAULT_NEW_USER_ROLE).get();
  }

  // endregion AUXILIAR METHODS

}
