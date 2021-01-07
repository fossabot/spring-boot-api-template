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

package com.phoenix.common.jsonwebtoken.jws;

import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.component.Header;
import com.phoenix.common.jsonwebtoken.component.JwsHeader;
import com.phoenix.common.jsonwebtoken.parser.JwtParserBuilder;
import com.phoenix.common.lang.Classes;

import java.util.Map;

/**
 * Factory class useful for creating instances of JWT interfaces.  Using this factory class can be a good
 * alternative to tightly coupling your code to implementation classes.
 *
 * @since 0.1
 */
public final class Jwts {

    private static final Class[] MAP_ARG = new Class[]{Map.class};

    private static final String DEFAULT_HEADER_CLASS = "com.phoenix.common.jsonwebtoken.component.DefaultHeader";
    private static final String DEFAULT_JWS_HEADER_CLASS = "com.phoenix.common.jsonwebtoken.component.DefaultJwsHeader";
    private static final String DEFAULT_CLAIMS_CLASS = "com.phoenix.common.jsonwebtoken.component.DefaultClaims";
    private static final String DEFAULT_JWT_PARSER_CLASS = "com.phoenix.common.jsonwebtoken.parser.DefaultJwtParser";
    private static final String DefaultJwtParserBuilder = "com.phoenix.common.jsonwebtoken.parser.DefaultJwtParserBuilder";
    private static final String DefaultJwtBuilder = "com.phoenix.common.jsonwebtoken.jws.DefaultJwtBuilder";
    private static final String DEFAULT_JWT_PARSER_BUILDER_CLASS = "com.phoenix.common.jsonwebtoken.parser.DefaultJwtParserBuilder";
    private static final String DEFAULT_JWT_BUILDER_CLASS = "com.phoenix.common.jsonwebtoken.jws.DefaultJwtBuilder";

    private Jwts() {
    }

    /**
     * Creates a new {@link Header} instance suitable for <em>plaintext</em> (not digitally signed) JWTs.  As this
     * is a less common use of JWTs, consider using the {@link #jwsHeader()} factory method instead if you will later
     * digitally sign the JWT.
     *
     * @return a new {@link Header} instance suitable for <em>plaintext</em> (not digitally signed) JWTs.
     */
    public static Header header() {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultHeader");
        return Classes.newInstance(DEFAULT_HEADER_CLASS);
    }

    /**
     * Creates a new {@link Header} instance suitable for <em>plaintext</em> (not digitally signed) JWTs, populated
     * with the specified name/value pairs.  As this is a less common use of JWTs, consider using the
     * {@link #jwsHeader(java.util.Map)} factory method instead if you will later digitally sign the JWT.
     *
     * @return a new {@link Header} instance suitable for <em>plaintext</em> (not digitally signed) JWTs.
     */
    public static Header header(Map<String, Object> header) {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultHeader", MAP_ARG, header);
        return Classes.newInstance(DEFAULT_HEADER_CLASS, MAP_ARG, header);
    }

    /**
     * Returns a new {@link JwsHeader} instance suitable for digitally signed JWTs (aka 'JWS's).
     *
     * @return a new {@link JwsHeader} instance suitable for digitally signed JWTs (aka 'JWS's).
     * @see JwtBuilder#setHeader(Header)
     */
    public static JwsHeader jwsHeader() {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultJwsHeader");
        return Classes.newInstance(DEFAULT_JWS_HEADER_CLASS);
    }

    /**
     * Returns a new {@link JwsHeader} instance suitable for digitally signed JWTs (aka 'JWS's), populated with the
     * specified name/value pairs.
     *
     * @return a new {@link JwsHeader} instance suitable for digitally signed JWTs (aka 'JWS's), populated with the
     * specified name/value pairs.
     * @see JwtBuilder#setHeader(Header)
     */
    public static JwsHeader jwsHeader(Map<String, Object> header) {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultJwsHeader", MAP_ARG, header);
        return Classes.newInstance(DEFAULT_JWS_HEADER_CLASS, MAP_ARG, header);
    }

    /**
     * Returns a new {@link Claims} instance to be used as a JWT body.
     *
     * @return a new {@link Claims} instance to be used as a JWT body.
     */
    public static Claims claims() {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultClaims");
        return Classes.newInstance(DEFAULT_CLAIMS_CLASS);
    }

    /**
     * Returns a new {@link Claims} instance populated with the specified name/value pairs.
     *
     * @param claims the name/value pairs to populate the new Claims instance.
     * @return a new {@link Claims} instance populated with the specified name/value pairs.
     */
    public static Claims claims(Map<String, Object> claims) {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultClaims", MAP_ARG, claims);
        return Classes.newInstance(DEFAULT_CLAIMS_CLASS, MAP_ARG, claims);
    }

    /**
     * Returns a new {@link JwtParserBuilder} instance that can be configured to create an immutable/thread-safe {@link JwtParser).
     *
     * @return a new {@link JwtParser} instance that can be configured create an immutable/thread-safe {@link JwtParser).
     */
    public static JwtParserBuilder parserBuilder() {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultJwtParserBuilder");
        return Classes.newInstance(DEFAULT_JWT_PARSER_BUILDER_CLASS);
    }

    /**
     * Returns a new {@link JwtBuilder} instance that can be configured and then used to create JWT compact serialized
     * strings.
     *
     * @return a new {@link JwtBuilder} instance that can be configured and then used to create JWT compact serialized
     * strings.
     */
    public static JwtBuilder builder() {
//        return Classes.newInstance("io.jsonwebtoken.impl.DefaultJwtBuilder");
        return Classes.newInstance(DEFAULT_JWT_BUILDER_CLASS);
    }
}