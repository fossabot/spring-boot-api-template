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
import com.phoenix.common.util.IdGenerator;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public void createJwt() throws InvalidKeyException, java.security.InvalidKeyException {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String kid = Keys.createKeyId(key);

        String secretString = Base64.encodeBytes(key.getEncoded());

        System.out.println(secretString);
        Date expiration = new Date();
        Date notBefore = new Date();

        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "JWT");
        headers.put("kid", kid);

        Map<String, String> claims = new HashMap<>();
        claims.put("id", "123456789");


//        String jws = Jwts.builder()
//                .setIssuer("me")
//                .setSubject("Bob")
//                .setAudience("you")
//                .setExpiration(expiration) //a java.util.Date
//                .setNotBefore(notBefore) //a java.util.Date
//                .setIssuedAt(new Date()) // for example, now
//                .setId(IdGenerator.generate())
//                .signWith(key)
//                .compact(); //just an example id

        String jws = Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

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

    @Test
    public void testSaveKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        String secretString = Base64.encodeBytes(key.getEncoded());
        String keyId = Keys.createKeyId(key);

        KeyWrapper keyWrapper = new KeyWrapper(secretString, key, keyId);

        writeObjectToFile(keyWrapper);
        KeyWrapper wrapper = (KeyWrapper) readObjectFromFile("J:\\spring-boot-api-template\\MODULE-COMMON\\src\\main\\resources\\test.dat");

        System.out.println(keyWrapper);
        System.out.println(wrapper);
        System.out.println(keyWrapper.equals(wrapper));
    }

    private void writeObjectToFile(Object serObj) {
        try {
            FileOutputStream fileOut = new FileOutputStream("J:\\spring-boot-api-template\\MODULE-COMMON\\src\\main\\resources\\test.dat");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object readObjectFromFile(String filepath) {
        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            System.out.println("The Object has been read from the file");
            objectIn.close();
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
