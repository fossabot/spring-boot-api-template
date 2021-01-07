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

package com.phoenix.common.jsonwebtoken.common;

import com.phoenix.common.exception.ioe.SerializationException;
import com.phoenix.common.exception.security.InvalidKeyException;
import com.phoenix.common.jsonwebtoken.compression.CompressionCodec;
import com.phoenix.common.jsonwebtoken.component.*;
import com.phoenix.common.jsonwebtoken.signature.DefaultJwtSigner;
import com.phoenix.common.jsonwebtoken.signature.JwtSigner;
import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.lang.Assert;
import com.phoenix.common.lang.Collections;
import com.phoenix.common.lang.Services;
import com.phoenix.common.lang.Strings;
import com.phoenix.common.util.Base64;
import com.phoenix.common.util.Base64Url;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class DefaultJwtBuilder implements JwtBuilder {

    private Header header;
    private Claims claims;
    private String payload;

    private SignatureAlgorithm algorithm;
    private Key key;

    private Serializer<Map<String, ?>> serializer;


    private CompressionCodec compressionCodec;

    @Override
    public JwtBuilder serializeToJsonWith(Serializer<Map<String, ?>> serializer) {
        Assert.notNull(serializer, "Serializer cannot be null.");
        this.serializer = serializer;
        return this;
    }

    @Override
    public JwtBuilder setHeader(Header header) {
        this.header = header;
        return this;
    }

    @Override
    public JwtBuilder setHeader(Map<String, Object> header) {
        this.header = new DefaultHeader(header);
        return this;
    }

    @Override
    public JwtBuilder setHeaderParams(Map<String, Object> params) {
        if (!Collections.isEmpty(params)) {

            Header header = ensureHeader();

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                header.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    protected Header ensureHeader() {
        if (this.header == null) {
            this.header = new DefaultHeader();
        }
        return this.header;
    }

    @Override
    public JwtBuilder setHeaderParam(String name, Object value) {
        ensureHeader().put(name, value);
        return this;
    }

    @Override
    public JwtBuilder signWith(Key key) throws InvalidKeyException {
        Assert.notNull(key, "Key argument cannot be null.");
        SignatureAlgorithm alg = SignatureAlgorithm.forSigningKey(key);
        return signWith(key, alg);
    }

    @Override
    public JwtBuilder signWith(Key key, SignatureAlgorithm alg) throws InvalidKeyException {
        Assert.notNull(key, "Key argument cannot be null.");
        Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
        alg.assertValidSigningKey(key); //since 0.10.0 for https://github.com/jwtk/jjwt/issues/334
        this.algorithm = alg;
        this.key = key;
        return this;
    }

    @Override
    public JwtBuilder compressWith(CompressionCodec compressionCodec) {
        Assert.notNull(compressionCodec, "compressionCodec cannot be null");
        this.compressionCodec = compressionCodec;
        return this;
    }

    @Override
    public JwtBuilder setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    protected Claims ensureClaims() {
        if (this.claims == null) {
            this.claims = new DefaultClaims();
        }
        return this.claims;
    }

    @Override
    public JwtBuilder setClaims(Claims claims) {
        this.claims = claims;
        return this;
    }

    @Override
    public JwtBuilder setClaims(Map<String, ?> claims) {
        this.claims = new DefaultClaims(claims);
        return this;
    }

    @Override
    public JwtBuilder addClaims(Map<String, Object> claims) {
        ensureClaims().putAll(claims);
        return this;
    }

    @Override
    public JwtBuilder setIssuer(String iss) {
        if (Strings.hasText(iss)) {
            ensureClaims().setIssuer(iss);
        } else {
            if (this.claims != null) {
                claims.setIssuer(iss);
            }
        }
        return this;
    }

    @Override
    public JwtBuilder setSubject(String sub) {
        if (Strings.hasText(sub)) {
            ensureClaims().setSubject(sub);
        } else {
            if (this.claims != null) {
                claims.setSubject(sub);
            }
        }
        return this;
    }

    @Override
    public JwtBuilder setAudience(String aud) {
        if (Strings.hasText(aud)) {
            ensureClaims().setAudience(aud);
        } else {
            if (this.claims != null) {
                claims.setAudience(aud);
            }
        }
        return this;
    }

    @Override
    public JwtBuilder setExpiration(Date exp) {
        if (exp != null) {
            ensureClaims().setExpiration(exp);
        } else {
            if (this.claims != null) {
                //noinspection ConstantConditions
                this.claims.setExpiration(exp);
            }
        }
        return this;
    }

    @Override
    public JwtBuilder setNotBefore(Date nbf) {
        if (nbf != null) {
            ensureClaims().setNotBefore(nbf);
        } else {
            if (this.claims != null) {
                //noinspection ConstantConditions
                this.claims.setNotBefore(nbf);
            }
        }
        return this;
    }

    @Override
    public JwtBuilder setIssuedAt(Date iat) {
        if (iat != null) {
            ensureClaims().setIssuedAt(iat);
        } else {
            if (this.claims != null) {
                //noinspection ConstantConditions
                this.claims.setIssuedAt(iat);
            }
        }
        return this;
    }

    @Override
    public JwtBuilder setId(String jti) {
        if (Strings.hasText(jti)) {
            ensureClaims().setId(jti);
        } else {
            if (this.claims != null) {
                claims.setId(jti);
            }
        }
        return this;
    }

    @Override
    public JwtBuilder claim(String name, Object value) {
        Assert.hasText(name, "Claim property name cannot be null or empty.");
        if (this.claims == null) {
            if (value != null) {
                ensureClaims().put(name, value);
            }
        } else {
            if (value == null) {
                this.claims.remove(name);
            } else {
                this.claims.put(name, value);
            }
        }

        return this;
    }

    @Override
    public String compact() {

        if (this.serializer == null) {
            // try to find one based on the services available
            // use the previous commented out line instead
//            this.serializer = Services.loadFirst(Serializer.class);
            this.serializer = new GsonSerializer<>();
        }

        if (payload == null && Collections.isEmpty(claims)) {
            payload = "";
        }

        if (payload != null && !Collections.isEmpty(claims)) {
            throw new IllegalStateException("Both 'payload' and 'claims' cannot both be specified. Choose either one.");
        }

        Header header = ensureHeader();

        JwsHeader jwsHeader;
        if (header instanceof JwsHeader) {
            jwsHeader = (JwsHeader) header;
        } else {
            //noinspection unchecked
            jwsHeader = new DefaultJwsHeader(header);
        }

        if (key != null) {
            jwsHeader.setAlgorithm(algorithm.getValue());
        } else {
            //no signature - plaintext JWT:
            jwsHeader.setAlgorithm(SignatureAlgorithm.NONE.getValue());
        }

        if (compressionCodec != null) {
            jwsHeader.setCompressionAlgorithm(compressionCodec.getAlgorithmName());
        }

        //String base64UrlEncodedHeader = base64UrlEncode(jwsHeader, "Unable to serialize header to json.");
        String base64UrlEncodedHeader = null;
        try {
            base64UrlEncodedHeader = Base64.encodeBytes(serializer.serialize(jwsHeader));
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        byte[] bytes;
        try {
            if (this.payload != null) {
                bytes = payload.getBytes(Strings.UTF_8);
            } else {
                //bytes = toJson(claims);
                bytes = serializer.serialize(claims);
            }
        } catch (SerializationException e) {
            throw new IllegalArgumentException("Unable to serialize claims object to json: " + e.getMessage(), e);
        }

        if (compressionCodec != null) {
            bytes = compressionCodec.compress(bytes);
        }

        String base64UrlEncodedBody = Base64Url.encode(bytes);

        String jwt = base64UrlEncodedHeader + JwtParser.SEPARATOR_CHAR + base64UrlEncodedBody;

        if (key != null) { //jwt must be signed:

            JwtSigner signer = createSigner(algorithm, key);

            String base64UrlSignature = signer.sign(jwt);

            jwt += JwtParser.SEPARATOR_CHAR + base64UrlSignature;
        } else {
            // no signature (plaintext), but must terminate w/ a period, see
            // https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-6.1
            jwt += JwtParser.SEPARATOR_CHAR;
        }

        return jwt;
    }

    /*
     * @since 0.5 mostly to allow testing overrides
     */
    protected JwtSigner createSigner(SignatureAlgorithm alg, Key key) {
        return new DefaultJwtSigner(alg, key);
    }

}
