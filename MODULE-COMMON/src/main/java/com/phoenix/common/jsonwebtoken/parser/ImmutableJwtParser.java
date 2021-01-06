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

package com.phoenix.common.jsonwebtoken.parser;
import com.phoenix.common.exception.runtime.ExpiredJwtException;
import com.phoenix.common.exception.runtime.MalformedJwtException;
import com.phoenix.common.exception.runtime.SignatureException;
import com.phoenix.common.exception.runtime.UnsupportedJwtException;
import com.phoenix.common.jsonwebtoken.common.*;
import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.component.Header;
import com.phoenix.common.jsonwebtoken.compression.CompressionCodecResolver;
import com.phoenix.common.jsonwebtoken.signature.SigningKeyResolver;

import java.security.Key;
import java.util.Date;
import java.util.Map;
/**
 * This JwtParser implementation exists as a stop gap until the mutable methods are removed from JwtParser.
 * TODO: remove this class BEFORE 1.0
 * @since 0.11.0
 */
class ImmutableJwtParser implements JwtParser {

    private final JwtParser jwtParser;

    ImmutableJwtParser(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    private IllegalStateException doNotMutate() {
        return new IllegalStateException("Cannot mutate a JwtParser created from JwtParserBuilder.build(), " +
                "the mutable methods in JwtParser will be removed before version 1.0");
    }

    @Override
    public boolean isSigned(String jwt) {
        return this.jwtParser.isSigned(jwt);
    }

    @Override
    public Jwt parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return this.jwtParser.parse(jwt);
    }

    @Override
    public <T> T parse(String jwt, JwtHandler<T> handler) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return this.jwtParser.parse(jwt, handler);
    }

    @Override
    public Jwt<Header, String> parsePlaintextJwt(String plaintextJwt) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return this.jwtParser.parsePlaintextJwt(plaintextJwt);
    }

    @Override
    public Jwt<Header, Claims> parseClaimsJwt(String claimsJwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return this.jwtParser.parseClaimsJwt(claimsJwt);
    }

    @Override
    public Jws<String> parsePlaintextJws(String plaintextJws) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return this.jwtParser.parsePlaintextJws(plaintextJws);
    }

    @Override
    public Jws<Claims> parseClaimsJws(String claimsJws) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return this.jwtParser.parseClaimsJws(claimsJws);
    }
}
