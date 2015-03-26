package com.emergya.ohiggins.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.emergya.ohiggins.dao.ChileindicaInversionDataDao;
import com.emergya.ohiggins.dto.ChileindicaInversionDataDto;
import com.emergya.ohiggins.dto.ChileindicaInversionFinanciamientoDataDto;
import com.emergya.ohiggins.dto.ChileindicaRelacionIntrumentosDataDto;
import com.emergya.ohiggins.model.ChileindicaInversionDataEntity;
import com.emergya.ohiggins.model.ChileindicaInversionFinanciamientoDataEntity;
import com.emergya.ohiggins.model.ChileindicaRelacionIntrumentosDataEntity;
import com.emergya.ohiggins.service.ChileindicaInversionDataService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Service
public class ChileindicaInversionDataServiceImpl
		extends
		AbstractServiceImpl<ChileindicaInversionDataDto, ChileindicaInversionDataEntity>
		implements ChileindicaInversionDataService {

	@Resource
	private ChileindicaInversionDataDao dao;

	@Override
	protected GenericDAO<ChileindicaInversionDataEntity, Long> getDao() {
		return this.dao;
	}

	@Override
	protected ChileindicaInversionDataDto entityToDto(
			ChileindicaInversionDataEntity entity) {
		if (entity == null) {
			return null;
		}
		ChileindicaInversionDataDto dto = new ChileindicaInversionDataDto();
		BeanUtils.copyProperties(entity, dto);

		// Copy nested properties
		List<ChileindicaInversionFinanciamientoDataDto> financiamientos = new ArrayList<ChileindicaInversionFinanciamientoDataDto>();
		if (entity.getFinanciamientosList() != null) {
			for (ChileindicaInversionFinanciamientoDataEntity financiamietoEntity : entity
					.getFinanciamientosList()) {
				ChileindicaInversionFinanciamientoDataDto financiamientoDto = new ChileindicaInversionFinanciamientoDataDto();
				BeanUtils.copyProperties(financiamietoEntity,
						financiamientoDto, new String[] { "inversionData" });
				financiamientoDto.setInversionData(dto);
				financiamientos.add(financiamientoDto);
			}
		}
		dto.setFinanciamientosList(financiamientos);

		List<ChileindicaRelacionIntrumentosDataDto> relacionInstrumentos = new ArrayList<ChileindicaRelacionIntrumentosDataDto>();
		if (entity.getRelacionInstrumentosList() != null) {
			for (ChileindicaRelacionIntrumentosDataEntity relacion : entity
					.getRelacionInstrumentosList()) {
				ChileindicaRelacionIntrumentosDataDto relacionDto = new ChileindicaRelacionIntrumentosDataDto();
				BeanUtils.copyProperties(relacion, relacionDto,
						new String[] { "inversionData" });
				relacionDto.setInversionData(dto);
				relacionInstrumentos.add(relacionDto);
			}
		}
		dto.setRelacionInstrumentosList(relacionInstrumentos);

		return dto;
	}

	@Override
	protected ChileindicaInversionDataEntity dtoToEntity(
			ChileindicaInversionDataDto dto) {
		ChileindicaInversionDataEntity entity = new ChileindicaInversionDataEntity();
		BeanUtils.copyProperties(dto, entity);

		// Copy lists
		List<ChileindicaInversionFinanciamientoDataEntity> financiamientosList = new ArrayList<ChileindicaInversionFinanciamientoDataEntity>();
		if (dto.getFinanciamientosList() != null) {
			for (ChileindicaInversionFinanciamientoDataDto financiamientoDto : dto
					.getFinanciamientosList()) {
				financiamientosList.add(dtoToEntity(financiamientoDto, entity));
			}
		}
		entity.setFinanciamientosList(financiamientosList);

		// Copy lists
		List<ChileindicaRelacionIntrumentosDataEntity> relacionesList = new ArrayList<ChileindicaRelacionIntrumentosDataEntity>();
		if (dto.getRelacionInstrumentosList() != null) {
			for (ChileindicaRelacionIntrumentosDataDto relacionDto : dto
					.getRelacionInstrumentosList()) {
				relacionesList.add(dtoToEntity(relacionDto, entity));
			}
		}
		entity.setRelacionInstrumentosList(relacionesList);

		return entity;
	}

	protected ChileindicaInversionFinanciamientoDataEntity dtoToEntity(
			ChileindicaInversionFinanciamientoDataDto dto,
			ChileindicaInversionDataEntity inversionEntity) {
		ChileindicaInversionFinanciamientoDataEntity entity = new ChileindicaInversionFinanciamientoDataEntity();
		BeanUtils.copyProperties(dto, entity, new String[] { "inversionData" });
		entity.setInversionData(inversionEntity);
		return entity;
	}

	protected ChileindicaRelacionIntrumentosDataEntity dtoToEntity(
			ChileindicaRelacionIntrumentosDataDto dto,
			ChileindicaInversionDataEntity inversionEntity) {
		ChileindicaRelacionIntrumentosDataEntity entity = new ChileindicaRelacionIntrumentosDataEntity();
		BeanUtils.copyProperties(dto, entity, new String[] { "inversionData" });
		entity.setInversionData(inversionEntity);
		return entity;
	}

	@Override
	public boolean checkIfProjectMustBeUpdated(Integer region, Integer ano,
			Integer cInstitucion, Integer cPreinversion, Integer cFicha,
			Integer fechaRegistro) {
		Date fechaRegistroDate = getValidDate(fechaRegistro);
		if (fechaRegistroDate == null) {
			return true;
		}

		ChileindicaInversionDataEntity example = new ChileindicaInversionDataEntity();
		example.setRegion(region);
		example.setAno(ano);
		example.setCInstitucion(cInstitucion);
		example.setCPreinversion(cPreinversion);
		example.setCFicha(cFicha);

		List<ChileindicaInversionDataEntity> inversionList = getDao()
				.findByExample(example, new String[] {});
		boolean result = false;

		if (inversionList.size() == 0
				|| inversionList.get(0).getFechaRegistroChileindica() == null
				|| inversionList.get(0).getFechaRegistroChileindica()
						.before(fechaRegistroDate)) {
			result = true;
		}
		return result;
	}

	@Override
	public Date getValidDate(Integer dateString) {
		Date result = null;
		if (dateString == null) {
			return result;
		}
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			df.setLenient(false);
			result = df.parse(dateString.toString());
		} catch (ParseException ex) {
			return null;
		}

		return result;
	}

	@Override
	public ChileindicaInversionDataDto findProjectByBussinessKey(Integer ano,
			Integer region, Integer cInstitucion, Integer cPreinversion,
			Integer cFicha) {
		ChileindicaInversionDataDto result = null;
		ChileindicaInversionDataEntity example = new ChileindicaInversionDataEntity();
		example.setAno(ano);
		example.setRegion(region);
		example.setCInstitucion(cInstitucion);
		example.setCPreinversion(cPreinversion);
		example.setCFicha(cFicha);

		List<ChileindicaInversionDataEntity> results = dao.findByExample(
				example, new String[] {});
		if (results.size() > 0) {
			result = entityToDto(results.get(0));
		}

		return result;
	}

	@Override
	public List<Long> getAllProjectDbIds() {
		return dao.getAllIds();

	}

	@Override
	public void deleteById(Long id) {
		dao.deleteById(id);

	}

}
