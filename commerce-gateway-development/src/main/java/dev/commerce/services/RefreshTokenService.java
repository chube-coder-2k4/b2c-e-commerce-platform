package dev.commerce.services;

import dev.commerce.entitys.RefreshToken;
import dev.commerce.entitys.Users;

import java.util.UUID;

public interface RefreshTokenService {
    void deleteByUserId(Users userId);
    String saveRefreshToken(RefreshToken refreshToken);
    RefreshToken getByUserId(Users userId);
}
