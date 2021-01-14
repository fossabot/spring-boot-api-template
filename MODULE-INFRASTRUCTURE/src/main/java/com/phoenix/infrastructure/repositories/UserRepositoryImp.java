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

import com.phoenix.domain.builder.UserBuilder;
import com.phoenix.domain.entity.User;
import com.phoenix.infrastructure.config.PersistenceUnitsName;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository(value = "UserRepositoryImp")
public class UserRepositoryImp {

    @PersistenceContext(unitName = PersistenceUnitsName.PRIMARY_PERSISTENCE_UNIT_NAME)
    private final EntityManager entityManager;

    public UserRepositoryImp(@Qualifier("PrimaryEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * INSERT INTO user
     * (USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, CREATED_DATE, CREATED_BY, LAST_MODIFIED_DATE, LAST_MODIFIED_BY)
     * VALUE
     * (?, ?, ?, ?, ?, DEFAULT, null, DEFAULT, null);
     * <p>
     * ------------------------------------------------------------------------------------------------------------------
     * <p>
     * insert into user_role
     * (user_id, role_id)
     * select
     * user.id, role.id
     * from
     * role, user
     * where
     * (user.email = ? or user.USERNAME = ?)
     * and role.NAME in (? ... ? );
     */
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

    /**
     * select u.id, u.USERNAME, u.EMAIL, u.PASSWORD, u.FIRST_NAME, u.LAST_NAME, GROUP_CONCAT(r.NAME) roles
     * from user u,
     * role r,
     * user_role ur
     * where u.id = ur.USER_ID
     * and r.id = ur.ROLE_ID
     * and (u.USERNAME = ? or u.EMAIL = ?)
     */
    public User findUserByEmailOrUsername(String s) {
        String sql = "select u.id, u.USERNAME, u.EMAIL, u.PASSWORD, u.FIRST_NAME, u.LAST_NAME, GROUP_CONCAT(r.NAME) roles from user u, role r, user_role ur where u.id = ur.USER_ID and r.id = ur.ROLE_ID and (u.USERNAME = ? or u.EMAIL = ?)";

        Query query = this.entityManager.createNativeQuery(sql);

        query.setParameter(1, s);
        query.setParameter(2, s);

        List<Object[]> queryResultList = query.getResultList();

        UserBuilder userBuilder = UserBuilder.anUser();

        Set<String> roles = new HashSet<>();

        for (Object[] record : queryResultList) {
            userBuilder
                    .withId(Long.parseLong(record[0].toString()))
                    .withUsername(record[1].toString())
                    .withEmail(record[2].toString())
                    .withPassword(record[3].toString());

            if (record[4] != null) {
                userBuilder.withFirstName(record[3].toString());
            }

            if (record[5] != null) {
                userBuilder.withLastName(record[4].toString());
            }

            String[] tmp = record[6].toString().split(",");
            roles.addAll(Arrays.asList(tmp));

            userBuilder.withRoles(roles);

            break;
        }

        return userBuilder.build();
    }
}
