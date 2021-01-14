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

package com.phoenix.common.jsonwebtoken.component;

import java.util.Date;
import java.util.Map;

/**
 * A JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4">Claims set</a>.
 *
 * <p>This is ultimately a JSON map and any values can be added to it, but JWT standard names are provided as
 * type-safe getters and setters for convenience.</p>
 *
 * <p>Because this interface extends {@code Map&lt;String, Object&gt;}, if you would like to add your own properties,
 * you simply use map methods, for example:</p>
 *
 * <pre>
 * claims.{@link Map#put(Object, Object) put}("someKey", "someValue");
 * </pre>
 *
 * <h3>Creation</h3>
 *
 * <p>It is easiest to create a {@code Claims} instance by calling one of the
 * {@link Jwts#claims() JWTs.claims()} factory methods.</p>
 *
 * @since 0.1
 */
public interface Claims extends Map<String, Object>, ClaimsMutator<Claims> {

    /** JWT {@code Issuer} claims parameter name: <code>"iss"</code> */
    public static final String ISSUER = "iss";

    /** JWT {@code Subject} claims parameter name: <code>"sub"</code> */
    public static final String SUBJECT = "sub";

    /** JWT {@code Audience} claims parameter name: <code>"aud"</code> */
    public static final String AUDIENCE = "aud";

    /** JWT {@code Expiration} claims parameter name: <code>"exp"</code> */
    public static final String EXPIRATION = "exp";

    /** JWT {@code Not Before} claims parameter name: <code>"nbf"</code> */
    public static final String NOT_BEFORE = "nbf";

    /** JWT {@code Issued At} claims parameter name: <code>"iat"</code> */
    public static final String ISSUED_AT = "iat";

    /** JWT {@code JWT ID} claims parameter name: <code>"jti"</code> */
    public static final String ID = "jti";

    /**
     * Returns the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.1">
     * <code>iss</code></a> (issuer) value or {@code null} if not present.
     *
     * @return the JWT {@code iss} value or {@code null} if not present.
     */
    String getIssuer();

    /**
     * {@inheritDoc}
     */
    @Override //only for better/targeted JavaDoc
    Claims setIssuer(String iss);

    /**
     * Returns the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.2">
     * <code>sub</code></a> (subject) value or {@code null} if not present.
     *
     * @return the JWT {@code sub} value or {@code null} if not present.
     */
    String getSubject();

    /**
     * {@inheritDoc}
     */
    @Override //only for better/targeted JavaDoc
    Claims setSubject(String sub);

    /**
     * Returns the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.3">
     * <code>aud</code></a> (audience) value or {@code null} if not present.
     *
     * @return the JWT {@code aud} value or {@code null} if not present.
     */
    String getAudience();

    /**
     * {@inheritDoc}
     */
    @Override //only for better/targeted JavaDoc
    Claims setAudience(String aud);

    /**
     * Returns the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.4">
     * <code>exp</code></a> (expiration) timestamp or {@code null} if not present.
     *
     * <p>A JWT obtained after this timestamp should not be used.</p>
     *
     * @return the JWT {@code exp} value or {@code null} if not present.
     */
    Date getExpiration();

    /**
     * {@inheritDoc}
     */
    @Override //only for better/targeted JavaDoc
    Claims setExpiration(Date exp);

    /**
     * Returns the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.5">
     * <code>nbf</code></a> (not before) timestamp or {@code null} if not present.
     *
     * <p>A JWT obtained before this timestamp should not be used.</p>
     *
     * @return the JWT {@code nbf} value or {@code null} if not present.
     */
    Date getNotBefore();

    /**
     * {@inheritDoc}
     */
    @Override //only for better/targeted JavaDoc
    Claims setNotBefore(Date nbf);

    /**
     * Returns the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.6">
     * <code>iat</code></a> (issued at) timestamp or {@code null} if not present.
     *
     * <p>If present, this value is the timestamp when the JWT was created.</p>
     *
     * @return the JWT {@code iat} value or {@code null} if not present.
     */
    Date getIssuedAt();

    /**
     * {@inheritDoc}
     */
    @Override //only for better/targeted JavaDoc
    Claims setIssuedAt(Date iat);

    /**
     * Returns the JWTs <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.7">
     * <code>jti</code></a> (JWT ID) value or {@code null} if not present.
     *
     * <p>This value is a CaSe-SenSiTiVe unique identifier for the JWT. If available, this value is expected to be
     * assigned in a manner that ensures that there is a negligible probability that the same value will be
     * accidentally
     * assigned to a different data object.  The ID can be used to prevent the JWT from being replayed.</p>
     *
     * @return the JWT {@code jti} value or {@code null} if not present.
     */
    String getId();

    /**
     * {@inheritDoc}
     */
    @Override //only for better/targeted JavaDoc
    Claims setId(String jti);

    /**
     * Returns the JWTs claim ({@code claimName}) value as a type {@code requiredType}, or {@code null} if not present.
     *
     * <p>JJWT only converts simple String, Date, Long, Integer, Short and Byte types automatically. Anything more
     * complex is expected to be already converted to your desired type by the JSON
     * {@link io.jsonwebtoken.io.Deserializer Deserializer} implementation. You may specify a custom Deserializer for a
     * JwtParser with the desired conversion configuration via the {@link JwtParserBuilder#deserializeJsonWith} method.
     * See <a href="https://github.com/jwtk/jjwt#custom-json-processor">custom JSON processor</a></a> for more
     * information. If using Jackson, you can specify custom claim POJO types as described in
     * <a href="https://github.com/jwtk/jjwt#json-jackson-custom-types">custom claim types</a>.
     *
     * @param claimName name of claim
     * @param requiredType the type of the value expected to be returned
     * @param <T> the type of the value expected to be returned
     * @return the JWT {@code claimName} value or {@code null} if not present.
     * @throws RequiredTypeException throw if the claim value is not null and not of type {@code requiredType}
     */
    <T> T get(String claimName, Class<T> requiredType);
}