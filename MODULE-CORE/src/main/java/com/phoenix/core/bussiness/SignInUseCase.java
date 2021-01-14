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

import com.phoenix.common.jsonwebtoken.Scope;
import com.phoenix.common.jsonwebtoken.TokenProvider;
import com.phoenix.common.exception.runtime.UserValidationException;
import com.phoenix.common.jsonwebtoken.component.Claims;
import com.phoenix.common.jsonwebtoken.component.DefaultClaims;
import com.phoenix.common.lang.Strings;
import com.phoenix.common.util.IdGenerator;
import com.phoenix.core.port.repositories.UserRepositoryPort;
import com.phoenix.core.port.security.AuthenticationManagerPort;
import com.phoenix.domain.entity.User;
import com.phoenix.domain.model.AccessToken;
import com.phoenix.domain.payload.LoginUser;

import java.util.Date;
import java.util.Optional;

public class SignInUseCase {
    private final AuthenticationManagerPort authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepositoryPort userRepositoryPort;

    public SignInUseCase(AuthenticationManagerPort authenticationManager,
                         TokenProvider tokenProvider,
                         UserRepositoryPort userRepositoryPort) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepositoryPort = userRepositoryPort;
    }

    public AccessToken execute(LoginUser loginUser) throws Exception {
        // validate input
        validate(loginUser);

        // Xác thực loginUser.
        authenticationManager.authenticate(loginUser.getUsername(), loginUser.getPassword());

        Optional<User> optional = userRepositoryPort.findUserByEmailOrUsername(loginUser.getUsername());

        if (optional.isPresent()) {
            User user = optional.get();
            long expiration = tokenProvider.getExpiryDuration();
            Date now = new Date();
            String accessTokenScope = Scope.ACCESS.toString();
            String accessTokenId = IdGenerator.generate();

            Claims claims = new DefaultClaims();


            claims.setId(accessTokenId);
            claims.setExpiration(new Date(now.getTime() + expiration));
            claims.setIssuedAt(now);
            claims.setSubject(String.valueOf(user.getId()));
            claims.put("username", user.getUsername());
            claims.put("email", user.getEmail());
            claims.put("scope", accessTokenScope);

            String accessToken = tokenProvider.generateTokenFromClaims(claims);

            String refreshTokenScope = Scope.REFRESH.toString();
            claims.setExpiration(new Date(now.getTime() + expiration * 2));
            String refreshTokenId = IdGenerator.generate();
            claims.put("scope", refreshTokenScope);
            claims.setId(refreshTokenId);

            String refreshToken = tokenProvider.generateTokenFromClaims(claims, expiration * 2);

            AccessToken token = new AccessToken();

            token.setAccessToken(accessToken);
            token.setTokenId(accessTokenId);
            token.setTokenType("Bearer");
            token.setRefreshToken(refreshToken);
            token.setNotBeforePolicy("0");
            token.setScope(accessTokenScope);
            token.setExpiresIn("");
            token.setRefreshExpiresIn("");

            return token;
        } else {

            return null;
        }
    }

    private void validate(LoginUser user) {
        if (user == null)
            throw new UserValidationException("User should not be null");
        if (!Strings.isNullOrNotBlank(user.getUsername()))
            throw new UserValidationException("Username can be null but not blank.");
        if (!Strings.isNullOrNotBlank(user.getEmail()))
            throw new UserValidationException("Email can be null but not blank.");
        if(!Strings.hasLength(user.getEmail()) && !Strings.hasLength(user.getUsername()))
            throw new UserValidationException("Username and email must not be concurrently empty.");
    }

}
