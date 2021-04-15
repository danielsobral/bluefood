package com.daniel.bluefood.domain.application.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daniel.bluefood.util.IOUtils;

@Service
public class ImageService {
	
	@Value("${bluefood.files.logotipo}")
	private String logotiposDir;
	
	@Value("${bluefood.files.categoria}")
	private String categoriasDir;
	
	@Value("${bluefood.files.comida}")
	private String comidasDir;

	public void uploadLogotipo(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, logotiposDir);
		} catch (IOException e) {
			
			throw new ApplicationServiceException(e);
		}
	}
	
	public byte[] getBytes(String type, String imgName) {
		
		try {
		String dir;
		
		if ("logotipo".equals(type)) {
			
			dir = logotiposDir;
		} else if ("categoria".equals(type)) {
			dir = categoriasDir;
			
		} else if("comida".equals(type)) {
			
			dir = comidasDir;
		} else {
			
			throw new Exception(type + "Não é um tipo de imagem valido");
		}
		
		return IOUtils.getBytes(Paths.get(dir, imgName));
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}
}
