package org.city_discover.utill;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.city_discover.dto.UserExternalDto;
import org.city_discover.dto.user.UserDto;
import org.city_discover.dto.user.UserUpdateDto;
import org.city_discover.entity.UserEntity;
import org.city_discover.exception.UnprocessableActionException;
import org.modelmapper.ModelMapper;

@UtilityClass
public class EntityConverter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();

    public static UserDto mapUserEntityToUserDto(UserEntity user) {
        UserDto dto = modelMapper.map(user, UserDto.class);

        try {
            dto.setPreferences(objectMapper.readValue(user.getPreferences(), new TypeReference<>() {
            }));
        } catch (JsonProcessingException e) {
            throw new UnprocessableActionException("Невозможно конвертировать объект.");
        }

        return dto;
    }

    public static UserExternalDto mapUserEntityToUserExternalDto(UserEntity user) {
        UserExternalDto dto = modelMapper.map(user, UserExternalDto.class);

        try {
            dto.setPreferences(objectMapper.readValue(user.getPreferences(), new TypeReference<>() {
            }));
        } catch (JsonProcessingException e) {
            throw new UnprocessableActionException("Невозможно конвертировать объект.");
        }

        return dto;
    }

    public static UserEntity mapUserUpdateDtoToUserEntity(UserUpdateDto user) {
        UserEntity entity = modelMapper.map(user, UserEntity.class);
        entity.setPreferences(getJsonValue(user.getPreferences()));

        return entity;
    }


    private String getJsonValue(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
