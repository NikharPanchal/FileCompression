package com.filecompression.service;

import java.io.InputStream;

import jakarta.servlet.http.HttpServletResponse;

public interface FileCompressionService {

	public byte[] addContentToZipFile(InputStream inputStream, String fileName);
	
	public void saveZipFile(byte[] zipContent, String filename, HttpServletResponse response);
}
