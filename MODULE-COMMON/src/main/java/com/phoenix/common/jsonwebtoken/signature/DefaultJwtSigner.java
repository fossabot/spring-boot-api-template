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

import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.lang.Assert;
import com.phoenix.common.util.Base64Url;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class DefaultJwtSigner implements JwtSigner {

    private static final Charset US_ASCII = StandardCharsets.US_ASCII;

    private final Signer signer;

    public DefaultJwtSigner(SignatureAlgorithm alg, Key key) {
        this(DefaultSignerFactory.INSTANCE, alg, key);
    }


    public DefaultJwtSigner(SignerFactory factory, SignatureAlgorithm alg, Key key) {
        Assert.notNull(factory, "SignerFactory argument cannot be null.");
        this.signer = factory.createSigner(alg, key);
    }

    @Override
    public String sign(String jwtWithoutSignature) {

        byte[] bytesToSign = jwtWithoutSignature.getBytes(US_ASCII);

        byte[] signature = signer.sign(bytesToSign);

        return Base64Url.encode(signature);
    }
}
