package com.example.backofficeVoiture.util;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.util.Date;

import com.example.backofficeVoiture.domain.Utilisateur;
import com.example.backofficeVoiture.models.admin.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static long expiryDuration = 60 * 60;

    public String userToken(Utilisateur utilisateur){
        long milliTime = System.currentTimeMillis();
        long expiryTime = milliTime + expiryDuration * 1000;

        Date issuedAt = new Date(milliTime);
        Date expiryAt = new Date(expiryTime);

        // claims
        Claims claims = Jwts.claims()
                .setIssuer(utilisateur.getIdUtilisateur())
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt);

        // optional claims
        claims.put("nom", utilisateur.getNom());
        claims.put("prenom", utilisateur.getPrenom());
        claims.put("email",utilisateur.getEmail());
        claims.put("idUtilisateur", utilisateur.getIdUtilisateur());

        // generate jwt using claims
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return token;
    }
    public String generate(Admin admin) {
        long milliTime = System.currentTimeMillis();
        long expiryTime = milliTime + expiryDuration * 1000;

        Date issuedAt = new Date(milliTime);
        Date expiryAt = new Date(expiryTime);

        // claims
        Claims claims = Jwts.claims()
                .setIssuer(admin.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt);

        // optional claims
        claims.put("userName", admin.getUserName());
        claims.put("password", admin.getPassword());

        // generate jwt using claims
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return token;

    }

    public Claims verify(String authorization) throws Exception {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(authorization).getBody();
            return claims;
        } catch (Exception e) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    public Utilisateur findUserByToken(String authorization) throws AccessDeniedException {
        authorization = authorization.replace("Bearer ", "");
        System.out.println("your token "+authorization);
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(authorization).getBody();
            System.out.println("user id "+claims.getIssuer());
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setIdUtilisateur(claims.getIssuer());
            return utilisateur;
        } catch (Exception e) {
            throw new AccessDeniedException("Access Denied");
        }
    }

}
