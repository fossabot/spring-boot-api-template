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

package com.phoenix.common.jsonwebtoken.compression;

import com.phoenix.common.exception.runtime.CompressionException;

/**
 * Compresses and decompresses byte arrays according to a compression algorithm.
 *
 * @see CompressionCodecs#DEFLATE
 * @see CompressionCodecs#GZIP
 * @since 0.6.0
 */
public interface CompressionCodec {

    /**
     * The compression algorithm name to use as the JWT's {@code zip} header value.
     *
     * @return the compression algorithm name to use as the JWT's {@code zip} header value.
     */
    String getAlgorithmName();

    /**
     * Compresses the specified byte array according to the compression {@link #getAlgorithmName() algorithm}.
     *
     * @param payload bytes to compress
     * @return compressed bytes
     * @throws CompressionException if the specified byte array cannot be compressed according to the compression
     *                              {@link #getAlgorithmName() algorithm}.
     */
    byte[] compress(byte[] payload) throws CompressionException;

    /**
     * Decompresses the specified compressed byte array according to the compression
     * {@link #getAlgorithmName() algorithm}.  The specified byte array must already be in compressed form
     * according to the {@link #getAlgorithmName() algorithm}.
     *
     * @param compressed compressed bytes
     * @return decompressed bytes
     * @throws CompressionException if the specified byte array cannot be decompressed according to the compression
     *                              {@link #getAlgorithmName() algorithm}.
     */
    byte[] decompress(byte[] compressed) throws CompressionException;
}
