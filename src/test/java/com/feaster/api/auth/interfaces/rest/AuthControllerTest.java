package com.feaster.api.auth.interfaces.rest;

import com.feaster.api.auth.application.commandservices.AuthCommandService;
import com.feaster.api.auth.application.commands.LoginCommand;
import com.feaster.api.auth.application.results.AuthResult;
import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.users.application.queries.UserView;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
    @Test
    void loginReturnsToken() throws Exception {
        AuthCommandService authCommandService = mock(AuthCommandService.class);
        UserQueryService userQueryService = mock(UserQueryService.class);
        when(authCommandService.login(new LoginCommand("luna", "password123"))).thenReturn(new AuthResult(
                "token", new UserView("u1", "luna", "Luna", "", 0, 0)));
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new AuthController(authCommandService, userQueryService)).build();
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"luna\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.user.username").value("luna"));
    }
}
