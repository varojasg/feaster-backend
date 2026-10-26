package com.feaster.api.users.interfaces.rest.resources;

import com.feaster.api.posts.interfaces.rest.resources.PostResource;
import com.feaster.api.collections.interfaces.rest.resources.CollectionResource;
import java.util.List;

public record FoodieContentResource(UserResource user, List<PostResource> posts,
                                    List<CollectionResource> collections) {}
