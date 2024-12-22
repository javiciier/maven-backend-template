package $package.users.application;

import $package.users.domain.User;
import $package.users.domain.exceptions.UserIsDeactivatedException;
import $package.users.domain.exceptions.UserNotFoundException;
import $package.users.infrastructure.dto.input.UpdateContactInfoParamsDTO;

import java.util.UUID;

public interface UserService {
    /**
     * Buscar un usuario por su ID.
     *
     * @param userID ID del usuario
     * @return Datos del usuario
     * @throws UserNotFoundException No se encuentra al usuario
     * @throws UserIsDeactivatedException Usuario está desactivado
     */
    User findUserByID(UUID userID) throws UserNotFoundException, UserIsDeactivatedException;

    /**
     * Actualiza la información de contacto del usuario
     *
     * @param userID       ID del usuario
     * @param updateParams Datos de contacto a actualizar
     * @return Usuario con los datos actualizados
     * @throws UserNotFoundException No se encuentra al usuario
     * @throws UserIsDeactivatedException Usuario está desactivado
     */
    User updateContactInfo(UUID userID, UpdateContactInfoParamsDTO updateParams) throws UserNotFoundException, UserIsDeactivatedException;

}
