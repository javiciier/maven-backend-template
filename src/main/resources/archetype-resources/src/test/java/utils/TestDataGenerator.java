package ${package}.utils;

import ${package}.users.domain.entities.*;
import ${package}.users.domain.repositories.UserRepository;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Log4j2
@ActiveProfiles("test")
@Component
@Transactional
public class TestDataGenerator {
  // region CONSTANTS
  public static final String DEFAULT_PASSWORD = "password";
  public static final String EMAIL_FORMAT = "%s@%s.es";
  // endregion CONSTANTS

  @Value("${spring.application.name}")
  private String applicationName;

  // region DEPENDENCIES
  protected BCryptPasswordEncoder passwordEncoder;
  protected UserRepository userRepository;
  protected final Faker faker = new Faker(Locale.US);

  // endregion DEPENDENCIES

  @Autowired
  public TestDataGenerator(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  // region User data generator
  public User generateUser(String nickname) {
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

    return user;
  }

  public Credential generateCredentialForUser(User user, String nickname) {
    Credential credentials = Credential.builder()
        .user(user)
        .nickname(nickname)
        .build();
    String userPassword = getPlainPasswordFromUser();
    String passwordHash = passwordEncoder.encode(userPassword);
    credentials.setPasswordHash(passwordHash);

    return credentials;
  }

  public ContactInfo generateContactInfoForUser(User user) {
    String email = createEmailForUser(user);
    String phoneNumber = getOrCreatePhoneNumberFromUser(user);

    return ContactInfo.builder()
        .user(user)
        .email(email)
        .phoneNumber(phoneNumber)
        .build();
  }


  /**
   * Stores a user in the database
   */
  public User registerUserInRepository(User user) {
    User storedUser = user;
    // If user has no ID, then it's not registered in database
    if (user.getUserID() == null) {
      storedUser = userRepository.save(user);
    }

    return storedUser;
  }

  /**
   * Creates a new user and stores it in database
     /**
   * Creates a new user and stores it in database
   */
  public User createAndRegisterUser(String nickname) {
    User user = generateUser(nickname);
    user = registerUserInRepository(user);
    log.debug("Registered new user: {}", user);

    return user;
  }


  // endregion User data generator


  // region auxiliar methods

  public String createEmailForUser(User user) {
    String username = "%s_%s".formatted(user.getName(), user.getSurname());
    return EMAIL_FORMAT.formatted(username, applicationName);
  }

  public String getPlainPasswordFromUser() {
    return DEFAULT_PASSWORD;
  }

  public String getOrCreatePhoneNumberFromUser(User user) {
    return (user.getContactInfo() != null)
        ? user.getContactInfo().getPhoneNumber()
        : faker.phoneNumber().cellPhone();
  }

  // endregion auxiliar methods
}