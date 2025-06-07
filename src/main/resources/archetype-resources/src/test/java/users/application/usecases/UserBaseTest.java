package ${package}.users.application.usecases;

import ${package}.users.domain.entities.*;
import ${package}.users.domain.repositories.UserRepository;
import ${package}.utils.TestDataGenerator;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

/**
 * Base class with common methods for testing the user module
 */
@Log4j2
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class UserBaseTest {

    // region TEST VARIABLES

    // endregion TEST VARIABLES

    // region DEPENDENCIES
    @Autowired
    protected TestDataGenerator dataGenerator;
    protected final UserRepository userRepository;
    protected final Faker faker = new Faker(Locale.US);

    // endregion DEPENDENCIES

    public UserBaseTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // region UTILITY METHODS



    // endregion UTILITY METHODS

    // region AUXILIAR FUNCTIONS

    // endregion AUXILIAR FUNCTIONS

    // region TEST LIFECYCLE
    @AfterAll
    public void tearDown() {
        // Drop all registered users
        userRepository.deleteAll();
    }
    // endregion TEST LIFECYCLE

}
