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

package com.phoenix.adapter.map;

import com.phoenix.domain.builder.UserBuilder;
import com.phoenix.domain.entity.User;
import com.phoenix.domain.payload.RegisterUser;

public class RegisterUserMapUser implements Mapper<RegisterUser, User> {

    @Override
    public User convert(RegisterUser registerUser) {
        UserBuilder userBuilder = UserBuilder.anUser()
                .withUsername(registerUser.getUsername())
                .withEmail(registerUser.getEmail())
                .withPassword(registerUser.getPassword());

        return userBuilder.build();
    }

    @Override
    public RegisterUser revert(User user) {
        return null;
    }
}
