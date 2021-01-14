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

import com.phoenix.common.exception.runtime.SignatureException;
import com.phoenix.common.lang.Assert;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;

abstract class SignatureProvider {//NOPMD

    /**
     * JJWT's default SecureRandom number generator.  This RNG is initialized using the JVM default as follows:
     *
     * <pre><code>
     * static {
     *     DEFAULT_SECURE_RANDOM = new SecureRandom();
     *     DEFAULT_SECURE_RANDOM.nextBytes(new byte[64]);
     * }
     * </code></pre>
     *
     * <p><code>nextBytes</code> is called to force the RNG to initialize itself if not already initialized.  The
     * byte array is not used and discarded immediately for garbage collection.</p>
     */
    public static final SecureRandom DEFAULT_SECURE_RANDOM;

    static {
        DEFAULT_SECURE_RANDOM = new SecureRandom();
        DEFAULT_SECURE_RANDOM.nextBytes(new byte[64]);
    }

    protected final SignatureAlgorithm alg;
    protected final Key key;

    protected SignatureProvider(SignatureAlgorithm alg, Key key) {
        Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
        Assert.notNull(key, "Key cannot be null.");
        this.alg = alg;
        this.key = key;
    }

    protected Signature createSignatureInstance() {
        try {
            return getSignatureInstance();
        } catch (NoSuchAlgorithmException e) {
            String msg = "Unavailable " + alg.getFamilyName() + " Signature algorithm '" + alg.getJcaName() + "'.";
            if (!alg.isJdkStandard() && !isBouncyCastleAvailable()) {
                msg += " This is not a standard JDK algorithm. Try including BouncyCastle in the runtime classpath.";
            }
            throw new SignatureException(msg, e);
        }
    }

    protected Signature getSignatureInstance() throws NoSuchAlgorithmException {
        return Signature.getInstance(alg.getJcaName());
    }

    protected boolean isBouncyCastleAvailable() {
//        return RuntimeEnvironment.BOUNCY_CASTLE_AVAILABLE;
        return false;
    }
}
