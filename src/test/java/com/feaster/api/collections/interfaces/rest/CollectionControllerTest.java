package com.feaster.api.collections.interfaces.rest;

import com.feaster.api.collections.application.commandservices.CollectionCommandService;
import com.feaster.api.collections.application.queryservices.CollectionQueryService;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CollectionControllerTest {
    @Test
    void publicCollectionsReturnsOnlyServiceResults() throws Exception {
        CollectionCommandService commandService = mock(CollectionCommandService.class);
        CollectionQueryService queryService = mock(CollectionQueryService.class);
        GourmetCollection collection = new GourmetCollection();
        collection.collectionKey = "c1";
        collection.name = "Recetas";
        collection.visibility = Visibility.PUBLIC;
        collection.owner = new AuthorPreview("u1", "luna", "Luna");
        when(queryService.publicCollections()).thenReturn(List.of(collection));
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new CollectionController(commandService, queryService)).build();
        mvc.perform(get("/api/collections/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].collectionKey").value("c1"))
                .andExpect(jsonPath("$[0].visibility").value("PUBLIC"));
    }
}
