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

package com.phoenix.common.util;

import com.phoenix.common.exception.ioe.SerializationException;
import com.phoenix.common.jsonwebtoken.common.GsonSerializer;
import com.phoenix.common.jsonwebtoken.common.Serializer;
import org.junit.Test;

public class TestBase64 {
    @Test
    public void testBase64EncodeBytes(){
        String secret = "sajkdnaskda,smd,sadkas;;d";
        System.out.println(Base64.encodeBytes(secret.getBytes()));
        System.out.println(Base64Url.encode(secret.getBytes()));
    }//NOPMD
//NOPMD
    @Test
    public void testBase64UrlEncodeObject() throws SerializationException {
        Serializer<String> serializer = new GsonSerializer<>();
        String a = "1";
        System.out.println(new String(serializer.serialize(a)));
    }
}//NOPMD
