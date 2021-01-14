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

package com.phoenix.adapter.controller;

import com.phoenix.adapter.map.RegisterUserMapUser;
import com.phoenix.core.bussiness.SignInUseCase;
import com.phoenix.core.bussiness.SignUpUseCase;
import com.phoenix.domain.entity.User;
import com.phoenix.domain.model.AccessToken;
import com.phoenix.domain.payload.LoginUser;
import com.phoenix.domain.payload.RegisterUser;
import com.phoenix.domain.response.ApiResponse;
import com.phoenix.domain.response.HttpStatus;
import com.phoenix.domain.response.ResponseType;

public class AuthControllerAdapter {
    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;

    public AuthControllerAdapter(SignUpUseCase signUpUseCase, SignInUseCase signInUseCase) {
        this.signUpUseCase = signUpUseCase;
        this.signInUseCase = signInUseCase;
    }

    public ApiResponse signUp(RegisterUser registerUser) {
        try {
            RegisterUserMapUser mapping = new RegisterUserMapUser();

            User domainUser = signUpUseCase.execute(mapping.convert(registerUser));


            ApiResponse<User> userApiResponse = new ApiResponse<User>(
                    String.valueOf(HttpStatus.CREATED.value()),
                    ResponseType.INFO, domainUser,
                    "Enums.Message.RESOURCE_CREATED.value()");
            return userApiResponse;
        } catch (Exception e) {
            //e.printStackTrace();
            ApiResponse<Exception> exceptionApiResponse = new ApiResponse<>(
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    ResponseType.EXCEPTION, null,
                    e.getMessage());
            return exceptionApiResponse;
        }
    }

    public ApiResponse signIn(LoginUser user) {
        try {
            AccessToken accessToken = this.signInUseCase.execute(user);

            ApiResponse<AccessToken> apiResponse = new ApiResponse<AccessToken>(
                    String.valueOf(HttpStatus.CREATED.value()),
                    ResponseType.INFO, accessToken,
                    "Enums.Message.RESOURCE_CREATED.value()");

            return apiResponse;
        } catch (Exception e) {
            //e.printStackTrace();
            ApiResponse<Exception> exceptionApiResponse = new ApiResponse<>(
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    ResponseType.EXCEPTION, null,
                    e.getMessage());
            return exceptionApiResponse;
        }
    }
}
