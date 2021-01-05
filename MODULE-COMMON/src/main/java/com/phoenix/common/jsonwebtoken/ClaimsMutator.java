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

import java.util.Date;

/**
 * Mutation (modifications) to a {@link io.jsonwebtoken.Claims Claims} instance.
 *
 * @param <T> the type of mutator
 * @see io.jsonwebtoken.JwtBuilder
 * @see io.jsonwebtoken.Claims
 * @since 0.2
 */
public interface ClaimsMutator<T extends ClaimsMutator> {

    /**
     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.1">
     * <code>iss</code></a> (issuer) value.  A {@code null} value will remove the property from the JSON map.
     *
     * @param iss the JWT {@code iss} value or {@code null} to remove the property from the JSON map.
     * @return the {@code Claims} instance for method chaining.
     */
    T setIssuer(String iss);

    /**
     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.2">
     * <code>sub</code></a> (subject) value.  A {@code null} value will remove the property from the JSON map.
     *
     * @param sub the JWT {@code sub} value or {@code null} to remove the property from the JSON map.
     * @return the {@code Claims} instance for method chaining.
     */
    T setSubject(String sub);

    /**
     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.3">
     * <code>aud</code></a> (audience) value.  A {@code null} value will remove the property from the JSON map.
     *
     * @param aud the JWT {@code aud} value or {@code null} to remove the property from the JSON map.
     * @return the {@code Claims} instance for method chaining.
     */
    T setAudience(String aud);

    /**
     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.4">
     * <code>exp</code></a> (expiration) timestamp.  A {@code null} value will remove the property from the JSON map.
     *
     * <p>A JWT obtained after this timestamp should not be used.</p>
     *
     * @param exp the JWT {@code exp} value or {@code null} to remove the property from the JSON map.
     * @return the {@code Claims} instance for method chaining.
     */
    T setExpiration(Date exp);

    /**
     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.5">
     * <code>nbf</code></a> (not before) timestamp.  A {@code null} value will remove the property from the JSON map.
     *
     * <p>A JWT obtained before this timestamp should not be used.</p>
     *
     * @param nbf the JWT {@code nbf} value or {@code null} to remove the property from the JSON map.
     * @return the {@code Claims} instance for method chaining.
     */
    T setNotBefore(Date nbf);

    /**
     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.6">
     * <code>iat</code></a> (issued at) timestamp.  A {@code null} value will remove the property from the JSON map.
     *
     * <p>The value is the timestamp when the JWT was created.</p>
     *
     * @param iat the JWT {@code iat} value or {@code null} to remove the property from the JSON map.
     * @return the {@code Claims} instance for method chaining.
     */
    T setIssuedAt(Date iat);

    /**
     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.7">
     * <code>jti</code></a> (JWT ID) value.  A {@code null} value will remove the property from the JSON map.
     *
     * <p>This value is a CaSe-SenSiTiVe unique identifier for the JWT. If specified, this value MUST be assigned in a
     * manner that ensures that there is a negligible probability that the same value will be accidentally
     * assigned to a different data object.  The ID can be used to prevent the JWT from being replayed.</p>
     *
     * @param jti the JWT {@code jti} value or {@code null} to remove the property from the JSON map.
     * @return the {@code Claims} instance for method chaining.
     */
    T setId(String jti);
}

