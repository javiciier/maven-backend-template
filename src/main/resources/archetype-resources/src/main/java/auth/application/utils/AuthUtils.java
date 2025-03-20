package ${package}.auth.application.utils;

import ${package}.common.security.PasswordEncoderBean;
import ${package}.common.security.jwt.domain.*;
import ${package}.common.security.jwt.application.*;
import ${package}.users.domain.entities.*;
import ${package}.users.domain.entities.roles.*;
import ${package}.users.domain.exceptions.*;
import ${package}.users.domain.repositories.*;
import ${package}.users.domain.repositories.roles.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class AuthUtils {
    private final BCryptPasswordEncoder passwordEncoder = PasswordEncoderBean.passwordEncoder();
    private final JwtGenerator jwtGenerator;
    private final RoleRepository roleRepo;
    private final RoleAssignmentRepository userRoleRepo;
    private final CredentialRepository credentialRepo;
    private final UserRepository userRepo;


    public AuthUtils(RoleRepository roleRepo,
                     RoleAssignmentRepository userRoleRepo,
                     JwtGenerator jwtGenerator,
                     CredentialRepository credentialRepo,
                     UserRepository userRepo) {
        this.roleRepo = roleRepo;
        this.userRoleRepo = userRoleRepo;
        this.jwtGenerator = jwtGenerator;
        this.credentialRepo = credentialRepo;
        this.userRepo = userRepo;
    }

    public String generateJWTFromUser(User user) {
        String nickname = user.getCredential().getNickname();
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .toList();

        JwtData jwtData = JwtData.builder()
                .userID(user.getUserID())
                .nickname(nickname)
                .roles(roles)
                .build();

        return jwtGenerator.generateJWT(jwtData);
    }

    public String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean doPasswordsMatch(String rawPassword, String expectedPassword) {
        return passwordEncoder.matches(rawPassword, expectedPassword);
    }

    public User fetchUserByNickname(String nickname) throws UserNotFoundException {
        // Check credentials exists for the given nickname
        User user = credentialRepo.findByNicknameIgnoreCase(nickname)
                .orElseThrow(() -> new UserNotFoundException(nickname))
                .getUser();

        return user;
    }

    public User findUserByID(UUID userID) throws UserNotFoundException {
        // Check user exists
        return userRepo.findById(userID)
                .orElseThrow(() -> new UserNotFoundException(userID));
    }

    public User fetchUserByID(UUID userID) throws UserNotFoundException {
        // Check user exists
        User user = findUserByID(userID);

        return user;
    }

    public Credential findUserCredential(UUID userID) throws UserNotFoundException {
        return credentialRepo.findCredentialByUserID(userID)
                .orElseThrow(() -> new UserNotFoundException(userID));
    }

    public boolean doUsersMatch(UUID requestorUserID, UUID targetUserID) {
        return requestorUserID.equals(targetUserID);
    }
}
