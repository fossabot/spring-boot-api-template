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

/**
 * Provides default implementations of the {@link CompressionCodec} interface.
 *
 * @see #DEFLATE
 * @see #GZIP
 * @since 0.7.0
 */
public final class CompressionCodecs {

    private CompressionCodecs() {
    } //prevent external instantiation

    /**
     * Codec implementing the <a href="https://tools.ietf.org/html/rfc7518">JWA</a> standard
     * <a href="https://en.wikipedia.org/wiki/DEFLATE">deflate</a> compression algorithmD
     */
    public static final CompressionCodec DEFLATE = new DeflateCompressionCodec();
    //Classes.newInstance("io.jsonwebtoken.impl.compression.DeflateCompressionCodec");

    /**
     * Codec implementing the <a href="https://en.wikipedia.org/wiki/Gzip">gzip</a> compression algorithm.
     * <h3>Compatibility Warning</h3>
     * <p><b>This is not a standard JWA compression algorithm</b>.  Be sure to use this only when you are confident
     * that all parties accessing the token support the gzip algorithm.</p>
     * <p>If you're concerned about compatibility, the {@link #DEFLATE DEFLATE} code is JWA standards-compliant.</p>
     */
    public static final CompressionCodec GZIP = new GzipCompressionCodec();
    //Classes.newInstance("io.jsonwebtoken.impl.compression.GzipCompressionCodec");

}
