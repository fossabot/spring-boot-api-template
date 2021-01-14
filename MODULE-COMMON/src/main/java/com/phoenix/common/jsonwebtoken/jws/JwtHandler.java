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

/**
 * A JwtHandler is invoked by a {@link io.jsonwebtoken.JwtParser JwtParser} after parsing a JWT to indicate the exact
 * type of JWT or JWS parsed.
 *
 * @param <T> the type of object to return to the parser caller after handling the parsed JWT.
 * @since 0.2
 */
public interface JwtHandler<T> {

    /**
     * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
     * a plaintext JWT.  A plaintext JWT has a String (non-JSON) body payload and it is not cryptographically signed.
     *
     * @param jwt the parsed plaintext JWT
     * @return any object to be used after inspecting the JWT, or {@code null} if no return value is necessary.
     */
    T onPlaintextJwt(Jwt<Header, String> jwt);

    /**
     * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
     * a Claims JWT.  A Claims JWT has a {@link Claims} body and it is not cryptographically signed.
     *
     * @param jwt the parsed claims JWT
     * @return any object to be used after inspecting the JWT, or {@code null} if no return value is necessary.
     */
    T onClaimsJwt(Jwt<Header, Claims> jwt);

    /**
     * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
     * a plaintext JWS.  A plaintext JWS is a JWT with a String (non-JSON) body (payload) that has been
     * cryptographically signed.
     *
     * <p>This method will only be invoked if the cryptographic signature can be successfully verified.</p>
     *
     * @param jws the parsed plaintext JWS
     * @return any object to be used after inspecting the JWS, or {@code null} if no return value is necessary.
     */
    T onPlaintextJws(Jws<String> jws);

    /**
     * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
     * a valid Claims JWS.  A Claims JWS is a JWT with a {@link Claims} body that has been cryptographically signed.
     *
     * <p>This method will only be invoked if the cryptographic signature can be successfully verified.</p>
     *
     * @param jws the parsed claims JWS
     * @return any object to be used after inspecting the JWS, or {@code null} if no return value is necessary.
     */
    T onClaimsJws(Jws<Claims> jws);

}
