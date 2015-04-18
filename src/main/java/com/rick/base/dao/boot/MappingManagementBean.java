package com.rick.base.dao.boot;

import java.util.List;

import javax.annotation.PostConstruct;

public class MappingManagementBean {
	
	private List<String> packagesToScan;
	
	private List<String> mappingDirectoryLocations;

	public List<String> getMappingDirectoryLocations() {
		return mappingDirectoryLocations;
	}

	public void setMappingDirectoryLocations(List<String> mappingDirectoryLocations) {
		this.mappingDirectoryLocations = mappingDirectoryLocations;
	}

	public List<String> getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	
	@PostConstruct
	public void init() throws Exception {
		SQLReader.getAllSQL(mappingDirectoryLocations);
		EntityReader.scanningCandidateEntity(packagesToScan);
	}
}
