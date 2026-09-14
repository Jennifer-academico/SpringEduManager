package cl.miclase.springedumanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;

/**
 * Repositorio JPA para {@link Persona}.
 *
 * <p>Además de las operaciones CRUD estándar de {@link JpaRepository},
 * permite buscar por email o RUT (ambos únicos) y filtrar por rol.</p>
 */
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    /**
     * Busca una persona por su email, sin distinguir mayúsculas/minúsculas.
     *
     * @param email email a buscar
     * @return la persona encontrada, o vacío si no existe
     */
    Optional<Persona> findByEmailIgnoreCase(String email);

    /**
     * Busca una persona por su RUT exacto.
     *
     * @param rut RUT en formato {@code 12345678-9}
     * @return la persona encontrada, o vacío si no existe
     */
    Optional<Persona> findByRut(String rut);

    /**
     * Lista todas las personas que tienen un rol específico.
     *
     * @param rol rol a filtrar (ADMIN, DOCENTE o ESTUDIANTE)
     * @return lista de personas con ese rol
     */
    List<Persona> findByRol(Rol rol);
}