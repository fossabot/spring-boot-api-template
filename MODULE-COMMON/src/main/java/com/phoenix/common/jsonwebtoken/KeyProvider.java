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

package com.phoenix.common.jsonwebtoken;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phoenix.common.KeyWrapper;
import com.phoenix.common.jsonwebtoken.crypto.Keys;
import com.phoenix.common.jsonwebtoken.crypto.SignatureAlgorithm;
import com.phoenix.common.util.Base64;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public final class KeyProvider {
    private final KeyWrapper keyWrapper;

    /**
     * Private constructor.
     */
    private KeyProvider() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("jws-key.json")).getFile());

        if (!isEmptyKey()) {
            Scanner scanner = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                scanner = new Scanner(file);

                while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine());
                    stringBuilder.append("\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                scanner.close();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            keyWrapper = gson.fromJson(stringBuilder.toString(), KeyWrapper.class);
        } else {
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

            String secretString = Base64.encodeBytes(key.getEncoded());
            String keyId = Keys.createKeyId(key);

            keyWrapper = new KeyWrapper(secretString, key, keyId);

            try {
                saveKey();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public KeyWrapper getKeyWrapper() {
        return keyWrapper;
    }

    private void saveKey() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(this.keyWrapper);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("jws-key.json")).getFile());

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(gson.toJson(this.keyWrapper));
        fileWriter.close();
    }

    /**
     * Kiểm tra xem đã lưu key nào file chưa.
     *
     * @return return false nếu đã có key lưu rồi, return true nếu chưa lưu key nào.
     */
    private boolean isEmptyKey() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("jws-key.json")).getFile());

        return file.length() == 0;
    }

    /**
     * Singleton instance.
     *
     * @return Singleton instance
     */
    public static KeyProvider getInstance() {
        return KeyProviderHelper.INSTANCE;
    }

    /**
     * Provides the lazy-loaded Singleton instance.
     */
    private static class KeyProviderHelper {
        private static final KeyProvider INSTANCE = new KeyProvider();
    }
}
