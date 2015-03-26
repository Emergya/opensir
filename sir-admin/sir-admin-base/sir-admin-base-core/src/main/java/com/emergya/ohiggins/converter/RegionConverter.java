package com.emergya.ohiggins.converter;

import java.util.ArrayList;
import java.util.List;

import com.emergya.ohiggins.dto.RegionDto;
import com.emergya.ohiggins.model.RegionEntity;

public class RegionConverter {

	public static RegionDto entityToDto(final RegionEntity entity) {

		if (entity == null) {
			return null;
		}

		RegionDto dto = new RegionDto();
		dto.setId(entity.getId());
		dto.setName_region(entity.getName_region());
		dto.setPrefix_wks(entity.getPrefix_wks());
		dto.setNode_analytics(entity.getNode_analytics());
		dto.setNode_publicacion(entity.getNode_publicacion());

		return dto;

	}

	public static List<RegionDto> entityToDtoList(
			final List<RegionEntity> entityList) {
		List<RegionDto> result = new ArrayList<RegionDto>();

		if (entityList != null) {
			for (RegionEntity re : entityList) {
				result.add(RegionConverter.entityToDto(re));
			}
		}

		return result;
	}

	public static RegionEntity dtoToEntity(RegionDto dto) {

		RegionEntity entity = null;

		if (dto != null) {
			entity = new RegionEntity();
			entity.setId(dto.getId());
			entity.setName_region(dto.getName_region());
			entity.setPrefix_wks(dto.getPrefix_wks());
			entity.setNode_analytics(dto.getNode_analytics());
			entity.setNode_publicacion(dto.getNode_publicacion());
		}
		return entity;
	}
}
