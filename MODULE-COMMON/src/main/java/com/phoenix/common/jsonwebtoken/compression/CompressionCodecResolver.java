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

package com.phoenix.common.jsonwebtoken.compression;

import com.phoenix.common.exception.runtime.CompressionException;
import com.phoenix.common.jsonwebtoken.component.Header;

/**
 * Looks for a JWT {@code zip} header, and if found, returns the corresponding {@link CompressionCodec} the parser
 * can use to decompress the JWT body.
 *
 * <p>JJWT's default {@link JwtParser} implementation supports both the
 * {@link CompressionCodecs#DEFLATE DEFLATE}
 * and {@link CompressionCodecs#GZIP GZIP} algorithms by default - you do not need to
 * specify a {@code CompressionCodecResolver} in these cases.</p>
 *
 * <p>However, if you want to use a compression algorithm other than {@code DEF} or {@code GZIP}, you must implement
 * your own {@link CompressionCodecResolver} and specify that when
 * {@link io.jsonwebtoken.JwtBuilder#compressWith(CompressionCodec) building} and
 * {@link io.jsonwebtoken.JwtParser#setCompressionCodecResolver(CompressionCodecResolver) parsing} JWTs.</p>
 *
 * @since 0.6.0
 */
public interface CompressionCodecResolver {

    /**
     * Looks for a JWT {@code zip} header, and if found, returns the corresponding {@link CompressionCodec} the parser
     * can use to decompress the JWT body.
     *
     * @param header of the JWT
     * @return CompressionCodec matching the {@code zip} header, or null if there is no {@code zip} header.
     * @throws CompressionException if a {@code zip} header value is found and not supported.
     */
    CompressionCodec resolveCompressionCodec(Header header) throws CompressionException;

}
