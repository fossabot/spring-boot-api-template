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

package com.phoenix.common.jsonwebtoken.crypto;

import com.phoenix.common.lang.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public abstract class MacProvider extends SignatureProvider {

    protected MacProvider(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(alg.isHmac(), "SignatureAlgorithm must be a HMAC SHA algorithm.");
    }

    /**
     * Generates a new secure-random 512 bit secret key suitable for creating and verifying HMAC-SHA signatures. This
     * is a convenience method that immediately delegates to {@link #generateKey(SignatureAlgorithm)} using {@link
     * SignatureAlgorithm#HS512} as the method argument.
     *
     * @return a new secure-random 512 bit secret key suitable for creating and verifying HMAC-SHA signatures.
     * @see #generateKey(SignatureAlgorithm)
     * @see #generateKey(SignatureAlgorithm, SecureRandom)
     * @since 0.5
     */
    public static SecretKey generateKey() {
        return generateKey(SignatureAlgorithm.HS512);
    }

    /**
     * Generates a new secure-random secret key of a length suitable for creating and verifying HMAC signatures
     * according to the specified {@code SignatureAlgorithm} using JJWT's default {@link
     * SignatureProvider#DEFAULT_SECURE_RANDOM SecureRandom instance}.  This is a convenience method that immediately
     * delegates to {@link #generateKey(SignatureAlgorithm, SecureRandom)}.
     *
     * @param alg the desired signature algorithm
     * @return a new secure-random secret key of a length suitable for creating and verifying HMAC signatures according
     * to the specified {@code SignatureAlgorithm} using JJWT's default {@link SignatureProvider#DEFAULT_SECURE_RANDOM
     * SecureRandom instance}.
     * @see #generateKey()
     * @see #generateKey(SignatureAlgorithm, SecureRandom)
     * @since 0.5
     */
    public static SecretKey generateKey(SignatureAlgorithm alg) {
        return generateKey(alg, DEFAULT_SECURE_RANDOM);


    }

    /**
     * Generates a new secure-random secret key of a length suitable for creating and verifying HMAC signatures
     * according to the specified {@code SignatureAlgorithm} using the specified SecureRandom number generator.  This
     * implementation returns secure-random key sizes as follows:
     *
     * <table> <caption>Key Sizes</caption> <thead> <tr> <th>Signature Algorithm</th> <th>Generated Key Size</th> </tr> </thead> <tbody> <tr>
     * <td>HS256</td> <td>256 bits (32 bytes)</td> </tr> <tr> <td>HS384</td> <td>384 bits (48 bytes)</td> </tr> <tr>
     * <td>HS512</td> <td>512 bits (64 bytes)</td> </tr> </tbody> </table>
     *
     * @param alg    the signature algorithm that will be used with the generated key
     * @param random the secure random number generator used during key generation
     * @return a new secure-random secret key of a length suitable for creating and verifying HMAC signatures according
     * to the specified {@code SignatureAlgorithm} using the specified SecureRandom number generator.
     * @see #generateKey()
     * @see #generateKey(SignatureAlgorithm)
     * @since 0.5
     */
    private static SecretKey generateKey(SignatureAlgorithm alg, SecureRandom random) {

        Assert.isTrue(alg.isHmac(), "SignatureAlgorithm argument must represent an HMAC algorithm.");

        KeyGenerator gen;

        try {
            gen = KeyGenerator.getInstance(alg.getJcaName());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("The " + alg.getJcaName() + " algorithm is not available.  " +
                    "This should never happen on JDK 7 or later - please report this to the JJWT developers.", e);
        }

        return gen.generateKey();
    }
}
