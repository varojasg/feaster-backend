package com.feaster.api.posts.interfaces.rest;

import com.feaster.api.posts.application.commandservices.PostCommandService;
import com.feaster.api.posts.application.queryservices.PostQueryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest {
    @Test
    void listReturnsJsonArray() throws Exception {
        PostCommandService commandService = mock(PostCommandService.class);
        PostQueryService queryService = mock(PostQueryService.class);
        when(queryService.list(null, null)).thenReturn(List.of());
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new PostController(commandService, queryService)).build();
        mvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
