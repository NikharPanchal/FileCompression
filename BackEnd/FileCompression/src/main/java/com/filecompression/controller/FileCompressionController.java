package com.filecompression.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.filecompression.service.FileCompressionService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileCompressionController {

	@Autowired
	private FileCompressionService compressionService;

	private static final Logger log = LoggerFactory.getLogger(FileCompressionController.class);

	@GetMapping("/test")
	public String hello() {
		return "welcome to file compression";
	}

	@PostMapping("/upload")
	public void compressFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		try {
			if (file != null && file.getSize() > 0) {
				log.info("User file name :: {} , size :: {}  ", file.getOriginalFilename(), file.getSize());
				byte[] zipContent = compressionService.addContentToZipFile(file.getInputStream(),
						file.getOriginalFilename());
				if (zipContent != null && zipContent.length > 0) {
					compressionService.saveZipFile(zipContent, file.getOriginalFilename(), response);
				} else {
					log.error("Zip content is null or empty");
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Zip content is null or empty");
				}
			} else {
				log.error("File not uploaded or it's empty");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File not uploaded or it's empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in compress file. message :: {}", e.getMessage());
		}
	}
}
