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

package com.phoenix.common.jsonwebtoken.validator;

import com.phoenix.common.exception.runtime.SignatureException;
import com.phoenix.common.jsonwebtoken.crypto.RsaProvider;
import com.phoenix.common.jsonwebtoken.signature.RsaSigner;
import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.lang.Assert;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaSignatureValidator extends RsaProvider implements SignatureValidator {

    private final RsaSigner SIGNER;

    public RsaSignatureValidator(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(key instanceof RSAPrivateKey || key instanceof RSAPublicKey,
                "RSA Signature validation requires either a RSAPublicKey or RSAPrivateKey instance.");
        this.SIGNER = key instanceof RSAPrivateKey ? new RsaSigner(alg, key) : null;
    }

    @Override
    public boolean isValid(byte[] data, byte[] signature) {
        if (key instanceof PublicKey) {
            Signature sig = createSignatureInstance();
            PublicKey publicKey = (PublicKey) key;
            try {
                return doVerify(sig, publicKey, data, signature);
            } catch (Exception e) {
                String msg = "Unable to verify RSA signature using configured PublicKey. " + e.getMessage();
                throw new SignatureException(msg, e);
            }
        } else {
            Assert.notNull(this.SIGNER, "RSA Signer instance cannot be null.  This is a bug.  Please report it.");
            byte[] computed = this.SIGNER.sign(data);
            return MessageDigest.isEqual(computed, signature);
        }
    }

    protected boolean doVerify(Signature sig, PublicKey publicKey, byte[] data, byte[] signature)
            throws InvalidKeyException, java.security.SignatureException {
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }

}
