// package config;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureException;
// import io.jsonwebtoken.ExpiredJwtException;
// import static com.springboot.backend.andres.usersapp.usersbackend.auth.TokenJwtConfig.*;

// public class JwtValidator {

//     public static Claims validateToken(String token) throws SignatureException, ExpiredJwtException {
//         try {

//             Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build()
//                     .parseSignedClaims(token).getPayload();

//             return claims;
//         } catch (ExpiredJwtException e) {
//             throw new ExpiredJwtException(null, null, "El token ha expirado", e);
//         } catch (SignatureException e) {
//             throw new SignatureException("Firma no válida", e);
//         }
//     }
// }
