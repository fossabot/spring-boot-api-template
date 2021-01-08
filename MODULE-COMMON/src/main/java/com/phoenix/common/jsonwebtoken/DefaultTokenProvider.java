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

import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.jws.Jwts;
import com.phoenix.common.util.IdGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultTokenProvider implements TokenProvider {

    @Override
    public String generateToken() {
        return null;
    }

    @Override
    public String generateKey() {
        return null;
    }

    @Override
    public String generateTokenFromClaims(Claims claims) {
        Date expiration = new Date();
        Date notBefore = new Date();

        Map<String, String> headers = new HashMap<>();
        headers.put("type", "JWT");

        String jws = Jwts.builder()
                .setIssuer("me")
                .setSubject("Bob")
                .setAudience("you")
                .setExpiration(expiration) //a java.util.Date
                .setNotBefore(notBefore) //a java.util.Date
                .setIssuedAt(new Date()) // for example, now
                .setId(IdGenerator.generate())
                .compact(); //just an example id
        return null;
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
