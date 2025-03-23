package ${package}.auth.application.usecases.register;

import ${package}.auth.application.utils.AuthUtils;
import ${package}.users.domain.entities.*;
import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserAlreadyExistsException;
import ${package}.users.domain.repositories.*;
import ${package}.users.domain.repositories.roles.RoleRepository;
import ${package}.users.infrastructure.dto.conversors.UserConversor;
import ${package}.users.infrastructure.dto.input.RegisterUserParamsDTO;
import jakarta.annotation.PostConstruct;
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

  // region DEPENDENCIES
  private final UserRepository userRepository;
  private final CredentialRepository credentialRepository;
  private final RoleRepository roleRepository;
  private final ContactInfoRepository contactInfoRepository;
  private final AuthUtils authUtils;
  private Role DEFAULT_NEW_USER_ROLE;

  // endregion DEPENDENCIES

  @PostConstruct
  private final void setConstantValues() {
    this.DEFAULT_NEW_USER_ROLE = roleRepository.findByName(RoleNames.BASIC).get();
  }

  // region USE CASES

  /**
   * Register a new user.
   *
   * @param paramsDTO New user information
   * @return A new registered user
   * @throws UserAlreadyExistsException User already exists
   */
  public User register(RegisterUserParamsDTO paramsDTO) throws UserAlreadyExistsException {
    String nickname = paramsDTO.getNickname();
    log.info("Trying to register a new user with nickname '{}'", nickname);

    // Check if another user with the same nickname exists
    if (doesUserWithNicknameExist(nickname)) {
      log.error("Register new user failed because an user with nickname '{}' already existed");
      throw new UserAlreadyExistsException(nickname);
    }

    // Create user data
    log.info("Registering new user with nickname '{}'", nickname);
    User user = UserConversor.fromRegisterUserParamsDTO(paramsDTO);
    createCredentialForUser(paramsDTO, user);
    createContactInfoForUser(paramsDTO, user);

    // Assign default data for a new user
    user.assignRole(DEFAULT_NEW_USER_ROLE);
    user.setRegisteredAt(LocalDateTime.now());
    user = userRepository.save(user);

    log.info("User with nickname '{}' registered successfuly. User ID assigned is '{}'", nickname,
        user.getUserID());
    return user;
  }

  // endregion USE CASES

  // region AUXILIAR METHODS
  private boolean doesUserWithNicknameExist(String nickname) {
    return credentialRepository.existsByNicknameIgnoreCase(nickname);
  }

  private User createCredentialForUser(RegisterUserParamsDTO dto, User user) {
    String encryptedPassword = authUtils.encryptPassword(dto.getRawPassword());
    Credential credential = Credential.builder()
        .nickname(dto.getNickname())
        .passwordHash(encryptedPassword)
        .user(user)
        .build();

    credentialRepository.save(credential);
    user.attachCredential(credential);

    return user;
  }

  private User createContactInfoForUser(RegisterUserParamsDTO dto, User user) {
    ContactInfo contactInfo = ContactInfo.builder()
        .email(dto.getEmail().toLowerCase())
        .phoneNumber(dto.getPhoneNumber())
        .user(user)
        .build();

    contactInfoRepository.save(contactInfo);
    user.attachContactInfo(contactInfo);

    return user;
  }

  // endregion AUXILIAR METHODS

}
