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
 */

package com.phoenix.common.jsonwebtoken;

import com.phoenix.common.exception.runtime.JwtException;
import com.phoenix.common.exception.security.InvalidKeyException;
import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.jws.Jws;
import com.phoenix.common.jsonwebtoken.jws.Jwts;
import com.phoenix.common.jsonwebtoken.crypto.Keys;
import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.util.Base64;
import org.junit.Test;

import javax.crypto.SecretKey;

public class TestJwt {
    @Test
    public void testGenerateKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        String secretString = Base64.encodeBytes(key.getEncoded());

        System.out.println(key);
        System.out.println(secretString);
    }

    @Test
    public void createJws() throws InvalidKeyException, java.security.InvalidKeyException {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        String secretString = Base64.encodeBytes(key.getEncoded());

        System.out.println(secretString);

        String jws = Jwts.builder() // (1)

                .setSubject("Bob")      // (2)

                .signWith(key)          // (3)

                .compact();             // (4)

        System.out.println(jws);
    }

    @Test
    public void testReadJws() {
        Jws<Claims> jws;
        String key = "4hmssUz2ElsGPPVWK5MY+pmqtSFyPOXHKqYWGnm1VjAnhfIpqlx1EiH7EN03ZGhFrguJdVxQ5WrW/hiGW/OZ4Q==";
        String jwsString = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJCb2IifQ.gc-FnKrfBZbe0dsUVyBgUIxs06dBbPJIutriJJxHjVx_Qf24xEViQ0hk2GG0o7a8_yAxLrPukC6WFdOt9DwpTA";

        try {
            jws = Jwts.parserBuilder()  // (1)
                    .setSigningKey(key)         // (2)
                    .build()                    // (3)
                    .parseClaimsJws(jwsString); // (4)

            System.out.println(jws.getHeader());
            System.out.println(jws.getBody());
            System.out.println(jws.getSignature());
            // we can safely trust the JWT
        } catch (JwtException ex) {       // (5)

            // we *cannot* use the JWT as intended by its creator
        }
    }
}
