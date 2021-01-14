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

import com.phoenix.common.exception.security.InvalidKeyException;
import com.phoenix.common.jsonwebtoken.component.Claims;

import java.util.Date;
import java.util.Map;

public interface TokenProvider {
    public String  generateToken();

    public String generateTokenFromClaims(Map claims) throws InvalidKeyException, java.security.InvalidKeyException;

    public String generateTokenFromClaims(Map claims, long expiration) throws InvalidKeyException, java.security.InvalidKeyException;

    /**
     * Returns Claims encapsulated within the token
     */
    public Claims getClaimsFromToken(String token);

    /**
     * Validates if a token satisfies the following properties
     * - Signature is not malformed
     * - Token hasn't expired
     * - Token is supported
     * - Token has not recently been logged out.
     */
    public boolean validateToken(String token);

    public String getSubjectFromToken(String token);

    /**
     * Return the jwt expiration for the client so that they can execute
     * the refresh token logic appropriately
     */
    public long getExpiryDuration();

    /**
     * Returns the token expiration date encapsulated within the token
     */
    public Date getTokenExpiryFromToken(String token);

}
