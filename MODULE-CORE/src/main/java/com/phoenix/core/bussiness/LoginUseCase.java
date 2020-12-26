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

package com.phoenix.core.bussiness;

import com.phoenix.common.security.TokenProvider;
import com.phoenix.common.string_utils.ValidateString;
import com.phoenix.core.exception.UserValidationException;
import com.phoenix.core.model.payload.LoginUser;
import com.phoenix.core.port.security.AuthenticationManagerPort;

public class LoginUseCase {
    private final AuthenticationManagerPort authenticationManager;
    private final TokenProvider tokenProvider;

    public LoginUseCase(AuthenticationManagerPort authenticationManager,
                        TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public String execute(LoginUser user) throws Exception {
        // validate input
        validate(user);

        // Xác thực user.
        authenticationManager.authenticate(user.getUsername(), user.getPassword());

        //Tạo token
        String token = tokenProvider.generateToken();

        return token;
    }

    private void validate(LoginUser user) {
        if (user == null)
            throw new UserValidationException("User should not be null");
        if (ValidateString.isBlank(user.getEmail()))
            throw new UserValidationException("Email should not be null.");
        if (!ValidateString.isNullOrNotBlank(user.getEmail()))
            throw new UserValidationException("Email can be null but not blank.");
    }
}
