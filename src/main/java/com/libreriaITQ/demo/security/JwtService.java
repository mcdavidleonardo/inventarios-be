package com.libreriaITQ.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private record UserData(String nombre, String rol, String clave_hash) {}

    private UserData getUserDataFromDb(String email, String tokenClave) {
        String sql = "exec sp_usuario @operacion = 'Q', @email = ?, @clave = ?";

        List<Map<String, Object>> resultados = jdbcTemplate.queryForList(sql, email, tokenClave);

        if (resultados.isEmpty()) {
            throw new RuntimeException("Acceso denegado: Usuario inexistente o credenciales desactualizadas.");
        }

        Map<String, Object> row = resultados.get(0);
        return new UserData(
                (String) row.get("nombre"),
                (String) row.get("id_rol"),
                tokenClave
        );
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValidInDatabase(String email) {
        String sql = "exec sp_usuario @operacion = 'V', @email = ?";
        System.out.println(email);
        List<Map<String, Object>> resultados = jdbcTemplate.queryForList(sql, email);
        return !resultados.isEmpty();
    }

    public String loginAndGenerateToken(String email, String clavePlana) {
        UserData user = getUsuarioByEmail(email);
        if (user == null || !passwordEncoder.matches(clavePlana, user.clave_hash())) {
            throw new RuntimeException("Credenciales incorrectas: Email o clave no válidos.");
        }

        UserData validatedUser = getUserDataFromDb(email, user.clave_hash());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("nombre", validatedUser.nombre());
        extraClaims.put("rol", validatedUser.rol());
        extraClaims.put("clave", user.clave_hash());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private UserData getUsuarioByEmail(String email) {
        String sql = "exec sp_usuario @operacion = 'V', @email = ?";

        List<UserData> results = jdbcTemplate.query(sql, (rs, rowNum) ->
                        new UserData(
                                rs.getString("nombre"),
                                rs.getString("id_rol"),
                                rs.getString("clave")
                        ),
                email
        );

        return results.isEmpty() ? null : results.get(0);
    }

    public String extractRol(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}