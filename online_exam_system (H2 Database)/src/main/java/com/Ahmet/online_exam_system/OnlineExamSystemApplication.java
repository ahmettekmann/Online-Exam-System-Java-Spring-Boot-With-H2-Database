package com.Ahmet.online_exam_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class OnlineExamSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineExamSystemApplication.class, args);
		openHomePage();

	}

	public static void openHomePage() {
		try {
			String url = "http://localhost:8080/login";
			String os = System.getProperty("os.name").toLowerCase();
			String command = "";

			if (os.contains("win")) {
				command = "rundll32 url.dll,FileProtocolHandler " + url;
			} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
				command = "xdg-open " + url;
			} else {
				System.err.println("Unsupported operating system.");
				return;
			}
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
