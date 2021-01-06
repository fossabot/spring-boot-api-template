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

package com.phoenix.common.jsonwebtoken.signature;

import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.component.JwsHeader;

import java.security.Key;

/**
 * A {@code SigningKeyResolver} can be used by a {@link io.jsonwebtoken.JwtParser JwtParser} to find a signing key that
 * should be used to verify a JWS signature.
 *
 * <p>A {@code SigningKeyResolver} is necessary when the signing key is not already known before parsing the JWT and the
 * JWT header or payload (plaintext body or Claims) must be inspected first to determine how to look up the signing key.
 * Once returned by the resolver, the JwtParser will then verify the JWS signature with the returned key.  For
 * example:</p>
 *
 * <pre>
 * Jws&lt;Claims&gt; jws = Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter() {
 *         &#64;Override
 *         public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
 *             //inspect the header or claims, lookup and return the signing key
 *             return getSigningKeyBytes(header, claims); //implement me
 *         }})
 *     .parseClaimsJws(compact);
 * </pre>
 *
 * <p>A {@code SigningKeyResolver} is invoked once during parsing before the signature is verified.</p>
 *
 * <h3>SigningKeyResolverAdapter</h3>
 *
 * <p>If you only need to resolve a signing key for a particular JWS (either a plaintext or Claims JWS), consider using
 * the {@link io.jsonwebtoken.SigningKeyResolverAdapter} and overriding only the method you need to support instead of
 * implementing this interface directly.</p>
 *
 * @see io.jsonwebtoken.SigningKeyResolverAdapter
 * @since 0.4
 */
public interface SigningKeyResolver {

    /**
     * Returns the signing key that should be used to validate a digital signature for the Claims JWS with the specified
     * header and claims.
     *
     * @param header the header of the JWS to validate
     * @param claims the claims (body) of the JWS to validate
     * @return the signing key that should be used to validate a digital signature for the Claims JWS with the specified
     * header and claims.
     */
    Key resolveSigningKey(JwsHeader header, Claims claims);

    /**
     * Returns the signing key that should be used to validate a digital signature for the Plaintext JWS with the
     * specified header and plaintext payload.
     *
     * @param header    the header of the JWS to validate
     * @param plaintext the plaintext body of the JWS to validate
     * @return the signing key that should be used to validate a digital signature for the Plaintext JWS with the
     * specified header and plaintext payload.
     */
    Key resolveSigningKey(JwsHeader header, String plaintext);
}
