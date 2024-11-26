package sg.edu.nus.iss.day15_workshop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Day15WorkshopApplication {

	private static String dataDir;

	@Value("${data.dir}")
	private String defaultDataDir;

	public static void main(String[] args) {
		SpringApplication.run(Day15WorkshopApplication.class, args);
	}

	@Bean
	public String dataDir(ApplicationArguments args) {
		
		if (args.containsOption("dataDir")) {
			dataDir = args.getOptionValues("dataDir").get(0);
		} else {
			dataDir = defaultDataDir;
		}

		return dataDir;
	}

	@Bean 
	public CommandLineRunner handleDirectory() {
		return args -> {

			Path dirPath = Paths.get(dataDir);
			
			try {
				if (Files.exists(dirPath)) {
					if(!Files.isDirectory(dirPath)) {
						System.out.println("Error: Path provided is not a directory");
						System.exit(1);
					}

				} else {
					Files.createDirectories(dirPath);
					System.out.println("Directory created at: " + dirPath);
				}
			} catch (IOException e) {
				System.out.println("Error handling directory " + e.getMessage());
				System.exit(1);
			}

		};
	}



}
