package com.feaster.api.posts.application.commands;

import java.util.List;

public record CreatePostCommand(String title, String description, String category,
                                List<String> hashtags, String mediaUrl, String mediaType) {}
