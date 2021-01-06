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
import com.phoenix.common.exception.security.WeakKeyException;
import com.phoenix.common.lang.Assert;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

/**
 * Utility class for securely generating {@link SecretKey}s.
 *
 * @since 0.10.0
 */
public final class Keys {

    private static final String MAC = "io.jsonwebtoken.impl.crypto.MacProvider";

    private static final Class[] SIG_ARG_TYPES = new Class[]{SignatureAlgorithm.class};

    //purposefully ordered higher to lower:
    private static final List<SignatureAlgorithm> PREFERRED_HMAC_ALGS = List.of(SignatureAlgorithm.HS512, SignatureAlgorithm.HS384, SignatureAlgorithm.HS256);

    //prevent instantiation
    private Keys() {
    }

    /*
    public static final int bitLength(Key key) throws IllegalArgumentException {
        Assert.notNull(key, "Key cannot be null.");
        if (key instanceof SecretKey) {
            byte[] encoded = key.getEncoded();
            return Arrays.length(encoded) * 8;
        } else if (key instanceof RSAKey) {
            return ((RSAKey)key).getModulus().bitLength();
        } else if (key instanceof ECKey) {
            return ((ECKey)key).getParams().getOrder().bitLength();
        }

        throw new IllegalArgumentException("Unsupported key type: " + key.getClass().getName());
    }
    */

    /**
     * Creates a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array.
     *
     * @param bytes the key byte array
     * @return a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array.
     * @throws WeakKeyException if the key byte array length is less than 256 bits (32 bytes) as mandated by the
     *                          <a href="https://tools.ietf.org/html/rfc7518#section-3.2">JWT JWA Specification
     *                          (RFC 7518, Section 3.2)</a>
     */
    public static SecretKey hmacShaKeyFor(byte[] bytes) throws InvalidKeyException {

        if (bytes == null) {
            throw new InvalidKeyException("SecretKey byte array cannot be null.");
        }

        int bitLength = bytes.length * 8;

        for (SignatureAlgorithm alg : PREFERRED_HMAC_ALGS) {
            if (bitLength >= alg.getMinKeyLength()) {
                return new SecretKeySpec(bytes, alg.getJcaName());
            }
        }

        String msg = "The specified key byte array is " + bitLength + " bits which " +
                "is not secure enough for any JWT HMAC-SHA algorithm.  The JWT " +
                "JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a " +
                "size >= 256 bits (the key size must be greater than or equal to the hash " +
                "output size).  Consider using the " + Keys.class.getName() + "#secretKeyFor(SignatureAlgorithm) method " +
                "to create a key guaranteed to be secure enough for your preferred HMAC-SHA algorithm.  See " +
                "https://tools.ietf.org/html/rfc7518#section-3.2 for more information.";
        throw new WeakKeyException(msg);
    }

    /**
     * Returns a new {@link SecretKey} with a key length suitable for use with the specified {@link SignatureAlgorithm}.
     *
     * <p><a href="https://tools.ietf.org/html/rfc7518#section-3.2">JWA Specification (RFC 7518), Section 3.2</a>
     * requires minimum key lengths to be used for each respective Signature Algorithm.  This method returns a
     * secure-random generated SecretKey that adheres to the required minimum key length.  The lengths are:</p>
     *
     * <table>
     * <tr>
     * <th>Algorithm</th>
     * <th>Key Length</th>
     * </tr>
     * <tr>
     * <td>HS256</td>
     * <td>256 bits (32 bytes)</td>
     * </tr>
     * <tr>
     * <td>HS384</td>
     * <td>384 bits (48 bytes)</td>
     * </tr>
     * <tr>
     * <td>HS512</td>
     * <td>512 bits (64 bytes)</td>
     * </tr>
     * </table>
     *
     * @param alg the {@code SignatureAlgorithm} to inspect to determine which key length to use.
     * @return a new {@link SecretKey} instance suitable for use with the specified {@link SignatureAlgorithm}.
     * @throws IllegalArgumentException for any input value other than {@link SignatureAlgorithm#HS256},
     *                                  {@link SignatureAlgorithm#HS384}, or {@link SignatureAlgorithm#HS512}
     */
    public static SecretKey secretKeyFor(SignatureAlgorithm alg) throws IllegalArgumentException {
        Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
        switch (alg) {
            case HS256:
            case HS384:
            case HS512:
                return Classes.invokeStatic(MAC, "generateKey", SIG_ARG_TYPES, alg);
            default:
                String msg = "The " + alg.name() + " algorithm does not support shared secret keys.";
                throw new IllegalArgumentException(msg);
        }
    }

}
