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

package com.phoenix.common.lang;

import com.phoenix.common.exception.runtime.UnavailableImplementationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import static com.phoenix.common.lang.Collections.arrayToList;

/**
 * Helper class for loading services from the classpath, using a {@link ServiceLoader}. Decouples loading logic for
 * better separation of concerns and testability.
 */
public final class Services {

    private static final List<ClassLoaderAccessor> CLASS_LOADER_ACCESSORS = arrayToList(new ClassLoaderAccessor[] {
            new ClassLoaderAccessor() {
                @Override
                public ClassLoader getClassLoader() {
                    return Thread.currentThread().getContextClassLoader();
                }
            },
            new ClassLoaderAccessor() {
                @Override
                public ClassLoader getClassLoader() {
                    return Services.class.getClassLoader();
                }
            },
            new ClassLoaderAccessor() {
                @Override
                public ClassLoader getClassLoader() {
                    return ClassLoader.getSystemClassLoader();
                }
            }
    });

    private Services() {}

    /**
     * Loads and instantiates all service implementation of the given SPI class and returns them as a List.
     *
     * @param spi The class of the Service Provider Interface
     * @param <T> The type of the SPI
     * @return An unmodifiable list with an instance of all available implementations of the SPI. No guarantee is given
     * on the order of implementations, if more than one.
     */
    public static <T> List<T> loadAll(Class<T> spi) {
        Assert.notNull(spi, "Parameter 'spi' must not be null.");

        for (ClassLoaderAccessor classLoaderAccessor : CLASS_LOADER_ACCESSORS) {
            List<T> implementations = loadAll(spi, classLoaderAccessor.getClassLoader());
            if (!implementations.isEmpty()) {
                return Collections.unmodifiableList(implementations);
            }
        }

        throw new UnavailableImplementationException(spi);
    }

    private static <T> List<T> loadAll(Class<T> spi, ClassLoader classLoader) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(spi, classLoader);
        List<T> implementations = new ArrayList<>();
        for (T implementation : serviceLoader) {
            implementations.add(implementation);
        }
        return implementations;
    }

    /**
     * Loads the first available implementation the given SPI class from the classpath. Uses the {@link ServiceLoader}
     * to find implementations. When multiple implementations are available it will return the first one that it
     * encounters. There is no guarantee with regard to ordering.
     *
     * @param spi The class of the Service Provider Interface
     * @param <T> The type of the SPI
     * @return A new instance of the service.
     * @throws UnavailableImplementationException When no implementation the SPI is available on the classpath.
     */
    public static <T> T loadFirst(Class<T> spi) {
        Assert.notNull(spi, "Parameter 'spi' must not be null.");

        for (ClassLoaderAccessor classLoaderAccessor : CLASS_LOADER_ACCESSORS) {
            T result = loadFirst(spi, classLoaderAccessor.getClassLoader());
            if (result != null) {
                return result;
            }
        }
        throw new UnavailableImplementationException(spi);
    }

    private static <T> T loadFirst(Class<T> spi, ClassLoader classLoader) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(spi, classLoader);
        if (serviceLoader.iterator().hasNext()) {
            return serviceLoader.iterator().next();
        }
        return null;
    }

    private interface ClassLoaderAccessor {
        ClassLoader getClassLoader();
    }
}
