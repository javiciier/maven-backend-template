package ${package}.auth.application.usecases;

import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.users.domain.entities.*;
import ${package}.users.domain.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Base class with common methods for testing the auth module
 */
@Log4j2
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
  protected final Faker faker;

  // endregion DEPENDENCIES

  @Autowired
  public AuthBaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.faker = new Faker(Locale.US);
  }

  // region UTILITY METHODS
  public synchronized User generateUser(String nickname) {
    if (generatedUsers.containsKey(nickname)) {
      return generatedUsers.get(nickname);
    }

    // Generate data for test
    String name = faker.name().firstName();
    String surname = faker.name().lastName();
    Gender gender = faker.bool().bool() ? Gender.MALE : Gender.FEMALE;
    LocalDate birthDate = faker.date().birthday(18, 65)
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    User user = User.builder()
            .name(name)
            .surname(surname)
            .gender(gender)
            .birthDate(birthDate)
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

  protected String getPlainPasswordFromUser(User user) {
    return "password";
  }

  private static String getOrCreatePhoneNumberFromUser(User user) {
    return (user.getContactInfo() == null) ? "123456789" : user.getContactInfo().getPhoneNumber();
  }

  /**
   * Creates a new user and stores it in database
   */
  public User createAndRegisterUser(String nickname) {
    User user = generateUser(nickname);
    user = registerUserInRepository(user);
    log.debug("Registered new user: {}", user);

    return user;
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
