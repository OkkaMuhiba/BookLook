package com.future.booklook.service;

import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.properties.RoleName;
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

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
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
        expectedSingleUser.setReadKey("klajsownddoshopa");

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

        when(userRepository.save(expected)).thenReturn(expected);
        doReturn(expected).when(userRepository).save(expected);

        User result = userService.save(expected);
        assertEquals(expected, result);
    }

    @Test
    public void test_UserExistByUserId(){
        when(userRepository.existsByUserId(expectedSingleUser.getUserId())).thenReturn(true);

        Boolean result = userService.userExistByUserId(expectedSingleUser.getUserId());
        assertTrue(result);
    }

    @Test
    public void test_GetTotalUserInNumber(){
        Long expected = Integer.toUnsignedLong(expectedMultiUser.size());
        when(userRepository.count()).thenReturn(Integer.toUnsignedLong(expectedMultiUser.size()));

        Long result = userService.getTotalUserInNumber();
        assertEquals(expected, result);
    }

    @Test
    public void test_ExistByUsername(){
        when(userRepository.existsByUsername(expectedSingleUser.getUsername()))
                .thenReturn(Boolean.TRUE);

        Boolean result = userService.existByUsername(expectedSingleUser.getUsername());
        assertTrue(result);
    }

    @Test
    public void test_ExistByEmail(){
        when(userRepository.existsByEmail(expectedSingleUser.getEmail()))
                .thenReturn(Boolean.TRUE);

        Boolean result = userService.existByEmail(expectedSingleUser.getEmail());
        assertTrue(result);
    }

    @Test
    public void test_UserExistByUserIdAndReadKey(){
        when(userRepository.existsByUserIdAndReadKey(expectedSingleUser.getUserId(), anyString()))
                .thenReturn(Boolean.TRUE);

        Boolean result = userService.userExistByUserIdAndReadKey(expectedSingleUser.getUserId(), eq("klajsownddoshopa"));
        assertTrue(result);
    }

    @Test
    public void test_FindAllUser(){
        Set<User> expected = expectedMultiUser;
        Role userRole = new Role(RoleName.ROLE_USER);

        when(roleRepository.findByName(RoleName.ROLE_USER))
                .thenReturn(java.util.Optional.of(userRole));

        when(userRepository.findByRoles(Collections.singleton(userRole)))
                .thenReturn(expectedMultiUser);

        Set<User> result = userService.findAllUser();
        assertEquals(expected, result);
    }
}
