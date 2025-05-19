package cz.uhk.kpro2;

import cz.uhk.kpro2.model.User;
import cz.uhk.kpro2.repository.UserRepository;
import cz.uhk.kpro2.security.MyUserDetails;
import cz.uhk.kpro2.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(),
                new User()
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserFound() {
        User user = new User();
        user.setUsername("user1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);
        assertNotNull(result);
        assertEquals("user1", result.getUsername());
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUser(1L);
        assertNull(result);
    }

    @Test
    void testSaveNewUser_EncodesPassword() {
        User user = new User();
        user.setPassword("plainpass");
        when(passwordEncoder.encode("plainpass")).thenReturn("encodedpass");

        userService.saveUser(user);

        assertEquals("encodedpass", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testSaveExistingUser_DoesNotEncodePassword() {
        User user = new User();

        userService.saveUser(user);

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        User user = new User();
        user.setUsername("john");
        when(userRepository.findByUsername("john")).thenReturn(user);

        UserDetails details = userService.loadUserByUsername("john");

        assertNotNull(details);
        assertEquals("john", details.getUsername());
        assertTrue(details instanceof MyUserDetails);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("john");
        });
    }
}
