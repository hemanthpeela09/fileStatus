package com.example.fileStatus;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileStatusApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(FileStatusApplication.class, args);

		CSVParser parser = new CSVParser();
		System.out.println(parser.getFileDetails());
	}

}
