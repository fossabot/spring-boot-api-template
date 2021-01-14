package com.phoenix.api.jpa;

import com.phoenix.infrastructure.entities.primary.UserEntity;
import com.phoenix.infrastructure.repositories.primary.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestUserRepo {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRepository(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("1");
        userEntity.setPassword("2");
        userEntity.setEmail("3");
        userEntity.setPassword("4");
        userRepository.save(userEntity);
    }
}
