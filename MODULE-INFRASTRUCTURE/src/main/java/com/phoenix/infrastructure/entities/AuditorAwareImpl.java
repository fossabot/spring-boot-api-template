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

package com.phoenix.infrastructure.entities;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * JPA can analyze createdDate and lastModifiedDate using current system time, but what about the createdBy and
 * lastModifiedBy fields? How will JPA recognize what to store in them?
 * To tell JPA about currently logged-in users, we will need to provide an implementation of AuditorAware and override
 * the getCurrentAuditor() method. And inside getCurrentAuditor(), we will need to fetch a currently logged-in user.
 * As of now, I have provided a hard-coded user, but if you are using Spring Security, then use it to find the currently
 * logged-in user.
 */
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.empty();
        // Use below commented code when will use Spring Security.
    }

    // ------------------ Use below code for spring security --------------------------

//    class SpringSecurityAuditorAware implements AuditorAware<UserEntity> {
//
//        public UserEntity getCurrentAuditor() {
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated()) {
//                return null;
//            }
//
//            return ((MyUserDetails) authentication.getPrincipal()).getUser();
//        }
//    }

}
