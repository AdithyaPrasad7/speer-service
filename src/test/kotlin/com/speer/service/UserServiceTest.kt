package com.speer.service


import com.nhaarman.mockitokotlin2.whenever
import com.speer.service.auth.AuthDetails
import com.speer.service.auth.JwtTokenUtil
import com.speer.service.dto.request.UserLogInRequest
import com.speer.service.dto.request.UserSignUpRequest
import com.speer.service.model.User
import com.speer.service.repository.IUserRepository
import com.speer.service.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import com.speer.service.enum.UserCreationStatus
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication


@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private lateinit var iUserRepository: IUserRepository

    @Mock
    private lateinit var authManager: AuthenticationManager

    @Mock
    private lateinit var jwtUtil: JwtTokenUtil

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var authDetails: AuthDetails


    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userService = UserService(iUserRepository, authManager, jwtUtil, authDetails)

    }

    @Test
    fun `test login UserService method with valid User credentials are passed`() {
        val request = UserLogInRequest(email = "test@gmail.com", password = "TestUser1@")
        val user = User(1, "testUser", request.email, "9876543210", request.password)
        val token = "mockedJwtToken"
        whenever(iUserRepository.findByEmail(request.email)).thenReturn(user)
        whenever(
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.email,
                    request.password
                )
            )
        ).thenReturn(mockedAuthentication())
        whenever(jwtUtil.generateToken(request.email)).thenReturn(token)

        val response = userService.login(request)

        assertEquals("User logged-in successfully", response.message)
        assertEquals(token, response.token)
        assertNotNull(response.userResponse)
        assertEquals(user.toResponse(), response.userResponse)
    }

    @Test
    fun `test login UserService method with invalid User credentials are passed`() {

        val request = UserLogInRequest(email = "nonexistent@gmail.com", password = "invalidpassword")
        whenever(iUserRepository.findByEmail(request.email)).thenReturn(null)

        val response = userService.login(request)

        assertEquals("User not found!", response.message)
        assertNull(response.token)
        assertNull(response.userResponse)
    }

    @Test
    fun `test addUser UserService method with valid User request are passed`() {
        val request = UserSignUpRequest(
            name = "Test User",
            email = "test@gmail.com",
            phone = "9876543210",
            password = "TestUser1@"
        )
        whenever(iUserRepository.findByEmail(request.email)).thenReturn(null)
        whenever(iUserRepository.save(any(User::class.java))).thenReturn(request.toUser())

        val result = userService.addUser(request)


        assert(result == UserCreationStatus.SUCCESS)
        verify(iUserRepository).findByEmail(request.email)
        verify(iUserRepository).save(any(User::class.java))
    }

    @Test
    fun `test addUser UserService method with invalid email is passed`() {
        val request = UserSignUpRequest(
            name = "Test User",
            email = "invalidEmail",
            phone = "9876543210",
            password = "TestUser1@"
        )

        val result = userService.addUser(request)

        assert(result == UserCreationStatus.EMAIL_INVALID)
    }

    @Test
    fun `test addUser UserService method with invalid phone when phone is less than 10digits`() {
        val request = UserSignUpRequest(
            name = "Test User",
            email = "test@gmail.com",
            phone = "98763",
            password = "TestUser1@"
        )

        val result = userService.addUser(request)

        assert(result == UserCreationStatus.PHONE_INVALID)
    }

    @Test
    fun `test addUser UserService method with invalid phone when phone has alphabets and symbols`() {
        val request = UserSignUpRequest(
            name = "Test User",
            email = "test@gmail.com",
            phone = "987d6a3@",
            password = "TestUser1@"
        )

        val result = userService.addUser(request)

        assert(result == UserCreationStatus.PHONE_INVALID)
    }


    @Test
    fun `test addUser UserService method when User already exists`() {
        val request = UserSignUpRequest(
            name = "Existing User",
            email = "existing@gmail.com",
            phone = "9876543210",
            password = "TestUser1@"
        )
        whenever(iUserRepository.findByEmail(request.email)).thenReturn(
            User(
                id = 1,
                name = "Existing User",
                email = request.email,
                password = "encrypted_password",
                phone = "9876543210"
            )
        )

        val result = userService.addUser(request)

        assert(result == UserCreationStatus.USER_ALREADY_EXIST)
    }

    @Test
    fun `test getUserByEmail when User exists`() {
        val loggedInUser =
            User(id = 1, name = "Test User", email = "test@gmail.com", password = "password", phone = "9876543210")
        val userEmail = "test@gmail.com"

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iUserRepository.findByEmail(userEmail)).thenReturn(loggedInUser)

        val result = userService.getUserByEmail(userEmail)

        assert(result == loggedInUser)
    }

    @Test
    fun `test getUserByEmail User not exists`() {
        val loggedInUser = User(
            name = "Invalid User",
            email = "Invalid User",
            password = "Invalid User",
            phone = "Invalid User"
        )
        val invalidEmail = "invalid@gmail.com"

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iUserRepository.findByEmail(invalidEmail)).thenReturn(null)

        val result = userService.getUserByEmail(invalidEmail)

        assert(result.id == null)
        assert(result.name == "Invalid User")
    }

    private fun mockedAuthentication(): Authentication {
        val authentication = mock(Authentication::class.java)
        return UsernamePasswordAuthenticationToken("test@gmail.com", "password", emptyList())
    }
}
