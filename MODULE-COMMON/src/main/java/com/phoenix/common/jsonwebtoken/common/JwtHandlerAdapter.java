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

import com.phoenix.common.exception.runtime.UnsupportedJwtException;
import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.component.Header;

/**
 * An <a href="http://en.wikipedia.org/wiki/Adapter_pattern">Adapter</a> implementation of the
 * {@link JwtHandler} interface that allows for anonymous subclasses to process only the JWT results that are
 * known/expected for a particular use case.
 *
 * <p>All of the methods in this implementation throw exceptions: overridden methods represent
 * scenarios expected by calling code in known situations.  It would be unexpected to receive a JWS or JWT that did
 * not match parsing expectations, so all non-overridden methods throw exceptions to indicate that the JWT
 * input was unexpected.</p>
 *
 * @param <T> the type of object to return to the parser caller after handling the parsed JWT.
 * @since 0.2
 */
public class JwtHandlerAdapter<T> implements JwtHandler<T> {

    @Override
    public T onPlaintextJwt(Jwt<Header, String> jwt) {
        throw new UnsupportedJwtException("Unsigned plaintext JWTs are not supported.");
    }

    @Override
    public T onClaimsJwt(Jwt<Header, Claims> jwt) {
        throw new UnsupportedJwtException("Unsigned Claims JWTs are not supported.");
    }

    @Override
    public T onPlaintextJws(Jws<String> jws) {
        throw new UnsupportedJwtException("Signed plaintext JWSs are not supported.");
    }

    @Override
    public T onClaimsJws(Jws<Claims> jws) {
        throw new UnsupportedJwtException("Signed Claims JWSs are not supported.");
    }
}
