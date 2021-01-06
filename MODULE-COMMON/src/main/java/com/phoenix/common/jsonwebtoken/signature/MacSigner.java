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

import com.phoenix.common.exception.runtime.SignatureException;
import com.phoenix.common.jsonwebtoken.crypto.MacProvider;
import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.lang.Assert;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

public class MacSigner extends MacProvider implements Signer {

    public MacSigner(SignatureAlgorithm alg, byte[] key) {
        this(alg, new SecretKeySpec(key, alg.getJcaName()));
    }

    public MacSigner(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(alg.isHmac(), "The MacSigner only supports HMAC signature algorithms.");
        if (!(key instanceof SecretKey)) {
            String msg = "MAC signatures must be computed and verified using a SecretKey.  The specified key of " +
                    "type " + key.getClass().getName() + " is not a SecretKey.";
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public byte[] sign(byte[] data) {
        Mac mac = getMacInstance();
        return mac.doFinal(data);
    }

    protected Mac getMacInstance() throws SignatureException {
        try {
            return doGetMacInstance();
        } catch (NoSuchAlgorithmException e) {
            String msg = "Unable to obtain JCA MAC algorithm '" + alg.getJcaName() + "': " + e.getMessage();
            throw new SignatureException(msg, e);
        } catch (InvalidKeyException e) {
            String msg = "The specified signing key is not a valid " + alg.name() + " key: " + e.getMessage();
            throw new SignatureException(msg, e);
        }
    }

    protected Mac doGetMacInstance() throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(alg.getJcaName());
        mac.init(key);
        return mac;
    }
}
