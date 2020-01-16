package com.future.booklook.sevice;

import com.future.booklook.model.entity.User;
import com.future.booklook.repository.RoleRepository;
import com.future.booklook.repository.UserRepository;
import com.future.booklook.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private User expectedSingleUser;

    private Set<User> expectedMultiUser;

    @Before
    public void setup(){
        expectedSingleUser = new User(
                "Okka Muhiba",
                "okkamuhiba",
                "okka.muhiba@mail.com",
                "manusia",
                "085312345678"
        );

        expectedMultiUser = Collections.singleton(expectedSingleUser);
    }

    @Test
    public void test_FindByUserId(){
        User expected = expectedSingleUser;

        when(userRepository.findByUserId(anyString())).thenReturn(expected);

        User result = userService.findByUserId(anyString());
        assertEquals(expected, result);
    }

    @Test
    public void test_SaveUser(){
        User expected = expectedSingleUser;

        when(userRepository.findByUserId(anyString())).thenReturn(expected);
        doReturn(expected).when(userRepository).save(expected);

        User result = userService.save(expected);
        assertEquals(expected, result);
    }
}
