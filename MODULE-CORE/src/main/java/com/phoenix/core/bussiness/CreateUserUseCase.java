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

import com.phoenix.common.string_utils.ValidateString;
import com.phoenix.core.domain.User;
import com.phoenix.core.exception.UserAlreadyExistsException;
import com.phoenix.core.exception.UserValidationException;
import com.phoenix.core.map.Mapper;
import com.phoenix.core.model.RegisterUser;
import com.phoenix.core.port.repositories.PasswordEncoderPort;
import com.phoenix.core.port.repositories.UserRepositoryPort;

public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final Mapper registerUserMapUser;

    public CreateUserUseCase(UserRepositoryPort userRepository,
                             PasswordEncoderPort passwordEncoder,
                             Mapper registerUserMapUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerUserMapUser = registerUserMapUser;
    }


    /**
     * @param registerUser : thông tin user đăng kí
     *
     * Thực thi use case create user.
     * Nếu có bất kì lỗi nào sẽ ném ra 1 Exception
     */
    public void execute(RegisterUser registerUser) throws Exception{
        //0. Validate registerUser
        validate(registerUser);

        //1. Convert RegisterUser -> User
        User user = (User) registerUserMapUser.convert(registerUser);

        //2. Mã hóa mật khẩu
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        //3. Lưu user vào db.
        userRepository.save(user);
    }

    /**
     * @param registerUser :
     *                     throw UserValidation nếu sai.
     */
    private void validate(RegisterUser registerUser) {
        if (registerUser == null)
            throw new UserValidationException("User should not be null");
        if (ValidateString.isBlank(registerUser.getEmail()))
            throw new UserValidationException("Email should not be null.");
        if (!ValidateString.isNullOrNotBlank(registerUser.getEmail()))
            throw new UserValidationException("Email can be null but not blank.");
        if (userRepository.findByEmail(registerUser.getEmail()).isPresent())
            throw new UserAlreadyExistsException(registerUser.getEmail() + " is already exist.");
        if (userRepository.findByUsername("Email: " + registerUser.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username: " + registerUser.getUsername() + " is already exist.");
    }
}
