package cl.miclase.springedumanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.repository.PersonaRepository;
import cl.miclase.springedumanager.service.PersonaService;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private PersonaRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private PersonaService service;

    @BeforeEach
    void setUp() {
        service = new PersonaService(repository, passwordEncoder);
    }

    @Test
    void precargar_creaPersonaSinPassword_cuandoNoExisteRutNiEmail() {
        when(repository.findByRut("11111111-1")).thenReturn(Optional.empty());
        when(repository.findByEmailIgnoreCase("test@clase.cl")).thenReturn(Optional.empty());
        when(repository.save(any(Persona.class))).thenAnswer(inv -> inv.getArgument(0));

        Persona resultado = service.precargar("Juan Pérez", "test@clase.cl", "11111111-1", Rol.ESTUDIANTE);

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertFalse(resultado.tieneCuentaActiva());
        verify(repository).save(any(Persona.class));
    }

    @Test
    void precargar_lanzaExcepcion_cuandoRutYaExiste() {
        Persona existente = new Persona("Otro", "otro@clase.cl", "11111111-1", Rol.ESTUDIANTE);
        when(repository.findByRut("11111111-1")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () ->
            service.precargar("Juan Pérez", "test@clase.cl", "11111111-1", Rol.ESTUDIANTE)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void activarCuenta_definePassword_cuandoPersonaExisteYNoTieneCuentaActiva() {
        Persona persona = new Persona("Juan Pérez", "test@clase.cl", "11111111-1", Rol.ESTUDIANTE);
        when(repository.findByRut("11111111-1")).thenReturn(Optional.of(persona));
        when(passwordEncoder.encode("123456")).thenReturn("hash-encriptado");

        Persona resultado = service.activarCuenta("11111111-1", "123456");

        assertTrue(resultado.tieneCuentaActiva());
        assertEquals("hash-encriptado", resultado.getPasswordHash());
    }

    @Test
    void activarCuenta_lanzaExcepcion_cuandoRutNoExiste() {
        when(repository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            service.activarCuenta("99999999-9", "123456")
        );
    }

    @Test
    void activarCuenta_lanzaExcepcion_cuandoYaTieneCuentaActiva() {
        Persona persona = new Persona("Juan Pérez", "test@clase.cl", "11111111-1", Rol.ESTUDIANTE);
        persona.activarCuenta("hash-viejo");
        when(repository.findByRut("11111111-1")).thenReturn(Optional.of(persona));

        assertThrows(IllegalArgumentException.class, () ->
            service.activarCuenta("11111111-1", "123456")
        );
    }

    @Test
    void obtener_lanzaExcepcion_cuandoIdNoExiste() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.obtener(999L));
    }
}