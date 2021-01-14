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

import com.phoenix.common.lang.Objects;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;

/**
 * Codec implementing the <a href="https://en.wikipedia.org/wiki/DEFLATE">deflate compression algorithm</a>.
 *
 * @since 0.6.0
 */
public class DeflateCompressionCodec extends AbstractCompressionCodec {

    private static final String DEFLATE = "DEF";

    private static final StreamWrapper WRAPPER = new StreamWrapper() {
        @Override
        public OutputStream wrap(OutputStream out) {
            return new DeflaterOutputStream(out);
        }
    };

    @Override
    public String getAlgorithmName() {
        return DEFLATE;
    }

    @Override
    protected byte[] doCompress(byte[] payload) throws IOException {
        return writeAndClose(payload, WRAPPER);
    }

    @Override
    protected byte[] doDecompress(final byte[] compressed) throws IOException {
        try {
            return readAndClose(new InflaterInputStream(new ByteArrayInputStream(compressed)));
        } catch (IOException e1) {
            try {
                return doDecompressBackCompat(compressed);
            } catch (IOException e2) {
                throw e1; //retain/report original exception
            }
        }
    }

    /**
     * This implementation was in 0.10.6 and earlier - it will be used as a fallback for backwards compatibility if
     * {@link #readAndClose(InputStream)} fails per <a href="https://github.com/jwtk/jjwt/issues/536">Issue 536</a>.
     *
     * @param compressed the compressed byte array
     * @return decompressed bytes
     * @throws IOException if unable to decompress using the 0.10.6 and earlier logic
     * @since 0.10.8
     */
    // package protected on purpose
    byte[] doDecompressBackCompat(byte[] compressed) throws IOException {
        InflaterOutputStream inflaterOutputStream = null;
        ByteArrayOutputStream decompressedOutputStream = null;

        try {
            decompressedOutputStream = new ByteArrayOutputStream();
            inflaterOutputStream = new InflaterOutputStream(decompressedOutputStream);
            inflaterOutputStream.write(compressed);
            inflaterOutputStream.flush();
            return decompressedOutputStream.toByteArray();
        } finally {
            Objects.nullSafeClose(decompressedOutputStream, inflaterOutputStream);
        }
    }
}