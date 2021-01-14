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

import com.phoenix.common.jsonwebtoken.common.Clock;
import com.phoenix.common.jsonwebtoken.common.DefaultClock;
import com.phoenix.common.jsonwebtoken.common.Deserializer;
import com.phoenix.common.jsonwebtoken.common.GsonDeserializer;
import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.component.DefaultClaims;
import com.phoenix.common.jsonwebtoken.compression.CompressionCodecResolver;
import com.phoenix.common.jsonwebtoken.compression.DefaultCompressionCodecResolver;
import com.phoenix.common.jsonwebtoken.jws.JwtParser;
import com.phoenix.common.jsonwebtoken.signature.SigningKeyResolver;
import com.phoenix.common.lang.Assert;
import com.phoenix.common.util.Base64;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class DefaultJwtParserBuilder implements JwtParserBuilder {

    private static Logger logger = Logger.getLogger(DefaultJwtParserBuilder.class);

    private static final int MILLISECONDS_PER_SECOND = 1000;

    /**
     * To prevent overflow per <a href="https://github.com/jwtk/jjwt/issues/583">Issue 583</a>.
     * <p>
     * Package-protected on purpose to allow use in backwards-compatible {@link DefaultJwtParser} implementation.
     */
    static final long MAX_CLOCK_SKEW_MILLIS = Long.MAX_VALUE / MILLISECONDS_PER_SECOND;
    static final String MAX_CLOCK_SKEW_ILLEGAL_MSG = "Illegal allowedClockSkewMillis value: multiplying this " +
            "value by 1000 to obtain the number of milliseconds would cause a numeric overflow.";

    private byte[] keyBytes;

    private Key key;

    private SigningKeyResolver signingKeyResolver;

    private CompressionCodecResolver compressionCodecResolver;


    private Deserializer<Map<String, ?>> deserializer;

    private Claims expectedClaims = new DefaultClaims();

    private Clock clock = DefaultClock.INSTANCE;

    private long allowedClockSkewMillis = 0;


    @Override
    public JwtParserBuilder deserializeJsonWith(Deserializer<Map<String, ?>> deserializer) {
        Assert.notNull(deserializer, "deserializer cannot be null.");
        this.deserializer = deserializer;
        return this;
    }

    @Override
    public JwtParserBuilder requireIssuedAt(Date issuedAt) {
        expectedClaims.setIssuedAt(issuedAt);
        return this;
    }

    @Override
    public JwtParserBuilder requireIssuer(String issuer) {
        expectedClaims.setIssuer(issuer);
        return this;
    }

    @Override
    public JwtParserBuilder requireAudience(String audience) {
        expectedClaims.setAudience(audience);
        return this;
    }

    @Override
    public JwtParserBuilder requireSubject(String subject) {
        expectedClaims.setSubject(subject);
        return this;
    }

    @Override
    public JwtParserBuilder requireId(String id) {
        expectedClaims.setId(id);
        return this;
    }

    @Override
    public JwtParserBuilder requireExpiration(Date expiration) {
        expectedClaims.setExpiration(expiration);
        return this;
    }

    @Override
    public JwtParserBuilder requireNotBefore(Date notBefore) {
        expectedClaims.setNotBefore(notBefore);
        return this;
    }

    @Override
    public JwtParserBuilder require(String claimName, Object value) {
        Assert.hasText(claimName, "claim name cannot be null or empty.");
        Assert.notNull(value, "The value cannot be null for claim name: " + claimName);
        expectedClaims.put(claimName, value);
        return this;
    }

    @Override
    public JwtParserBuilder setClock(Clock clock) {
        Assert.notNull(clock, "Clock instance cannot be null.");
        this.clock = clock;
        return this;
    }

    @Override
    public JwtParserBuilder setAllowedClockSkewSeconds(long seconds) throws IllegalArgumentException {
        Assert.isTrue(seconds <= MAX_CLOCK_SKEW_MILLIS, MAX_CLOCK_SKEW_ILLEGAL_MSG);
        this.allowedClockSkewMillis = Math.max(0, seconds * MILLISECONDS_PER_SECOND);
        return this;
    }

    @Override
    public JwtParserBuilder setSigningKey(byte[] key) {
        Assert.notEmpty(key, "signing key cannot be null or empty.");
        this.keyBytes = key;
        return this;
    }

    @Override
    public JwtParserBuilder setSigningKey(String base64EncodedSecretKey) {
        Assert.hasText(base64EncodedSecretKey, "signing key cannot be null or empty.");
        try {
            this.keyBytes = Base64.decode(base64EncodedSecretKey);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return this;
    }

    @Override
    public JwtParserBuilder setSigningKey(Key key) {
        Assert.notNull(key, "signing key cannot be null.");
        this.key = key;
        return this;
    }

    @Override
    public JwtParserBuilder setSigningKeyResolver(SigningKeyResolver signingKeyResolver) {
        Assert.notNull(signingKeyResolver, "SigningKeyResolver cannot be null.");
        this.signingKeyResolver = signingKeyResolver;
        return this;
    }

    @Override
    public JwtParserBuilder setCompressionCodecResolver(CompressionCodecResolver compressionCodecResolver) {
        Assert.notNull(compressionCodecResolver, "compressionCodecResolver cannot be null.");
        this.compressionCodecResolver = compressionCodecResolver;
        return this;
    }

    @Override
    public JwtParser build() {

        // Only lookup the deserializer IF it is null. It is possible a Deserializer implementation was set
        // that is NOT exposed as a service and no other implementations are available for lookup.
        if (this.deserializer == null) {
            // try to find one based on the services available:
            //this.deserializer = Services.loadFirst(Deserializer.class);
            this.deserializer = new GsonDeserializer<>();
        }

        // if the compressionCodecResolver is not set default it.
        if (this.compressionCodecResolver == null) {
            this.compressionCodecResolver = new DefaultCompressionCodecResolver();
        }


        return new DefaultJwtParser(signingKeyResolver,
                key,
                keyBytes,
                clock,
                allowedClockSkewMillis,
                expectedClaims,
                deserializer,
                compressionCodecResolver);
    }
}
