package com.middleware.parametrizacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import com.middleware.parametrizacion.dto.ParametrizacionMiddlewareDto;
import com.middleware.parametrizacion.dto.ParametrizacionMiddlewareUpdateDto;
import com.middleware.parametrizacion.entity.ParametrizacionMiddleware;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ParametrizacionMiddlewareMapper {
    ParametrizacionMiddleware toEntity(ParametrizacionMiddlewareDto dto);
    ParametrizacionMiddlewareDto toDto(ParametrizacionMiddleware entity);
    
    @Mapping(target = "valorParametro", ignore = true)
    void updateFromDto(ParametrizacionMiddlewareUpdateDto dto, @MappingTarget ParametrizacionMiddleware entity);
}
