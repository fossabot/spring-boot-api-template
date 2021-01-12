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
import com.phoenix.adapter.security.PasswordEncoderAdapter;
import com.phoenix.adapter.map.DomainUserMapUserEntity;
import com.phoenix.adapter.map.Mapper;
import com.phoenix.adapter.repository.UserRepositoryAdapter;
import com.phoenix.common.jsonwebtoken.DefaultTokenProvider;
import com.phoenix.common.jsonwebtoken.KeyProvider;
import com.phoenix.common.jsonwebtoken.TokenProvider;
import com.phoenix.core.bussiness.SignUpUseCase;
import com.phoenix.core.port.repositories.UserRepositoryPort;
import com.phoenix.core.port.security.PasswordEncoderPort;
import com.phoenix.infrastructure.repositories.UserRepositoryImp;
import com.phoenix.infrastructure.repositories.primary.UserRepository;

public class SpringConfiguration {
    private final UserRepositoryPort userRepositoryPort;
    private final Mapper domainUserMapUserEntity;
    private final PasswordEncoderPort passwordEncoderPort;


    public SpringConfiguration(UserRepository userRepository, UserRepositoryImp userRepositoryImp) {
        this.domainUserMapUserEntity = new DomainUserMapUserEntity();
        this.userRepositoryPort = new UserRepositoryAdapter(domainUserMapUserEntity, userRepository, userRepositoryImp);

        this.passwordEncoderPort = new PasswordEncoderAdapter();

        System.out.println("-------------------------------------------------------------");
    }

    public SignUpUseCase createUserUseCase() {
        return new SignUpUseCase(this.userRepositoryPort, this.passwordEncoderPort);
    }

    public AuthControllerAdapter authControllerAdapter() {
        return new AuthControllerAdapter(this.createUserUseCase());
    }

    public KeyProvider createKeyProvider() {
        return new KeyProvider();
    }

    public TokenProvider createTokenProvider(KeyProvider keyProvider) {
        return new DefaultTokenProvider(keyProvider);
    }

}
