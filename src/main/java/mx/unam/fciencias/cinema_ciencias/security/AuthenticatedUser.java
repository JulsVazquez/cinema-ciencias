package mx.unam.fciencias.cinema_ciencias.security;

/**
 * Identidad del usuario tal como la afirma el Gateway.
 * El monolito ya no conoce usuarios ni contraseñas: solo recibe quién es el
 * usuario a través de los headers que agrega el Gateway tras validar el JWT.
 */
public record AuthenticatedUser(Long id, String username) {}
