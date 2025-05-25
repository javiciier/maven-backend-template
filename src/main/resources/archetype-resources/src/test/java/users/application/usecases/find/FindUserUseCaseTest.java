package ${package}.users.application.usecases.find;

import ${package}.users.application.usecases.UserBaseTest;
import ${package}.users.application.usecases.find.FindUserUseCase;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.UserRepository;
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
@DisplayName("users.find.FindUserUseCase")
class FindUserUseCaseTest extends UserBaseTest {

    // region DEPENDENCIES
    private final FindUserUseCase useCase;
    // endregion DEPENDENCIES

    @Autowired
    public FindUserUseCaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, FindUserUseCase useCase) {
        super(passwordEncoder, userRepository);
        this.useCase = useCase;
    }

    private User registeredUser;

    // region TEST LIFECYCLE
    @BeforeEach
    void registerUser(TestInfo testInfo) {
        // These tests do not need to register users before their execution
        List<String> testsToSkip = List.of(
                "findNonExistentUserByID"
        );
        if (testsToSkip.stream().anyMatch(testInfo.getDisplayName()::contains)) {
            return;
        }

        String nickname = faker.name().firstName();
        log.debug("Registering user with nickname '{}'", nickname);
        this.registeredUser = createAndRegisterUser(nickname);
    }

    @AfterEach
    void removeUsers() {
        userRepository.deleteAll();
    }

    // endregion TEST LIFECYCLE

    // region TEST CASES

    @Test
    void findUserById() throws UserNotFoundException {
        // Given
        UUID userID = this.registeredUser.getUserID();

        // Then
        User foundUser = useCase.findByUserId(userID);

        // Check
        assert(foundUser.getUserID() != null);
        assertAll(
                // Contains same data
                () -> assertEquals(this.registeredUser, foundUser)
        );
    }

    @Test()
    void findNonExistentUserByID() throws UserNotFoundException {
        // Given
        final UUID userID = UUID.randomUUID();
        final String expectedMessage = "User with ID '%s' not found".formatted(userID);

        // Then
        final Exception thrownException = assertThrows(
                UserNotFoundException.class,
                () -> useCase.findByUserId(userID)
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