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
import com.phoenix.common.jsonwebtoken.component.Header;
import com.phoenix.common.lang.Assert;
import com.phoenix.common.lang.Services;
import com.phoenix.common.lang.Strings;

import java.util.*;

/**
 * Default implementation of {@link CompressionCodecResolver} that supports the following:
 * <p>
 * <ul>
 * <li>If the specified JWT {@link Header} does not have a {@code zip} header, this implementation does
 * nothing and returns {@code null} to the caller, indicating no compression was used.</li>
 * <li>If the header has a {@code zip} value of {@code DEF}, a {@link DeflateCompressionCodec} will be returned.</li>
 * <li>If the header has a {@code zip} value of {@code GZIP}, a {@link GzipCompressionCodec} will be returned.</li>
 * <li>If the header has any other {@code zip} value, a {@link CompressionException} is thrown to reflect an
 * unrecognized algorithm.</li>
 * </ul>
 *
 * <p>If you want to use a compression algorithm other than {@code DEF} or {@code GZIP}, you must implement your own
 * {@link CompressionCodecResolver} and specify that when
 * {@link io.jsonwebtoken.JwtBuilder#compressWith(CompressionCodec) building} and
 * {@link io.jsonwebtoken.JwtParser#setCompressionCodecResolver(CompressionCodecResolver) parsing} JWTs.</p>
 *
 * @see DeflateCompressionCodec
 * @see GzipCompressionCodec
 * @since 0.6.0
 */
public class DefaultCompressionCodecResolver implements CompressionCodecResolver {

    private static final String MISSING_COMPRESSION_MESSAGE = "Unable to find an implementation for compression algorithm [%s] using java.util.ServiceLoader. Ensure you include a backing implementation .jar in the classpath, for example jjwt-impl.jar, or your own .jar for custom implementations.";

    private final Map<String, CompressionCodec> codecs;

    public DefaultCompressionCodecResolver() {
        //        for (CompressionCodec codec : Services.loadAll(CompressionCodec.class)) {
//            codecMap.put(codec.getAlgorithmName().toUpperCase(), codec);
//        }

        codecs = Map.of(
                CompressionCodecs.DEFLATE.getAlgorithmName().toUpperCase(), CompressionCodecs.DEFLATE,
                CompressionCodecs.GZIP.getAlgorithmName().toUpperCase(), CompressionCodecs.GZIP);
    }

    @Override
    public CompressionCodec resolveCompressionCodec(Header header) {
        String cmpAlg = getAlgorithmFromHeader(header);

        final boolean hasCompressionAlgorithm = Strings.hasText(cmpAlg);

        if (!hasCompressionAlgorithm) {
            return null;
        }
        return byName(cmpAlg);
    }

    private String getAlgorithmFromHeader(Header header) {
        Assert.notNull(header, "header cannot be null.");

        return header.getCompressionAlgorithm();
    }

    private CompressionCodec byName(String name) {
        Assert.hasText(name, "'name' must not be empty");

        CompressionCodec codec = codecs.get(name.toUpperCase());
        if (codec == null) {
            throw new CompressionException(String.format(MISSING_COMPRESSION_MESSAGE, name));
        }

        return codec;
    }
}
