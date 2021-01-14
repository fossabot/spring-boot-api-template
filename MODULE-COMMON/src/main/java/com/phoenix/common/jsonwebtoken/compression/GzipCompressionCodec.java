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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Codec implementing the <a href="https://en.wikipedia.org/wiki/Gzip">gzip compression algorithm</a>.
 *
 * @since 0.6.0
 */
public class GzipCompressionCodec extends AbstractCompressionCodec implements CompressionCodec {

    private static final String GZIP = "GZIP";

    private static final StreamWrapper WRAPPER = new StreamWrapper() {
        @Override
        public OutputStream wrap(OutputStream out) throws IOException {
            return new GZIPOutputStream(out);
        }
    };

    @Override
    public String getAlgorithmName() {
        return GZIP;
    }

    @Override
    protected byte[] doCompress(byte[] payload) throws IOException {
        return writeAndClose(payload, WRAPPER);
    }

    @Override
    protected byte[] doDecompress(byte[] compressed) throws IOException {
        return readAndClose(new GZIPInputStream(new ByteArrayInputStream(compressed)));
    }
}
