package com.level.up.proyecto.spring.demo.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpiration;

    // Generar token JWT
    public String generarToken(Authentication autenticacion) {
        UserDetails userPrincipal = (UserDetails) autenticacion.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(obtenerClaveSecreta())
                .compact();
    }

    // Generar token por nombre de usuario
    public String generarTokenPorNombreUsuario(String nombreUsuario) {
        return Jwts.builder()
                .subject(nombreUsuario)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(obtenerClaveSecreta())
                .compact();
    }

    // Obtener nombre de usuario del token
    public String obtenerNombreUsuarioDelToken(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveSecreta())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validar token JWT
    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(obtenerClaveSecreta())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Token JWT inválido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Token JWT expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Token JWT no soportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Token JWT vacío: " + e.getMessage());
        }
        return false;
    }

    // Obtener clave secreta
    private SecretKey obtenerClaveSecreta() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
