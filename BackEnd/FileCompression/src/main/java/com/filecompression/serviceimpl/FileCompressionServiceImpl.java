package com.filecompression.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import com.filecompression.service.FileCompressionService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileCompressionServiceImpl implements FileCompressionService {

	@Override
	public byte[] addContentToZipFile(InputStream inputStream, String fileName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
			ZipEntry entry = new ZipEntry(fileName);
			zipOutputStream.putNextEntry(entry);
			IOUtils.copy(inputStream, zipOutputStream);
			zipOutputStream.closeEntry();
			zipOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in addContentToZipFile. message :: {}", e.getMessage());
		}
		return outputStream.toByteArray();
	}

	@Override
	public void saveZipFile(byte[] zipContent, String filename, HttpServletResponse response) {
		try {
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".zip");
			response.getOutputStream().write(zipContent);
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in saveZipFile. message :: {}", e.getMessage());
		}
	}
}
