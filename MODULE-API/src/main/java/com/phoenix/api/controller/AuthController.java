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

package com.phoenix.api.controller;

import com.phoenix.adapter.controller.AuthControllerAdapter;
import com.phoenix.api.config.ApplicationUrls;
import com.phoenix.domain.payload.LoginUser;
import com.phoenix.domain.payload.RegisterUser;
import com.phoenix.domain.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApplicationUrls.AUTH_PREFIX)
public class AuthController {
    private final AuthControllerAdapter authController;

    public AuthController(AuthControllerAdapter authController) {
        this.authController = authController;
    }

    @PostMapping(ApplicationUrls.SIGN_UP)
    public ApiResponse signUp(@RequestBody RegisterUser registerUser) {
        return authController.signUp(registerUser);
    }

    @PostMapping(ApplicationUrls.SIGN_IN)
    public ApiResponse signIn(@RequestBody LoginUser loginUser) {
        return authController.signIn(loginUser);
    }
}
