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
import com.phoenix.common.lang.Assert;
import com.phoenix.common.lang.Objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract class that asserts arguments and wraps IOException with CompressionException.
 *
 * @since 0.6.0
 */
public abstract class AbstractCompressionCodec implements CompressionCodec {

    //package-protected for a point release.  This can be made protected on a minor release (0.11.0, 0.12.0, 1.0, etc).
    protected interface StreamWrapper {
        OutputStream wrap(OutputStream out) throws IOException;
    }

    //package-protected for a point release.  This can be made protected on a minor release (0.11.0, 0.12.0, 1.0, etc).
    protected byte[] readAndClose(InputStream input) throws IOException {
        byte[] buffer = new byte[512];
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffer.length);
        int read;
        try {
            read = input.read(buffer); //assignment separate from loop invariant check for code coverage checks
            while (read != -1) {
                out.write(buffer, 0, read);
                read = input.read(buffer);
            }
        } finally {
            Objects.nullSafeClose(input);
        }
        return out.toByteArray();
    }

    //package-protected for a point release.  This can be made protected on a minor release (0.11.0, 0.12.0, 1.0, etc).
    protected byte[] writeAndClose(byte[] payload, StreamWrapper wrapper) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        OutputStream compressionStream = wrapper.wrap(outputStream);
        try {
            compressionStream.write(payload);
            compressionStream.flush();
        } finally {
            Objects.nullSafeClose(compressionStream);
        }
        return outputStream.toByteArray();
    }

    /**
     * Implement this method to do the actual work of compressing the payload
     *
     * @param payload the bytes to compress
     * @return the compressed bytes
     * @throws IOException if the compression causes an IOException
     */
    protected abstract byte[] doCompress(byte[] payload) throws IOException;

    /**
     * Asserts that payload is not null and calls {@link #doCompress(byte[]) doCompress}
     *
     * @param payload bytes to compress
     * @return compressed bytes
     * @throws CompressionException if {@link #doCompress(byte[]) doCompress} throws an IOException
     */
    @Override
    public final byte[] compress(byte[] payload) {
        Assert.notNull(payload, "payload cannot be null.");

        try {
            return doCompress(payload);
        } catch (IOException e) {
            throw new CompressionException("Unable to compress payload.", e);
        }
    }

    /**
     * Asserts the compressed bytes is not null and calls {@link #doDecompress(byte[]) doDecompress}
     *
     * @param compressed compressed bytes
     * @return decompressed bytes
     * @throws CompressionException if {@link #doDecompress(byte[]) doDecompress} throws an IOException
     */
    @Override
    public final byte[] decompress(byte[] compressed) {
        Assert.notNull(compressed, "compressed bytes cannot be null.");

        try {
            return doDecompress(compressed);
        } catch (IOException e) {
            throw new CompressionException("Unable to decompress bytes.", e);
        }
    }

    /**
     * Implement this method to do the actual work of decompressing the compressed bytes.
     *
     * @param compressed compressed bytes
     * @return decompressed bytes
     * @throws IOException if the decompression runs into an IO problem
     */
    protected abstract byte[] doDecompress(byte[] compressed) throws IOException;
}
