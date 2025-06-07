package ${package}.users.application.usecases.update;

import ${package}.users.application.usecases.UserBaseTest;
import ${package}.users.application.usecases.update.UpdateContactInfoUseCase;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.ContactInfo;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.UserRepository;
import ${package}.users.infrastructure.dto.inbound.UpdateContactInfoParamsDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("users.update.UpdateContactInfoUseCase")
class UpdateContactInfoUseCaseTest extends UserBaseTest {

    // region DEPENDENCIES
    private final UpdateContactInfoUseCase useCase;
    // endregion DEPENDENCIES

    @Autowired
    public UpdateContactInfoUseCaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UpdateContactInfoUseCase useCase) {
        super(userRepository);
        this.useCase = useCase;
    }

    private User registeredUser;

    // region TEST LIFECYCLE
    @BeforeEach
    void registerUser()  {
        String nickname = faker.name().firstName();
        log.debug("Registering user with nickname '{}'", nickname);
        this.registeredUser = dataGenerator.createAndRegisterUser(nickname);
    }

    @AfterEach
    void removeUsers() {
        userRepository.deleteAll();
    }

    // endregion TEST LIFECYCLE

    // region TEST CASES

    @Test
    void updateContactInfo() throws UserNotFoundException {
        // Given
        UUID userID = this.registeredUser.getUserID();
        final ContactInfo contactInfo = this.registeredUser.getContactInfo();
        final String oldEmail = contactInfo.getEmail();
        final String oldPhone = contactInfo.getPhoneNumber();
        String newEmail = faker.internet().emailAddress();
        String newPhone = faker.phoneNumber().cellPhone();

        // Then
        UpdateContactInfoParamsDTO params = UpdateContactInfoParamsDTO.builder()
                .newEmail(newEmail)
                .newMobilePhone(newPhone)
                .build();
        final User updatedUser = useCase.updateContactInfo(userID, params);
        ContactInfo updatedUserContactInfo = updatedUser.getContactInfo();

        // Check
        assertAll(
                // Contact info exists for user
                () -> assertNotNull(updatedUser.getContactInfo()),
                // Contact info has changed
                () -> assertNotEquals(oldEmail, updatedUserContactInfo.getEmail()),
                () -> assertNotEquals(oldPhone, updatedUserContactInfo.getPhoneNumber())
        );
    }

    @Test
    void updateContactInfoWithSameDataRemainsEqual() throws UserNotFoundException {
        // Given
        UUID userID = this.registeredUser.getUserID();
        final ContactInfo contactInfo = this.registeredUser.getContactInfo();
        final String oldEmail = contactInfo.getEmail();
        final String oldPhone = contactInfo.getPhoneNumber();

        // Then
        UpdateContactInfoParamsDTO params = UpdateContactInfoParamsDTO.builder()
                .newEmail(oldEmail)
                .newMobilePhone(oldPhone)
                .build();
        final User updatedUser = useCase.updateContactInfo(userID, params);
        ContactInfo updatedUserContactInfo = updatedUser.getContactInfo();

        // Check
        assertAll(
                // Contact info exists for user
                () -> assertNotNull(updatedUser.getContactInfo()),
                // Contact info has changed
                () -> assertEquals(oldEmail, updatedUserContactInfo.getEmail()),
                () -> assertEquals(oldPhone, updatedUserContactInfo.getPhoneNumber())
        );
    }

    @Test()
    void updateContactInfoForNonExistentUser() throws UserNotFoundException {
        // Given
        userRepository.delete(this.registeredUser);
        final UUID userID = UUID.randomUUID();
        final String expectedMessage = "User with ID '%s' not found".formatted(userID);
        String newEmail = faker.internet().emailAddress();
        String newPhone = faker.phoneNumber().cellPhone();

        // Then
        final UpdateContactInfoParamsDTO params = UpdateContactInfoParamsDTO.builder()
                .newEmail(newEmail)
                .newMobilePhone(newPhone)
                .build();
        final Exception thrownException = assertThrows(
                UserNotFoundException.class,
                () -> useCase.updateContactInfo(userID, params)
        );

        // Check
        final String actualMessage = thrownException.getMessage();
        assertAll(
                // Is expected thrownException
                () -> assertTrue(thrownException instanceof UserNotFoundException),
                // Contains expected message
                () -> assertEquals(expectedMessage, actualMessage)
        );
    }

    // endregion TEST CASES

}