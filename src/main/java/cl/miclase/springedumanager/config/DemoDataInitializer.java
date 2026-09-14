package cl.miclase.springedumanager.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import cl.miclase.springedumanager.domain.*;
import cl.miclase.springedumanager.repository.*;

/**
 * Carga un conjunto de datos de demostración al arrancar la aplicación,
 * únicamente si la tabla {@code persona} está vacía (para no duplicar
 * datos en arranques posteriores).
 *
 * <p>El escenario generado incluye:</p>
 * <ul>
 *   <li>Un administrador con cuenta ya activa.</li>
 *   <li>Dos docentes: uno con cuenta activa y otro sin activar, para
 *       poder probar ambos flujos de login y activación.</li>
 *   <li>Cinco estudiantes: dos con cuenta activa y tres sin activar.</li>
 *   <li>Cuatro cursos, dos a cargo de cada docente, con los cinco
 *       estudiantes inscritos en todos ellos.</li>
 *   <li>Cinco prácticas y tres evaluaciones repartidas entre los cursos,
 *       con algunas notas ya registradas.</li>
 * </ul>
 */
@Component
public class DemoDataInitializer implements CommandLineRunner {

    private final PersonaRepository personas;
    private final CursoRepository cursos;
    private final PracticaRepository practicas;
    private final EvaluacionRepository evaluaciones;
    private final NotaRepository notas;
    private final PasswordEncoder encoder;

    public DemoDataInitializer(
            PersonaRepository personas,
            CursoRepository cursos,
            PracticaRepository practicas,
            EvaluacionRepository evaluaciones,
            NotaRepository notas,
            PasswordEncoder encoder) {
        this.personas = personas;
        this.cursos = cursos;
        this.practicas = practicas;
        this.evaluaciones = evaluaciones;
        this.notas = notas;
        this.encoder = encoder;
    }

    /**
     * Ejecuta la carga de datos de demostración al iniciar la aplicación.
     * No hace nada si ya existen personas registradas.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    @Override
    public void run(String... args) {
        if (personas.count() > 0) {
            return;
        }

        crearActiva("Administrador", "admin@clase.cl", "00000000-0", Rol.ADMIN, "admin123");

        Persona docente1 = crearActiva("Profesor Ana Torres", "ana.torres@clase.cl", "11111111-1", Rol.DOCENTE, "docente123");
        Persona docente2 = crearSinActivar("Profesor Luis Vera", "luis.vera@clase.cl", "22222222-2", Rol.DOCENTE);

        Persona est1 = crearActiva("Camila Rojas", "camila.rojas@clase.cl", "33333333-3", Rol.ESTUDIANTE, "estudiante123");
        Persona est2 = crearActiva("Diego Soto", "diego.soto@clase.cl", "44444444-4", Rol.ESTUDIANTE, "estudiante123");
        Persona est3 = crearSinActivar("Valentina Reyes", "valentina.reyes@clase.cl", "55555555-5", Rol.ESTUDIANTE);
        Persona est4 = crearSinActivar("Matías Fuentes", "matias.fuentes@clase.cl", "66666666-6", Rol.ESTUDIANTE);
        Persona est5 = crearSinActivar("Fernanda Muñoz", "fernanda.munoz@clase.cl", "77777777-7", Rol.ESTUDIANTE);

        Curso javaBasico = new Curso("Java Básico", "JAVA101", "Fundamentos del lenguaje Java: variables, ciclos, POO.");
        javaBasico.asignarDocente(docente1);
        javaBasico = cursos.save(javaBasico);

        Curso springFramework = new Curso("Spring Framework", "SPRING201", "Desarrollo de aplicaciones web con Spring Boot.");
        springFramework.asignarDocente(docente1);
        springFramework = cursos.save(springFramework);

        Curso baseDatos = new Curso("Bases de Datos", "BD101", "Modelado relacional y SQL.");
        baseDatos.asignarDocente(docente2);
        baseDatos = cursos.save(baseDatos);

        Curso frontend = new Curso("Frontend Web", "WEB101", "HTML, CSS y JavaScript esencial.");
        frontend.asignarDocente(docente2);
        frontend = cursos.save(frontend);

        for (Persona est : new Persona[]{est1, est2, est3, est4, est5}) {
            javaBasico.inscribir(est);
            springFramework.inscribir(est);
            baseDatos.inscribir(est);
            frontend.inscribir(est);
        }

        cursos.save(javaBasico);
        cursos.save(springFramework);
        cursos.save(baseDatos);
        cursos.save(frontend);

        practicas.save(new Practica("Variables y ciclos", "Resolver 5 ejercicios de lógica básica.", LocalDate.now().plusDays(7), javaBasico));
        practicas.save(new Practica("Programación orientada a objetos", "Crear una clase Persona con herencia.", LocalDate.now().plusDays(14), javaBasico));
        practicas.save(new Practica("Controladores REST", "Crear un CRUD básico con Spring MVC.", LocalDate.now().plusDays(10), springFramework));
        practicas.save(new Practica("Modelo entidad-relación", "Diseñar el modelo de una tienda online.", LocalDate.now().plusDays(7), baseDatos));
        practicas.save(new Practica("Maquetación responsive", "Crear una landing page adaptable a móvil.", LocalDate.now().plusDays(5), frontend));

        Evaluacion prueba1Java = evaluaciones.save(new Evaluacion("Prueba 1", javaBasico));
        Evaluacion control1Spring = evaluaciones.save(new Evaluacion("Control 1", springFramework));
        Evaluacion prueba1BD = evaluaciones.save(new Evaluacion("Prueba 1", baseDatos));

        notas.save(new Nota(6.2, prueba1Java, est1));
        notas.save(new Nota(5.5, prueba1Java, est2));
        notas.save(new Nota(6.8, control1Spring, est1));
        notas.save(new Nota(5.9, control1Spring, est4));
        notas.save(new Nota(6.0, prueba1BD, est2));
    }

    /**
     * Crea una persona con cuenta ya activa, lista para iniciar sesión.
     *
     * @param nombre nombre completo
     * @param email email de la persona
     * @param rut RUT de la persona
     * @param rol rol a asignar
     * @param password contraseña en texto plano (se encripta antes de guardar)
     * @return la persona creada
     */
    private Persona crearActiva(String nombre, String email, String rut, Rol rol, String password) {
        Persona persona = new Persona(nombre, email, rut, rol);
        persona.activarCuenta(encoder.encode(password));
        return personas.save(persona);
    }

    /**
     * Crea una persona precargada, sin cuenta activa, para probar el
     * flujo de activación de cuenta.
     *
     * @param nombre nombre completo
     * @param email email de la persona
     * @param rut RUT de la persona
     * @param rol rol a asignar
     * @return la persona creada
     */
    private Persona crearSinActivar(String nombre, String email, String rut, Rol rol) {
        return personas.save(new Persona(nombre, email, rut, rol));
    }
}