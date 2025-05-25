package ${package}.auth.application.usecases;

import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.users.domain.entities.*;
import ${package}.users.domain.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class with common methods for testing the auth module
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class AuthBaseTest {

  @Value("${spring.application.name}")
  private String applicationName;

  // region TEST VARIABLES
  private Map<String, User> generatedUsers = new HashMap<>();
  // endregion TEST VARIABLES

  // region DEPENDENCIES
  protected final BCryptPasswordEncoder passwordEncoder;
  protected final UserRepository userRepository;

  // endregion DEPENDENCIES

  @Autowired
  public AuthBaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  // region UTILITY METHODS
  public synchronized User generateUser(String nickname) {
    if (generatedUsers.containsKey(nickname)) {
      return generatedUsers.get(nickname);
    }

    User user = User.builder()
        .name("name")
        .surname("surname")
        .gender(Gender.MALE)
        .birthDate(LocalDate.now().minusYears(18))
        .build();
    Credential userCredentials = generateCredentialForUser(user, nickname);
    user.assignCredential(userCredentials);
    ContactInfo contactInfo = generateContactInfoForUser(user);
    user.assignContactInfo(contactInfo);
    generatedUsers.put(nickname, user);

    return user;
  }

  public Credential generateCredentialForUser(User user, String nickname) {
    Credential credentials = Credential.builder()
        .user(user)
        .nickname(nickname)
        .build();
    String userPassword = getPlainPasswordFromUser(user);
    String passwordHash = passwordEncoder.encode(userPassword);
    credentials.setPasswordHash(passwordHash);

    return credentials;
  }

  public ContactInfo generateContactInfoForUser(User user) {
    String email = createEmailForUser(user);

    return ContactInfo.builder()
        .user(user)
        .email(email)
        .phoneNumber("123456789")
        .build();
  }

  public User registerUserInRepository(User user) {
    return userRepository.save(user);
  }

  public RegisterUserParamsDTO toRegisterUserParamsDTO(User user) {
    String plainPassword = getPlainPasswordFromUser(user);
    String email = createEmailForUser(user);
    String phoneNumber = getOrCreatePhoneNumberFromUser(user);

    return RegisterUserParamsDTO.builder()
        .name(user.getName())
        .surname(user.getSurname())
        .gender(user.getGender())
        .birthDate(user.getBirthDate())
        .nickname(user.getNickname())
        .plainPassword(plainPassword)
        .email(email)
        .phoneNumber(phoneNumber)
        .build();
  }

  // endregion UTILITY METHODS

  // region AUXILIAR FUNCTIONS
  private String createEmailForUser(User user) {
    String username;
    String surname = (user.getSurname() != null) ? user.getSurname() : Double.toString(Math.random());
    username = "%s_%s".formatted(user.getName(), surname);

    return username.toLowerCase()
            + "@"
            + applicationName
            + ".es";
  }

  private String getPlainPasswordFromUser(User user) {
    return "password";
  }

  private static String getOrCreatePhoneNumberFromUser(User user) {
    return (user.getContactInfo() == null) ? "123456789" : user.getContactInfo().getPhoneNumber();
  }
  // endregion AUXILIAR FUNCTIONS

  // region TEST LIFECYCLE
  @AfterAll
  public void tearDown() {
    // Drop all registered users
    userRepository.deleteAll();
  }
  // endregion TEST LIFECYCLE

}
