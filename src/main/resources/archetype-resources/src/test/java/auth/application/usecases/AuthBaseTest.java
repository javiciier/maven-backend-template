package ${package}.auth.application.usecases;

import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.users.domain.entities.*;
import ${package}.users.domain.repositories.UserRepository;
import ${package}.utils.TestDataGenerator;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

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

  // region TEST VARIABLES

  // endregion TEST VARIABLES

  // region DEPENDENCIES
  @Autowired
  protected TestDataGenerator dataGenerator;
  protected final UserRepository userRepository;
  protected final Faker faker = new Faker(Locale.US);

  // endregion DEPENDENCIES

  public AuthBaseTest(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // region UTILITY METHODS

  /**
   * Generates the {@link RegisterUserParamsDTO} for the given user
   */
  public RegisterUserParamsDTO toRegisterUserParamsDTO(User user) {
    String plainPassword = dataGenerator.getPlainPasswordFromUser();
    String email = dataGenerator.createEmailForUser(user);
    String phoneNumber = dataGenerator.getOrCreatePhoneNumberFromUser(user);

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

  // endregion AUXILIAR FUNCTIONS

  // region TEST LIFECYCLE
  @AfterAll
  void tearDown() {
    // Drop all registered users
    userRepository.deleteAll();
  }
  // endregion TEST LIFECYCLE

}
