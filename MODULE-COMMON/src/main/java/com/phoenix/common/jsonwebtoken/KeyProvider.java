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

import com.phoenix.common.jsonwebtoken.crypto.Keys;
import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.util.Base64;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public final class KeyProvider {
    private final KeyWrapper keyWrapper;

    /**
     * Private constructor.
     */
    private KeyProvider() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        String secretString = Base64.encodeBytes(key.getEncoded());
        String keyId = Keys.createKeyId(key);

        keyWrapper = new KeyWrapper(secretString, key, keyId);
    }

    public KeyWrapper getKeyWrapper() {
        return keyWrapper;
    }



    /**
     * Singleton instance.
     *
     * @return Singleton instance
     */
    public static KeyProvider getInstance() {
        return KeyProviderHelper.INSTANCE;
    }

    /**
     * Provides the lazy-loaded Singleton instance.
     */
    private static class KeyProviderHelper {
        private static final KeyProvider INSTANCE = new KeyProvider();
    }
}
