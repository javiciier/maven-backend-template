package ${package}.users.application;

import ${package}.users.application.utils.AuthUtils;
import ${package}.users.domain.*;
import ${package}.users.domain.exceptions.*;
import ${package}.users.infrastructure.dto.input.RegisterUserParamsDTO;
import ${package}.users.infrastructure.repositories.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    // region DEPENDENCIES
    private final UserRepository userRepo;
    private final CredentialRepository credentialRepo;
    private final ContactInfoRepository contactInfoRepo;
    private final AuthUtils authUtils;

    public AuthServiceImpl(UserRepository userRepo,
                           CredentialRepository credentialRepo,
                           ContactInfoRepository contactInfoRepo,
                           AuthUtils authUtils) {
        this.userRepo = userRepo;
        this.credentialRepo = credentialRepo;
        this.contactInfoRepo = contactInfoRepo;
        this.authUtils = authUtils;
    }

    // endregion DEPENDENCIES

    // region USE CASES
    @Override
    public User register(RegisterUserParamsDTO paramsDTO) throws UserAlreadyExistsException {
        // Check if another user with same nickname already exists
        if (credentialRepo.existsByNicknameIgnoreCase(paramsDTO.getNickname())) {
            throw new UserAlreadyExistsException(paramsDTO.getNickname());
        }

        // Register user
        User user = fromRegisterParams(paramsDTO);
        user = userRepo.save(user);
        createCredentialForUser(paramsDTO, user);
        createContactInfoForUser(paramsDTO, user);

        // Set default user data
        user.setRegisteredAt(LocalDateTime.now());
        authUtils.assignRoleToUser(user, UserRoles.BASIC);
        user = userRepo.save(user);

        log.info("Registered new user {}", paramsDTO.getNickname());
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public User login(String nickname, String rawPassword) throws IncorrectLoginException {
        // Check if user exists
        User user;
        try {
            user = authUtils.fetchUserByNickname(nickname);
        } catch (UserNotFoundException e) {
            throw new IncorrectLoginException();
        }

        // Check if credentials match
        Credential userCredentials = user.getCredential();
        if (!authUtils.doPasswordsMatch(rawPassword, userCredentials.getPasswordEncrypted())) {
            throw new IncorrectLoginException();
        }
        log.info("User {} authenticated using credentials", user.getUserID());


        return user;
    }

    @Override
    public User loginViaJWT(UUID userID) throws UserNotFoundException {
        User user = authUtils.fetchUserByID(userID);
        log.info("User {} authenticated using Json Web Token", userID);

        return user;
    }

    @Override
    public void changePassword(UUID userID, String oldPassword, String newPassword)
            throws UserNotFoundException, PasswordsMismatchException {
        // Find user and credentials
        Credential credential = authUtils.findUserCredential(userID);

        // Check passwords match
        String currentPassword = credential.getPasswordEncrypted();
        if (!authUtils.doPasswordsMatch(oldPassword, currentPassword)) {
            throw new PasswordsMismatchException();
        }

        // Encrypt and update password
        String encodedNewPassword = authUtils.encryptPassword(newPassword);
        credential.setPasswordEncrypted(encodedNewPassword);

        log.info("User {} has changed his password", credential.getNickname());
        credentialRepo.save(credential);
    }

    // endregion USE CASES


    // region AUXILIAR METHODS
    private User createContactInfoForUser(RegisterUserParamsDTO paramsDTO, User user) {
        ContactInfo contactInfo = ContactInfo.builder()
                .email(paramsDTO.getEmail().toLowerCase())
                .phoneNumber(paramsDTO.getPhoneNumber())
                .user(user)
                .build();
        contactInfoRepo.save(contactInfo);
        user.attachContactInfo(contactInfo);

        return user;
    }

    private User createCredentialForUser(RegisterUserParamsDTO dto, User user) {
        String encryptedPassword = authUtils.encryptPassword(dto.getRawPassword());
        Credential credentials = Credential.builder()
                .nickname(dto.getNickname())
                .passwordEncrypted(encryptedPassword)
                .user(user)
                .build();
        credentialRepo.save(credentials);
        user.attachCredential(credentials);

        return user;
    }
    private User fromRegisterParams(RegisterUserParamsDTO dto) {
        return User.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .build();
    }

    // endregion AUXILIAR METHODS

}
