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

package com.phoenix.common.jsonwebtoken.component;

import com.phoenix.common.jsonwebtoken.common.JwtMap;
import com.phoenix.common.lang.Strings;

import java.util.Map;

public class DefaultHeader<T extends Header<T>> extends JwtMap implements Header<T> {

    public DefaultHeader() {
        super();
    }

    public DefaultHeader(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getType() {
        return getString(TYPE);
    }

    @Override
    public T setType(String typ) {
        setValue(TYPE, typ);
        return (T)this;
    }

    @Override
    public String getContentType() {
        return getString(CONTENT_TYPE);
    }

    @Override
    public T setContentType(String cty) {
        setValue(CONTENT_TYPE, cty);
        return (T)this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getCompressionAlgorithm() {
        String alg = getString(COMPRESSION_ALGORITHM);
        if (!Strings.hasText(alg)) {
            alg = getString(DEPRECATED_COMPRESSION_ALGORITHM);
        }
        return alg;
    }

    @Override
    public T setCompressionAlgorithm(String compressionAlgorithm) {
        setValue(COMPRESSION_ALGORITHM, compressionAlgorithm);
        return (T) this;
    }

}
