package ua.lastbite.token_service.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.lastbite.token_service.dto.TokenRequest;
import ua.lastbite.token_service.model.Token;


@Mapper(componentModel = "spring")
public interface TokenMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "expiresAt", expression = "java(java.time.LocalDateTime.now().plusSeconds(tokenExpirationTime))")
    @Mapping(target = "used", constant = "false")
    Token toEntity(TokenRequest request, @Context long tokenExpirationTime);
}
