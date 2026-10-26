package com.feaster.api.auth.application.commands;

public record RegisterUserCommand(String username, String displayName, String email, String password) {}
