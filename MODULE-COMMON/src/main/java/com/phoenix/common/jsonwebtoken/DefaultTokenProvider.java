/*
 * MIT License
 *
 * Copyright (c) 2020 Đình Tài
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.phoenix.common.jsonwebtoken;

import com.phoenix.common.exception.runtime.*;
import com.phoenix.common.exception.security.InvalidKeyException;
import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.jsonwebtoken.jws.Jws;
import com.phoenix.common.jsonwebtoken.jws.Jwts;
import com.phoenix.common.util.IdGenerator;

import java.util.Date;
import java.util.Map;

public class DefaultTokenProvider implements TokenProvider {

    private final KeyWrapper keyWrapper;
    private long DEFAULT_EXPIRATION = 604800000L;

    public DefaultTokenProvider(KeyProvider keyProvider) {
        this.keyWrapper = keyProvider.getKeyWrapper();
    }

    @Override
    @Deprecated
    public String generateToken() {

        return null;
    }

    @Override
    public String generateTokenFromClaims(Map claim) throws InvalidKeyException, java.security.InvalidKeyException {
        return generateTokenFromClaims(claim, DEFAULT_EXPIRATION);
    }

    @Override
    public String generateTokenFromClaims(Map claim, long expiration) throws InvalidKeyException, java.security.InvalidKeyException {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String jws = Jwts.builder()
                .setId(IdGenerator.generate()) //jti
                .setExpiration(expiryDate) //set ngày hết hạn token
                .setIssuedAt(now) // set thời gian tạo token
                .setClaims(claim)
                .signWith(keyWrapper.getKey(), SignatureAlgorithm.HS256) //Kí với key và thuật toán HS256
                .compact();

        return jws;
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(keyWrapper.getKey())
                .build()
                .parseClaimsJws(token);
        return jws.getBody();
    }

    @Override
    public String getSubjectFromToken(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(keyWrapper.getKey())
                .build()
                .parseClaimsJws(token);
        return jws.getBody().getSubject();
    }

    @Override
    public long getExpiryDuration() {
        return DEFAULT_EXPIRATION;
    }

    @Override
    public Date getTokenExpiryFromToken(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(keyWrapper.getKey())
                .build()
                .parseClaimsJws(token);

        return jws.getBody().getExpiration();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keyWrapper.getKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (SignatureException ex) {
            throw new InvalidTokenException("JWT", token, "Incorrect signature");

        } catch (MalformedJwtException ex) {
            throw new InvalidTokenException("JWT", token, "Malformed jwt token");

        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("JWT", token, "Token expired. Refresh required");

        } catch (UnsupportedJwtException ex) {
            throw new InvalidTokenException("JWT", token, "Unsupported JWT token");

        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("JWT", token, "Illegal argument token");
        }
        return true;
    }
}




