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

package com.phoenix.common.jsonwebtoken.common;

import com.google.gson.Gson;
import com.phoenix.common.exception.ioe.DeserializationException;
import com.phoenix.common.lang.Assert;
import com.phoenix.common.lang.Strings;

import java.io.IOException;

public class GsonDeserializer<T> implements Deserializer<T> {

    private final Class<T> returnType;
    private final Gson gson;

    @SuppressWarnings("unused") //used via reflection by RuntimeClasspathDeserializerLocator
    public GsonDeserializer() {
        this(GsonSerializer.DEFAULT_GSON);
    }

    @SuppressWarnings({"unchecked", "WeakerAccess", "unused"}) // for end-users providing a custom gson
    public GsonDeserializer(Gson gson) {
        this(gson, (Class<T>) Object.class);
    }

    private GsonDeserializer(Gson gson, Class<T> returnType) {
        Assert.notNull(gson, "gson cannot be null.");
        Assert.notNull(returnType, "Return type cannot be null.");
        this.gson = gson;
        this.returnType = returnType;
    }

    @Override
    public T deserialize(byte[] bytes) throws DeserializationException {
        try {
            return readValue(bytes);
        } catch (IOException e) {
            String msg = "Unable to deserialize bytes into a " + returnType.getName() + " instance: " + e.getMessage();
            throw new DeserializationException(msg, e);
        }
    }

    protected T readValue(byte[] bytes) throws IOException {
        return gson.fromJson(new String(bytes, Strings.UTF_8), returnType);
    }
}
