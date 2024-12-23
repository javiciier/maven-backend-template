package ${package}.users.application;

import ${package}.users.domain.User;
import ${package}.users.domain.exceptions.*;
import ${package}.users.infrastructure.dto.input.RegisterUserParamsDTO;

import java.util.UUID;

public interface AuthService {
    /**
     * Registra un nuevo usuario.
     *
     * @param paramsDTO Datos del usuario a registrar
     * @return Usuario registrado
     * @throws UserAlreadyExistsException Usuario ya existe
     */
    User register(RegisterUserParamsDTO paramsDTO) throws UserAlreadyExistsException;

    /**
     * Iniciar sesión en el sistema.
     * @param nickname Nombre de usuario
     * @param rawPassword Contraseña escrita por el usuario
     * @return Datos del usuario autenticado
     * @throws IncorrectLoginException Nickname o contraseña incorrectos
     */
    User login(String nickname, String rawPassword) throws IncorrectLoginException;

    /**
     * Iniciar sesión en el sistema mediante Json Web Token (JWT).
     *
     * @param userID ID del usuario
     * @return Datos del usuario autenticado
     * @throws UserNotFoundException No se encuentra al usuario
     */
    User loginViaJWT(UUID userID) throws UserNotFoundException;

    /**
     * Cambiar contraseña del usuario.
     * @param userID ID del usuario
     * @param oldPassword Antigua contraseña
     * @param newPassword Nueva contraseña
     * @throws UserNotFoundException No se encuentra al usuario
     * @throws PasswordsDoNotMatchException Contraseñas no coinciden
     */
    void changePassword(UUID userID, String oldPassword, String newPassword)
            throws UserNotFoundException, PasswordsMismatchException;

}
