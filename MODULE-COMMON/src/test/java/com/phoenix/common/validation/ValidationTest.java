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

package com.phoenix.common.validation;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ValidationTest {
    @Test
    public void testValidEmail() {
//        https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
        List<String> validEmails = new ArrayList();
        validEmails.add("user@domain.com");
        validEmails.add("user@domain.co.in");
        validEmails.add("user.name@domain.com");
        validEmails.add("user_name@domain.com");
        validEmails.add("username@yahoo.corporate.in");

        for (String email : validEmails) {
            Assert.assertTrue(Validation.isValidEmail(email));
        }


        List<String> invalidEmails = new ArrayList();
        //Invalid emails
        invalidEmails.add(".username@yahoo.com");
        invalidEmails.add("username@yahoo.com.");
        invalidEmails.add("username@yahoo..com");
        invalidEmails.add("username@yahoo.c");
        invalidEmails.add("username@yahoo.corporate");

        for (String email : invalidEmails) {
            Assert.assertFalse(Validation.isValidEmail(email));
        }
    }

    @Test
    public void testValidUsername(){
        List<String> validUsernames = new LinkedList<>();

        validUsernames.add("dangdinhtai1gmailcom");
        validUsernames.add("dangdinhtai1gmailcom");
        validUsernames.add("aaaaaaaa");
        validUsernames.add("aaaaaa_a_a");
        validUsernames.add("aaaaaa_a_a");
        validUsernames.add("usernae");

        for (String username : validUsernames) {
            Assert.assertTrue(Validation.isValidUsername(username));
        }


        List<String> inValidUsernames = new LinkedList<>();

        inValidUsernames.add("");
        inValidUsernames.add(null);

        for (String username : inValidUsernames) {
            Assert.assertFalse(Validation.isValidUsername(username));
        }
    }
}
