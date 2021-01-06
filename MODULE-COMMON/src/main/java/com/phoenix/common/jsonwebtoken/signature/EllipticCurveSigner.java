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

import com.phoenix.common.exception.runtime.JwtException;
import com.phoenix.common.exception.runtime.SignatureException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.ECKey;

public class EllipticCurveSigner extends EllipticCurveProvider implements Signer {

    public EllipticCurveSigner(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        if (!(key instanceof PrivateKey && key instanceof ECKey)) {
            String msg = "Elliptic Curve signatures must be computed using an EC PrivateKey.  The specified key of " +
                    "type " + key.getClass().getName() + " is not an EC PrivateKey.";
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public byte[] sign(byte[] data) {
        try {
            return doSign(data);
        } catch (InvalidKeyException e) {
            throw new SignatureException("Invalid Elliptic Curve PrivateKey. " + e.getMessage(), e);
        } catch (java.security.SignatureException e) {
            throw new SignatureException("Unable to calculate signature using Elliptic Curve PrivateKey. " + e.getMessage(), e);
        } catch (JwtException e) {
            throw new SignatureException("Unable to convert signature to JOSE format. " + e.getMessage(), e);
        }
    }

    protected byte[] doSign(byte[] data) throws InvalidKeyException, java.security.SignatureException, JwtException {
        PrivateKey privateKey = (PrivateKey)key;
        Signature sig = createSignatureInstance();
        sig.initSign(privateKey);
        sig.update(data);
        return transcodeSignatureToConcat(sig.sign(), getSignatureByteArrayLength(alg));
    }
}

