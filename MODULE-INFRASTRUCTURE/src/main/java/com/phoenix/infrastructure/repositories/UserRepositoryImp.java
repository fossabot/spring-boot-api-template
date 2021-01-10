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

package com.phoenix.infrastructure.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.phoenix.domain.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepositoryImp {

    private final EntityManager entityManager;

    public UserRepositoryImp(@Qualifier("PrimaryEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public User createUser(User user) {
        String insertUserSql = "INSERT INTO user (USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, CREATED_DATE, CREATED_BY, LAST_MODIFIED_DATE, LAST_MODIFIED_BY) VALUES (?, ?, ?, ?, ?, DEFAULT, null, DEFAULT, null)";

        Query query = this.entityManager.createQuery(insertUserSql);

        query.setParameter(1, user.getUsername());
        query.setParameter(2, user.getEmail());
        query.setParameter(3, user.getPassword());
        query.setParameter(4, user.getFirstName());
        query.setParameter(5, user.getLastName());

        query.executeUpdate();

        Set<String> roles = new HashSet<>();
        List<String> list = new LinkedList<>();

        StringBuilder insertUserRoleSqlBuilder = new StringBuilder();

        insertUserRoleSqlBuilder.append("insert into user_role (user_id, role_id) select 1, id from role where NAME in (");

        for (String role : roles) {
            insertUserRoleSqlBuilder.append("'?',");
            list.add(role);
        }

        insertUserRoleSqlBuilder.deleteCharAt(insertUserRoleSqlBuilder.length() - 1);

        insertUserRoleSqlBuilder.append(")");

        query = null;
        query = this.entityManager.createQuery(insertUserRoleSqlBuilder.toString());

        for (int i = 0; i < list.size(); i++) {
            query.setParameter(i + 1, list.get(i));
        }

        query.executeUpdate();

        return user;
    }
}
