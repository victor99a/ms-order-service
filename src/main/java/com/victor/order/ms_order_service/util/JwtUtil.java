// ms-order-service/src/main/java/.../util/JwtUtil.java
package com.victor.order.ms_order_service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 👇 la leemos desde application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validarToken(String token) {
        try {
            getClaims(token); // si no lanza excepción, el token es válido
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // opcional: loguear para debug
            System.out.println("❌ JWT inválido en ms-order: " + e.getMessage());
            return false;
        }
    }

    public String obtenerEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        Object rol = getClaims(token).get("rol");
        return rol != null ? rol.toString() : null;
    }

    public Date obtenerExpiracion(String token) {
        return getClaims(token).getExpiration();
    }
}
