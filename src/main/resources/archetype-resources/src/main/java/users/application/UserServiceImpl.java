package $package.users.application;

import $package.users.application.utils.AuthUtils;
import $package.users.domain.ContactInfo;
import $package.users.domain.User;
import $package.users.domain.exceptions.UserIsDeactivatedException;
import $package.users.domain.exceptions.UserNotFoundException;
import $package.users.infrastructure.dto.input.UpdateContactInfoParamsDTO;
import $package.users.infrastructure.repositories.ContactInfoRepository;
import $package.users.infrastructure.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@Transactional
@Service
public class UserServiceImpl implements UserService {
    /* DEPENDENCIES */
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

    /* USE CASES */
    @Override
    public User findUserByID(UUID userID) throws UserNotFoundException, UserIsDeactivatedException {
        return authUtils.fetchUserByID(userID);
    }

    @Override
    public User updateContactInfo(UUID userID, UpdateContactInfoParamsDTO paramsDTO) throws UserNotFoundException, UserIsDeactivatedException {
        // Obtener al usuario y sus datos de contacto
        User user = authUtils.fetchUserByID(userID);
        ContactInfo contactInfo = user.getContactInfo();

        boolean hasChangedContact = false;
        // Comprobar si los campos han cambiado para actualizarlos
        if (hasChanged(contactInfo.getEmail(), paramsDTO.getNewEmail())) {
            // TODO Validar y confirmar email
            contactInfo.setEmail(paramsDTO.getNewEmail());
            hasChangedContact = true;
        }
        if (hasChanged(contactInfo.getPhoneNumber(), paramsDTO.getNewMobilePhone())) {
            // TODO validar y confirmar teléfono
            contactInfo.setPhoneNumber(paramsDTO.getNewMobilePhone());
            hasChangedContact = true;
        }

        if (hasChangedContact) {
            log.info("User {} has updated his contact info", userID);
            contactInfoRepo.save(contactInfo);
        }

        return user;
    }

    /* HELPER FUNCTIONS */
    private <T extends Object> boolean hasChanged(T expected, T received) {
        return !expected.equals(received);
    }
}
