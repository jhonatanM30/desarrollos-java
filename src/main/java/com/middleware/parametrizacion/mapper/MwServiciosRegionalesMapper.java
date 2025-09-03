package com.middleware.parametrizacion.mapper;

import com.middleware.parametrizacion.dto.MwServiciosRegionalesDto;
import com.middleware.parametrizacion.dto.MwServiciosRegionalesUpdateDto;
import com.middleware.parametrizacion.entity.MwServiciosRegionales;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MwServiciosRegionalesMapper {

    MwServiciosRegionales toEntity(MwServiciosRegionalesDto dto);

    MwServiciosRegionalesDto toDto(MwServiciosRegionales entity);

     @Mapping(target = "estado", ignore = true)
    void updateFromDto(MwServiciosRegionalesUpdateDto dto, @MappingTarget MwServiciosRegionales entity);
}
