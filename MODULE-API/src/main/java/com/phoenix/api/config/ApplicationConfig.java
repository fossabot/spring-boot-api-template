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

package com.phoenix.api.config;

import com.phoenix.adapter.controller.AuthControllerAdapter;
import com.phoenix.adapter.security.AuthenticationManagerAdapter;
import com.phoenix.common.jsonwebtoken.KeyProvider;
import com.phoenix.common.jsonwebtoken.TokenProvider;
import com.phoenix.config.SpringConfiguration;
import com.phoenix.core.bussiness.SignUpUseCase;
import com.phoenix.infrastructure.repositories.UserRepositoryImp;
import com.phoenix.infrastructure.repositories.primary.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.File;
import java.io.IOException;

@Configuration
public class ApplicationConfig {
    private final SpringConfiguration configuration;


    public ApplicationConfig(@Qualifier("UserRepository") UserRepository userRepository,
                             @Qualifier("UserRepositoryImp") UserRepositoryImp userRepositoryImp,
                             @Qualifier("DefaultAuthenticationManager") @Lazy AuthenticationManager authenticationManager
    ) throws IOException, ClassNotFoundException {
        File file = new ClassPathResource(ApplicationConstant.KEY_FILE).getFile();

        configuration = new SpringConfiguration(
                userRepository,
                userRepositoryImp,
                authenticationManager,
                file);
    }

    @Bean(value = "AuthControllerAdapterBean")
    public AuthControllerAdapter authControllerAdapter() {
        return configuration.authControllerAdapter();
    }

    @Bean(value = "KeyProvider")
    public KeyProvider keyProvider() {
        return configuration.createKeyProvider();
    }

    @Bean(value = "TokenProvider")
    public TokenProvider tokenProvider() {
        return configuration.createTokenProvider();
    }

    @Bean(value = "AuthenticationManagerAdapter")
    public AuthenticationManagerAdapter authenticationManagerAdapter() {
        return configuration.createAuthenticationManagerAdapter();
    }
}
