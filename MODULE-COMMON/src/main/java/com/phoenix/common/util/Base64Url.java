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

public class Base64Url {
    public static String encode(byte[] bytes) {
        String s = Base64.encodeBytes(bytes);
        return encodeBase64ToBase64Url(s);
    }

    public static byte[] decode(String s) {
        s = encodeBase64UrlToBase64(s);
        try {
            // KEYCLOAK-2479 : Avoid to try gzip decoding as for some objects, it may display exception to STDERR. And we know that object wasn't encoded as GZIP
            return Base64.decode(s, Base64.DONT_GUNZIP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param base64 String in base64 encoding
     * @return String in base64Url encoding
     */
    public static String encodeBase64ToBase64Url(String base64) {
        String s = base64.split("=")[0]; // Remove any trailing '='s
        s = s.replace('+', '-'); // 62nd char of encoding
        s = s.replace('/', '_'); // 63rd char of encoding
        return s;
    }


    /**
     * @param base64Url String in base64Url encoding
     * @return String in base64 encoding
     */
    public static String encodeBase64UrlToBase64(String base64Url) {
        String s = base64Url.replace('-', '+'); // 62nd char of encoding
        s = s.replace('_', '/'); // 63rd char of encoding
        switch (s.length() % 4) // Pad with trailing '='s
        {
            case 0:
                break; // No pad chars in this case
            case 2:
                s += "==";
                break; // Two pad chars
            case 3:
                s += "=";
                break; // One pad char
            default:
                throw new RuntimeException(
                        "Illegal base64url string!");
        }

        return s;
    }
}
