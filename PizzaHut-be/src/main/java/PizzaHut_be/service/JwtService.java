package PizzaHut_be.service;

import PizzaHut_be.dao.UserDao;
import PizzaHut_be.model.constant.RedisPrefixKeyConstant;
import PizzaHut_be.util.Util;
import io.jsonwebtoken.*;
import lombok.CustomLog;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Data
@CustomLog
@Service
@RequiredArgsConstructor
public class JwtService {
    private final JedisPool jedisPool;
    private final UserDao userDao;
    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expire}")
    private int jwtExpirationMs;

    /***
     * Generate JWT token by user id and save to redis
     * @param userId
     * @return
     */
    public String generateJwtTokenByUserId(String userId) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + jwtExpirationMs);
        String tokenGenerate = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = Util.generateRedisKey(RedisPrefixKeyConstant.TOKEN, tokenGenerate);
            jedis.set(key, userId);
            jedis.expire(key, jwtExpirationMs / 1000);
        } catch (Exception e) {
            log.error("Save token to redis failed: ", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return tokenGenerate;
    }

    /***
     * Get user id from JWT token
     * @param token
     * @return
     */
    public String getUserIdFromJWT(String token) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            String userId = jedis.get(Util.generateRedisKey(RedisPrefixKeyConstant.TOKEN, token));

            return userId;
        } catch (Exception e) {
            log.error("Get user id by jwt token failed: ", e);

            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /***
     * Validate JWT token
     * @param authToken
     * @return
     */
    public boolean validateToken(String authToken) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String userId = null;

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(authToken)
                        .getBody();

                userId = claims.getSubject();
            } catch (Exception e) {
                log.error("jwt token not verify with jwt secret");

                return false;
            }

            // Get user access token from redis and validate
            String jwtsFromRedis = jedis.get(Util.generateRedisKey(RedisPrefixKeyConstant.TOKEN, authToken));

            if (!Util.isNullOrEmpty(jwtsFromRedis) && !Util.isNullOrEmpty(userId) && jwtsFromRedis.equals(userId)) {
                return true;
            }
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: ", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: ", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty. ", ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    /***
     * Generate refresh token and save to redis
     * @param userId
     * @return
     */
    public String generateRefreshToken(String userId) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + jwtExpirationMs);
        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = Util.generateRedisKey(RedisPrefixKeyConstant.REFRESH_TOKEN, refreshToken);
            jedis.set(key, userId);
            jedis.expire(key, jwtExpirationMs / 1000);
        } catch (Exception e) {
            log.error("Save refresh token to redis failed: ", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return refreshToken;
    }

    /***
     * Refresh JWT token using refresh token
     * @param refreshToken
     * @return
     */
    public String refreshToken(String refreshToken) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String userId = jedis.get(Util.generateRedisKey(RedisPrefixKeyConstant.REFRESH_TOKEN, refreshToken));
            if (userId != null) {
                return generateJwtTokenByUserId(userId);
            } else {
                log.error("Invalid refresh token");
                return null;
            }
        } catch (Exception e) {
            log.error("Failed to refresh token: ", e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
