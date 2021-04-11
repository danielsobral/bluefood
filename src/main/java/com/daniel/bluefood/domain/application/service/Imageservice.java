package com.daniel.bluefood.domain.application.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daniel.bluefood.util.IOUtils;

@Service
public class Imageservice {
	
	@Value("${bluefood.files.logotipo}")
	private String logotiposDir;

	public void uploadLogotipo(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, logotiposDir);
		} catch (IOException e) {
			
			throw new ApplicationServiceException(e);
		}
	}
}
