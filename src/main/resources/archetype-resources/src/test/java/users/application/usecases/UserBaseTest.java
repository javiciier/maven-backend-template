package ${package}.users.application.usecases;

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
    public UserBaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
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
        String phoneNumber = getOrCreatePhoneNumberFromUser(user);

        return ContactInfo.builder()
                .user(user)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

    public User registerUserInRepository(User user) {
        User storedUser = user;
        // If user has no ID, then it's not registered in database
        if (user.getUserID() == null) {
            storedUser = userRepository.save(user);
        }

        if (!generatedUsers.containsKey(user.getNickname())) {
            generatedUsers.put(storedUser.getNickname(), storedUser);
        } else {
            generatedUsers.replace(user.getNickname(), user);
        }

        return storedUser;
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

    // endregion UTILITY METHODS

    // region AUXILIAR FUNCTIONS
    private String createEmailForUser(User user) {
        String username = "%s_%s".formatted(user.getName(), user.getSurname());

        return username.toLowerCase()
                + "@"
                + applicationName
                + ".es";
    }

    private String getPlainPasswordFromUser(User user) {
        return "password";
    }

    private String getOrCreatePhoneNumberFromUser(User user) {
        Faker faker = new Faker();

        return (user.getContactInfo() != null)
                ? user.getContactInfo().getPhoneNumber()
                : faker.phoneNumber().cellPhone();
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
