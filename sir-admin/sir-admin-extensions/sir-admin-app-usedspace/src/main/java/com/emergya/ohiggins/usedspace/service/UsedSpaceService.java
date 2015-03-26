package com.emergya.ohiggins.usedspace.service;

import java.util.List;

import com.emergya.ohiggins.usedspace.dto.UsedSpaceDto;


public interface UsedSpaceService {
	
	public List<UsedSpaceDto> getLayersSpaceByAuthority(long authId);

}
