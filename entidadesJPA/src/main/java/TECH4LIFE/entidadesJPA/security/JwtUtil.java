
package TECH4LIFE.entidadesJPA.security;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//
//import org.springframework.stereotype.Component;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.jcodepoint.jwt.util.JksProperties;
//
//@Component
//public class JwtUtil {
//	private RSAPrivateKey privateKey;
//	private RSAPublicKey publicKey;
//
//	public JwtUtil(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
//		this.privateKey = privateKey;
//		this.publicKey = publicKey;
//	}
//
//
//	public String encode(String subject) {
//		return JWT.create()
//				.withSubject(subject)
//				.withExpiresAt(null)
//				.sign(Algorithm.RSA256(publicKey, privateKey) );
//	}
//
//}



import java.net.URI;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
//	private Claims getAllClaimsFromToken(String token) {
////		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//	}

    private Claims getAllClaimsFromToken(String token) {
        byte[] keyBytes = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    /* METODO ELIMINADO public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, usuario.getId().toString());
    }*/

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
//	private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//				.signWith(SignatureAlgorithm.HS512, secret).compact();
//	}

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        byte[] keyBytes = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }
//AGREGADO PARA TESTS DE MARTIN  SOBRE SEGURIDAD:

   public String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UsuarioNoAutorizado();
        }
        return authHeader.substring(7); // Remueve el prefijo "Bearer "
    }

    public boolean isAdmin(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Boolean isAdmin = claims.get("administrador", Boolean.class);
        return isAdmin;
    }

    public String doGenerateToken(String subject,boolean isAdmin) {
        byte[] keyBytes = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder().setSubject(subject)
                .claim("administrador",isAdmin)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }
//FIN DE TESTS SOBRE SEGURIDAD


    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    

    public static URI uriBuilder(String scheme, String host, int port, String path,
        Map<String, String> queryParams) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port).path(path);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            ub.queryParam(entry.getKey(), entry.getValue());
        }
        return ub.build();
    }

    //AGREGADO PARA LOS TESTS:

    /*public UserDetails createUserDetails(String username, String password, List<String> roles){
        List<SimpleGrantedAuthority> authorities = roles.stream()
        .map(SimpleGrantedAuthority :: new).toList();
        return new User(username, password, authorities);
    }*/
}

