package ${package}.users.application;

import ${package}.auth.application.utils.AuthUtils;
import ${package}.users.domain.entities.*;
import ${package}.users.domain.exceptions.*;
import ${package}.users.domain.repositories.*;
import ${package}.users.application.UserService;
import ${package}.users.infrastructure.dto.input.UpdateContactInfoParamsDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@Transactional
@Service
public class UserServiceImpl implements UserService {
    // region DEPENDENCIES
    private final ContactInfoRepository contactInfoRepo;
    private final AuthUtils authUtils;
    private final UserRepository userRepo;

    public UserServiceImpl(ContactInfoRepository contactInfoRepo,
                           AuthUtils authUtils,
                           UserRepository userRepo) {
        this.contactInfoRepo = contactInfoRepo;
        this.authUtils = authUtils;
        this.userRepo = userRepo;
    }

    // endregion DEPENDENCIES

    // region USE CASES
    @Override
    public User findUserByID(UUID userID) throws UserNotFoundException {
        return authUtils.fetchUserByID(userID);
    }

    @Override
    public User updateContactInfo(UUID userID, UpdateContactInfoParamsDTO paramsDTO) throws UserNotFoundException {
        // Check user exists and get its credentials
        User user = authUtils.fetchUserByID(userID);
        ContactInfo contactInfo = user.getContactInfo();

        boolean hasContactInfoChanged = shouldUpdateContactInfo(contactInfo, paramsDTO);
        if (!hasContactInfoChanged) {
            return user;
        }

        log.info("User {} has updated his contact info", userID);
        contactInfoRepo.save(contactInfo);

        return user;
    }

    // endregion USE CASES

    // region AUXILIAR METHODS


    private boolean shouldUpdateContactInfo(ContactInfo currentInfo, UpdateContactInfoParamsDTO newInfoDto) {
        boolean shouldUpdate = false;

        if (hasChanged(currentInfo.getEmail(), newInfoDto.getNewEmail())) {
            // TODO Validate and confirm email
            currentInfo.setEmail(newInfoDto.getNewEmail());
            shouldUpdate = true;
        }

        if (hasChanged(currentInfo.getPhoneNumber(), newInfoDto.getNewMobilePhone())) {
            // TODO Validate and confirm phone
            currentInfo.setPhoneNumber(newInfoDto.getNewMobilePhone());
            shouldUpdate = true;
        }

        return shouldUpdate;
    }

    private <T extends Object> boolean hasChanged(T expected, T received) {
        return !expected.equals(received);
    }

    // endregion AUXILIAR METHODS

}
