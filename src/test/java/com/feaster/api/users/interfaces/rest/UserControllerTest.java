package com.feaster.api.users.interfaces.rest;

import com.feaster.api.users.application.commandservices.UserCommandService;
import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.users.application.queries.UserView;
import com.feaster.api.collections.application.queryservices.CollectionQueryService;
import com.feaster.api.posts.application.queryservices.PostQueryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {
    @Test
    void publicFoodieContentReturnsPostsAndCollections() throws Exception {
        UserQueryService userQueryService = mock(UserQueryService.class);
        UserCommandService userCommandService = mock(UserCommandService.class);
        PostQueryService postQueryService = mock(PostQueryService.class);
        CollectionQueryService collectionQueryService = mock(CollectionQueryService.class);
        when(userQueryService.profile("u1")).thenReturn(new UserView("u1", "luna", "Luna", "", 0, 0));
        when(postQueryService.byAuthor("u1")).thenReturn(List.of());
        when(collectionQueryService.byOwner("u1")).thenReturn(List.of());
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new UserController(
                userQueryService, userCommandService, postQueryService, collectionQueryService)).build();
        mvc.perform(get("/api/foodies/u1/public-content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("luna"))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.collections").isArray());
    }
}
