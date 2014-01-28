package com.pricegsm.controller.account;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import com.pricegsm.dao.BaseUserDao;
import com.pricegsm.domain.Administrator;
import com.pricegsm.domain.BaseUser;
import com.pricegsm.service.BaseUserService;
import com.pricegsm.service.BaseUserServiceImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private BaseUserService userService = new BaseUserServiceImpl();

    @Mock
    private BaseUserDao baseUserDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        // arrange
        thrown.expect(UsernameNotFoundException.class);
        thrown.expectMessage("user not found");

        when(baseUserDao.loadByEmail("user@example.com")).thenReturn(null);
        // act
        userService.loadUserByUsername("user@example.com");
    }

    @Test
    public void shouldReturnUserDetails() {
        // arrange
        BaseUser demoUser = new Administrator("Admin", "admin@pricegsm.com", "password");
        when(baseUserDao.loadByEmail("admin@pricegsm.com")).thenReturn(demoUser);

        // act
        UserDetails userDetails = userService.loadUserByUsername("admin@pricegsm.com");

        // assert
        assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
        assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
        assertThat(hasAuthority(userDetails, "ADMIN_ROLE"));
    }

    private boolean hasAuthority(UserDetails userDetails, String role) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for(GrantedAuthority authority : authorities) {
            if(authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
