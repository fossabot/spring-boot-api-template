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

import com.phoenix.common.exception.runtime.ExpiredJwtException;
import com.phoenix.common.exception.runtime.MalformedJwtException;
import com.phoenix.common.exception.runtime.SignatureException;
import com.phoenix.common.exception.runtime.UnsupportedJwtException;
import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.component.Header;

/**
 * A parser for reading JWT strings, used to convert them into a {@link Jwt} object representing the expanded JWT.
 *
 * @since 0.1
 */
public interface JwtParser {

    public static final char SEPARATOR_CHAR = '.';


    /**
     * Returns {@code true} if the specified JWT compact string represents a signed JWT (aka a 'JWS'), {@code false}
     * otherwise.
     * <p>
     * <p>Note that if you are reasonably sure that the token is signed, it is more efficient to attempt to
     * parse the token (and catching exceptions if necessary) instead of calling this method first before parsing.</p>
     *
     * @param jwt the compact serialized JWT to check
     * @return {@code true} if the specified JWT compact string represents a signed JWT (aka a 'JWS'), {@code false}
     * otherwise.
     */
    boolean isSigned(String jwt);

    /**
     * Parses the specified compact serialized JWT string based on the builder's current configuration state and
     * returns the resulting JWT or JWS instance.
     * <p>
     * <p>This method returns a JWT or JWS based on the parsed string.  Because it may be cumbersome to determine if it
     * is a JWT or JWS, or if the body/payload is a Claims or String with {@code instanceof} checks, the
     * {@link #parse(String, JwtHandler) parse(String,JwtHandler)} method allows for a type-safe callback approach that
     * may help reduce code or instanceof checks.</p>
     *
     * @param jwt the compact serialized JWT to parse
     * @return the specified compact serialized JWT string based on the builder's current configuration state.
     * @throws MalformedJwtException    if the specified JWT was incorrectly constructed (and therefore invalid).
     *                                  Invalid
     *                                  JWTs should not be trusted and should be discarded.
     * @throws SignatureException       if a JWS signature was discovered, but could not be verified.  JWTs that fail
     *                                  signature validation should not be trusted and should be discarded.
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the specified string is {@code null} or empty or only whitespace.
     * @see #parse(String, JwtHandler)
     * @see #parsePlaintextJwt(String)
     * @see #parseClaimsJwt(String)
     * @see #parsePlaintextJws(String)
     * @see #parseClaimsJws(String)
     */
    Jwt parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    /**
     * Parses the specified compact serialized JWT string based on the builder's current configuration state and
     * invokes the specified {@code handler} with the resulting JWT or JWS instance.
     * <p>
     * <p>If you are confident of the format of the JWT before parsing, you can create an anonymous subclass using the
     * {@link JwtHandlerAdapter JwtHandlerAdapter} and override only the methods you know are relevant
     * for your use case(s), for example:</p>
     * <p>
     * <pre>
     * String compactJwt = request.getParameter("jwt"); //we are confident this is a signed JWS
     *
     * String subject = Jwts.parser().setSigningKey(key).parse(compactJwt, new JwtHandlerAdapter&lt;String&gt;() {
     *     &#64;Override
     *     public String onClaimsJws(Jws&lt;Claims&gt; jws) {
     *         return jws.getBody().getSubject();
     *     }
     * });
     * </pre>
     * <p>
     * <p>If you know the JWT string can be only one type of JWT, then it is even easier to invoke one of the
     * following convenience methods instead of this one:</p>
     * <p>
     * <ul>
     * <li>{@link #parsePlaintextJwt(String)}</li>
     * <li>{@link #parseClaimsJwt(String)}</li>
     * <li>{@link #parsePlaintextJws(String)}</li>
     * <li>{@link #parseClaimsJws(String)}</li>
     * </ul>
     *
     * @param jwt the compact serialized JWT to parse
     * @return the result returned by the {@code JwtHandler}
     * @throws MalformedJwtException    if the specified JWT was incorrectly constructed (and therefore invalid).
     *                                  Invalid JWTs should not be trusted and should be discarded.
     * @throws SignatureException       if a JWS signature was discovered, but could not be verified.  JWTs that fail
     *                                  signature validation should not be trusted and should be discarded.
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the specified string is {@code null} or empty or only whitespace, or if the
     *                                  {@code handler} is {@code null}.
     * @see #parsePlaintextJwt(String)
     * @see #parseClaimsJwt(String)
     * @see #parsePlaintextJws(String)
     * @see #parseClaimsJws(String)
     * @see #parse(String)
     * @since 0.2
     */
    <T> T parse(String jwt, JwtHandler<T> handler)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    /**
     * Parses the specified compact serialized JWT string based on the builder's current configuration state and
     * returns
     * the resulting unsigned plaintext JWT instance.
     * <p>
     * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects an
     * unsigned plaintext JWT. An unsigned plaintext JWT has a String (non-JSON) body payload and it is not
     * cryptographically signed.</p>
     * <p>
     * <p><b>If the compact string presented does not reflect an unsigned plaintext JWT with non-JSON string body,
     * an {@link UnsupportedJwtException} will be thrown.</b></p>
     *
     * @param plaintextJwt a compact serialized unsigned plaintext JWT string.
     * @return the {@link Jwt Jwt} instance that reflects the specified compact JWT string.
     * @throws UnsupportedJwtException  if the {@code plaintextJwt} argument does not represent an unsigned plaintext
     *                                  JWT
     * @throws MalformedJwtException    if the {@code plaintextJwt} string is not a valid JWT
     * @throws SignatureException       if the {@code plaintextJwt} string is actually a JWS and signature validation
     *                                  fails
     * @throws IllegalArgumentException if the {@code plaintextJwt} string is {@code null} or empty or only whitespace
     * @see #parseClaimsJwt(String)
     * @see #parsePlaintextJws(String)
     * @see #parseClaimsJws(String)
     * @see #parse(String, JwtHandler)
     * @see #parse(String)
     * @since 0.2
     */
    Jwt<Header, String> parsePlaintextJwt(String plaintextJwt)
            throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    /**
     * Parses the specified compact serialized JWT string based on the builder's current configuration state and
     * returns
     * the resulting unsigned plaintext JWT instance.
     * <p>
     * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects an
     * unsigned Claims JWT. An unsigned Claims JWT has a {@link Claims} body and it is not cryptographically
     * signed.</p>
     * <p>
     * <p><b>If the compact string presented does not reflect an unsigned Claims JWT, an
     * {@link UnsupportedJwtException} will be thrown.</b></p>
     *
     * @param claimsJwt a compact serialized unsigned Claims JWT string.
     * @return the {@link Jwt Jwt} instance that reflects the specified compact JWT string.
     * @throws UnsupportedJwtException  if the {@code claimsJwt} argument does not represent an unsigned Claims JWT
     * @throws MalformedJwtException    if the {@code claimsJwt} string is not a valid JWT
     * @throws SignatureException       if the {@code claimsJwt} string is actually a JWS and signature validation
     *                                  fails
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the {@code claimsJwt} string is {@code null} or empty or only whitespace
     * @see #parsePlaintextJwt(String)
     * @see #parsePlaintextJws(String)
     * @see #parseClaimsJws(String)
     * @see #parse(String, JwtHandler)
     * @see #parse(String)
     * @since 0.2
     */
    Jwt<Header, Claims> parseClaimsJwt(String claimsJwt)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    /**
     * Parses the specified compact serialized JWS string based on the builder's current configuration state and
     * returns
     * the resulting plaintext JWS instance.
     * <p>
     * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects a
     * plaintext JWS. A plaintext JWS is a JWT with a String (non-JSON) body (payload) that has been
     * cryptographically signed.</p>
     * <p>
     * <p><b>If the compact string presented does not reflect a plaintext JWS, an {@link UnsupportedJwtException}
     * will be thrown.</b></p>
     *
     * @param plaintextJws a compact serialized JWS string.
     * @return the {@link Jws Jws} instance that reflects the specified compact JWS string.
     * @throws UnsupportedJwtException  if the {@code plaintextJws} argument does not represent an plaintext JWS
     * @throws MalformedJwtException    if the {@code plaintextJws} string is not a valid JWS
     * @throws SignatureException       if the {@code plaintextJws} JWS signature validation fails
     * @throws IllegalArgumentException if the {@code plaintextJws} string is {@code null} or empty or only whitespace
     * @see #parsePlaintextJwt(String)
     * @see #parseClaimsJwt(String)
     * @see #parseClaimsJws(String)
     * @see #parse(String, JwtHandler)
     * @see #parse(String)
     * @since 0.2
     */
    Jws<String> parsePlaintextJws(String plaintextJws)
            throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    /**
     * Parses the specified compact serialized JWS string based on the builder's current configuration state and
     * returns
     * the resulting Claims JWS instance.
     * <p>
     * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects a
     * Claims JWS. A Claims JWS is a JWT with a {@link Claims} body that has been cryptographically signed.</p>
     * <p>
     * <p><b>If the compact string presented does not reflect a Claims JWS, an {@link UnsupportedJwtException} will be
     * thrown.</b></p>
     *
     * @param claimsJws a compact serialized Claims JWS string.
     * @return the {@link Jws Jws} instance that reflects the specified compact Claims JWS string.
     * @throws UnsupportedJwtException  if the {@code claimsJws} argument does not represent an Claims JWS
     * @throws MalformedJwtException    if the {@code claimsJws} string is not a valid JWS
     * @throws SignatureException       if the {@code claimsJws} JWS signature validation fails
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the {@code claimsJws} string is {@code null} or empty or only whitespace
     * @see #parsePlaintextJwt(String)
     * @see #parseClaimsJwt(String)
     * @see #parsePlaintextJws(String)
     * @see #parse(String, JwtHandler)
     * @see #parse(String)
     * @since 0.2
     */
    Jws<Claims> parseClaimsJws(String claimsJws)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
}
