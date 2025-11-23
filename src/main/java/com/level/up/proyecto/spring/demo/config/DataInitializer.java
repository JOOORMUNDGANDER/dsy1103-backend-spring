package com.level.up.proyecto.spring.demo.config;

import com.level.up.proyecto.spring.demo.model.Producto;
import com.level.up.proyecto.spring.demo.model.Rol;
import com.level.up.proyecto.spring.demo.model.Usuario;
import com.level.up.proyecto.spring.demo.repository.ProductoRepository;
import com.level.up.proyecto.spring.demo.repository.RolRepository;
import com.level.up.proyecto.spring.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Override
    public void run(String... args) {
        // Crear roles si no existen
        Rol rolUsuario = crearRolSiNoExiste(Rol.NombreRol.ROL_USUARIO);
        Rol rolAdmin = crearRolSiNoExiste(Rol.NombreRol.ROL_ADMIN);
        Rol rolModerador = crearRolSiNoExiste(Rol.NombreRol.ROL_MODERADOR);

        System.out.println(" Roles inicializados correctamente");

        // Si es perfil dev, crear datos de prueba
        if ("dev".equals(activeProfile)) {
            crearDatosDePrueba(rolUsuario, rolAdmin, rolModerador);
        }
    }

    private Rol crearRolSiNoExiste(Rol.NombreRol nombreRol) {
        return rolRepository.findByNombre(nombreRol)
                .orElseGet(() -> {
                    Rol rol = Rol.builder().nombre(nombreRol).build();
                    System.out.println("   Rol creado: " + nombreRol);
                    return rolRepository.save(rol);
                });
    }

    private void crearDatosDePrueba(Rol rolUsuario, Rol rolAdmin, Rol rolModerador) {
        System.out.println("\n Creando datos de prueba para desarrollo...");

        // Crear usuario admin
        if (!usuarioRepository.existsByNombreUsuario("admin")) {
            Set<Rol> rolesAdmin = new HashSet<>();
            rolesAdmin.add(rolAdmin);
            rolesAdmin.add(rolUsuario);

            Usuario admin = Usuario.builder()
                    .nombreUsuario("admin")
                    .email("admin@levelup.com")
                    .password(passwordEncoder.encode("admin123"))
                    .nombre("Administrador")
                    .apellido("LevelUp")
                    .roles(rolesAdmin)
                    .build();
            usuarioRepository.save(admin);
            System.out.println("  Usuario admin creado (password: admin123)");
        }

        // Crear usuario normal
        if (!usuarioRepository.existsByNombreUsuario("usuario")) {
            Set<Rol> rolesUsuario = new HashSet<>();
            rolesUsuario.add(rolUsuario);

            Usuario usuario = Usuario.builder()
                    .nombreUsuario("usuario")
                    .email("usuario@levelup.com")
                    .password(passwordEncoder.encode("usuario123"))
                    .nombre("Usuario")
                    .apellido("Prueba")
                    .direccion("Calle Falsa 123")
                    .roles(rolesUsuario)
                    .build();
            usuarioRepository.save(usuario);
            System.out.println("  Usuario normal creado (password: usuario123)");
        }

        // Crear usuario moderador
        if (!usuarioRepository.existsByNombreUsuario("moderador")) {
            Set<Rol> rolesMod = new HashSet<>();
            rolesMod.add(rolModerador);
            rolesMod.add(rolUsuario);

            Usuario moderador = Usuario.builder()
                    .nombreUsuario("moderador")
                    .email("mod@levelup.com")
                    .password(passwordEncoder.encode("mod123"))
                    .nombre("Moderador")
                    .apellido("LevelUp")
                    .roles(rolesMod)
                    .build();
            usuarioRepository.save(moderador);
            System.out.println("  Usuario moderador creado (password: mod123)");
        }

        // Crear productos de prueba
        if (productoRepository.count() == 0) {
            crearProductosDePrueba();
        }

        System.out.println("\n Datos de prueba creados exitosamente!");
        System.out.println("    Swagger UI: http://localhost:8081/swagger-ui.html");
        System.out.println("    H2 Console: http://localhost:8081/h2-console");
        System.out.println("    JDBC URL: jdbc:h2:mem:levelup_dev\n");
    }

    private void crearProductosDePrueba() {
        // Producto 1 - Mouse G502
        productoRepository.save(Producto.builder()
                .codigo("MOUSE-001")
                .categoria("mouse")
                .nombre("Mouse Gaming Logitech G502")
                .precio(59990.0)
                .precioOriginal(79990.0)
                .imagen("/assets/images/g502.jpg")
                .descripcion("Mouse gaming de alta precisión")
                .descripcionProducto("Mouse gaming Logitech G502 HERO con sensor de 25,600 DPI")
                .oferta(true)
                .stock(50)
                .activo(true)
                .build());

        // Producto 2 - Teclado BlackWidow
        productoRepository.save(Producto.builder()
                .codigo("TECLADO-001")
                .categoria("accesorios")
                .nombre("Teclado Mecánico Razer BlackWidow")
                .precio(129990.0)
                .precioOriginal(149990.0)
                .imagen("/assets/images/blackwidow.jpg")
                .descripcion("Teclado mecánico RGB")
                .descripcionProducto("Teclado mecánico Razer BlackWidow V3 con switches verdes")
                .oferta(true)
                .stock(30)
                .activo(true)
                .build());

        // Producto 3 - Silla Gamer
        productoRepository.save(Producto.builder()
                .codigo("SILLA-001")
                .categoria("sillas-gamers")
                .nombre("Silla Gamer GT Player")
                .precio(249990.0)
                .imagen("/assets/images/gtplayer.jpg")
                .descripcion("Silla ergonómica para gaming")
                .descripcionProducto("Silla gamer GT Player con soporte lumbar ajustable")
                .oferta(false)
                .stock(15)
                .activo(true)
                .build());

        // Producto 4 - Audífonos HyperX
        productoRepository.save(Producto.builder()
                .codigo("AUDI-001")
                .categoria("perifericos-streaming")
                .nombre("Audífonos HyperX Cloud II")
                .precio(89990.0)
                .imagen("/assets/images/hyperx-cloud.jpg")
                .descripcion("Audífonos gaming 7.1 surround")
                .descripcionProducto("Audífonos HyperX Cloud II con sonido surround 7.1 virtual")
                .oferta(false)
                .stock(40)
                .activo(true)
                .build());

        // Producto 5 - Mousepad Goliathus
        productoRepository.save(Producto.builder()
                .codigo("MPAD-001")
                .categoria("mousepad")
                .nombre("Mousepad RGB Razer Goliathus")
                .precio(39990.0)
                .precioOriginal(49990.0)
                .imagen("/assets/images/goliathus.jpg")
                .descripcion("Mousepad XL con iluminación RGB")
                .descripcionProducto("Mousepad Razer Goliathus Extended Chroma con RGB personalizable")
                .oferta(true)
                .stock(60)
                .activo(true)
                .build());

        // Producto 6 - Juego de Mesa Catan
        productoRepository.save(Producto.builder()
                .codigo("JMESA-001")
                .categoria("juegos-mesa")
                .nombre("Catan - Juego de Mesa")
                .precio(45990.0)
                .imagen("/assets/images/catan.jpg")
                .descripcion("El clásico juego de estrategia")
                .descripcionProducto("Catan, el famoso juego de comercio y estrategia para 3-4 jugadores")
                .oferta(false)
                .stock(25)
                .activo(true)
                .build());

        // Producto 7 - Mouse G Pro
        productoRepository.save(Producto.builder()
                .codigo("MOUSE-002")
                .categoria("mouse")
                .nombre("Mouse Logitech G Pro")
                .precio(79990.0)
                .imagen("/assets/images/g-pro.jpg")
                .descripcion("Mouse profesional para esports")
                .descripcionProducto("Mouse Logitech G Pro Wireless, usado por profesionales de esports")
                .oferta(false)
                .stock(35)
                .activo(true)
                .build());

        // Producto 8 - Silla Noble Chairs
        productoRepository.save(Producto.builder()
                .codigo("SILLA-002")
                .categoria("sillas-gamers")
                .nombre("Silla Noble Chairs Hero")
                .precio(459990.0)
                .precioOriginal(499990.0)
                .imagen("/assets/images/noblechairs.jpg")
                .descripcion("Silla premium de cuero real")
                .descripcionProducto("Silla Noble Chairs Hero Series en cuero genuino")
                .oferta(true)
                .stock(8)
                .activo(true)
                .build());

        System.out.println("  8 productos de prueba creados");
    }
}
