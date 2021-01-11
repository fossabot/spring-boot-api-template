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

import com.phoenix.infrastructure.config.PersistenceUnitsName;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.phoenix.domain.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Repository(value = "UserRepositoryImp")
public class UserRepositoryImp {

    @PersistenceContext(unitName = PersistenceUnitsName.PRIMARY_PERSISTENCE_UNIT_NAME)
    private final EntityManager entityManager;

    public UserRepositoryImp(@Qualifier("PrimaryEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
        this.entityManager.getTransaction();
    }

    @Transactional
    public User createUser(User user) {
        String insertUserSql = "INSERT INTO user (USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, CREATED_DATE, CREATED_BY, LAST_MODIFIED_DATE, LAST_MODIFIED_BY) VALUE (?, ?, ?, ?, ?, DEFAULT, null, DEFAULT, null)";

        Query query = this.entityManager.createNativeQuery(insertUserSql);

        query.setParameter(1, user.getUsername());
        query.setParameter(2, user.getEmail());
        query.setParameter(3, user.getPassword());
        query.setParameter(4, user.getFirstName());
        query.setParameter(5, user.getLastName());

        query.executeUpdate();

        Set<String> roles = user.getRoles();
        StringBuilder insertUserRoleSqlBuilder = new StringBuilder();

        if (roles == null || roles.isEmpty()) {
            return user;
        }

        insertUserRoleSqlBuilder.append("insert into user_role (user_id, role_id) select user.id, role.id from role, user where (user.email = ? or user.USERNAME = ?) and role.NAME in (");
        List<String> list = new LinkedList<>();

        for (String role : roles) {
            insertUserRoleSqlBuilder.append("?,");
            list.add(role);
        }

        insertUserRoleSqlBuilder.deleteCharAt(insertUserRoleSqlBuilder.length() - 1);

        insertUserRoleSqlBuilder.append(")");

        query = null;
        query = this.entityManager.createNativeQuery(insertUserRoleSqlBuilder.toString());

        query.setParameter(1, user.getEmail());
        query.setParameter(2, user.getUsername());

        for (int i = 0; i < list.size(); i++) {
            query.setParameter(i + 3, list.get(i));
        }

        query.executeUpdate();

        return user;
    }
}
