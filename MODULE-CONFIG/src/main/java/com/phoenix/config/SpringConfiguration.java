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

package com.phoenix.config;

import com.phoenix.adapter.controller.AuthControllerAdapter;
import com.phoenix.adapter.map.DomainUserMapUserEntity;
import com.phoenix.adapter.map.Mapper;
import com.phoenix.adapter.repository.UserRepositoryAdapter;
import com.phoenix.adapter.security.AuthenticationManagerAdapter;
import com.phoenix.adapter.security.PasswordEncoderAdapter;
import com.phoenix.common.jsonwebtoken.DefaultTokenProvider;
import com.phoenix.common.jsonwebtoken.TokenProvider;
import com.phoenix.core.bussiness.SignInUseCase;
import com.phoenix.core.bussiness.SignUpUseCase;
import com.phoenix.common.jsonwebtoken.KeyProvider;
import com.phoenix.core.port.repositories.UserRepositoryPort;
import com.phoenix.core.port.security.PasswordEncoderPort;
import com.phoenix.infrastructure.repositories.UserRepositoryImp;
import com.phoenix.infrastructure.repositories.primary.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.File;
import java.io.IOException;

/**
 * File cấu hình để chỉ ra mối liên hệ giữa các đối tượng
 * map các port và adapter lại với nhau tại đây.
 */
public class SpringConfiguration {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final KeyProvider keyProvider;
    private final AuthenticationManagerAdapter authenticationManager;


    public SpringConfiguration(UserRepository userRepository,
                               UserRepositoryImp userRepositoryImp,
                               AuthenticationManager authenticationManager,
                               File keyFile
    ) throws IOException, ClassNotFoundException {
        this.passwordEncoderPort = new PasswordEncoderAdapter();
        this.keyProvider = new KeyProvider(keyFile);

        this.authenticationManager = new AuthenticationManagerAdapter(authenticationManager);
        Mapper domainUserMapUserEntity = new DomainUserMapUserEntity();
        this.userRepositoryPort = new UserRepositoryAdapter(domainUserMapUserEntity, userRepository, userRepositoryImp);
    }

    //=======================================================
    //                   CONTROLLER
    //=======================================================

    public AuthControllerAdapter authControllerAdapter() {
        return new AuthControllerAdapter(this.signUpUseCase(), this.signInUseCase());
    }

    //=======================================================
    //                   USE CASE
    //=======================================================

    public SignUpUseCase signUpUseCase() {
        return new SignUpUseCase(this.userRepositoryPort, this.passwordEncoderPort);
    }

    public SignInUseCase signInUseCase() {
        return new SignInUseCase(this.authenticationManager, this.createTokenProvider(), this.userRepositoryPort);
    }

    //=======================================================
    //                   DEPENDENCY
    //=======================================================

    public KeyProvider createKeyProvider() {
        return this.keyProvider;
    }

    public TokenProvider createTokenProvider() {
        return new DefaultTokenProvider(this.keyProvider);
    }

    public AuthenticationManagerAdapter createAuthenticationManagerAdapter() {
        return this.authenticationManager;
    }


}
