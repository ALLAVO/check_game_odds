package programmers_backend.CheckGameOdds;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class CheckGameOddsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckGameOddsApplication.class, args);
	}

	@RestController
	public static class ApiController {
		private final String DATA_DIR = "../data/input";
		private final ObjectMapper objectMapper = new ObjectMapper();

		@GetMapping("/api/gamerecord/users")
		// 여기에 코드를 작성하세요.
		public ResponseEntity<?> getUsers() {
			try {
				List<Map<String, Object>> users = objectMapper.readValue(
						Files.readAllBytes(Paths.get(DATA_DIR, "records.json")),
						new TypeReference<>() {
						});
				System.out.println(users);
				return ResponseEntity.ok("Hello, world!");
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}

		@GetMapping("/api/gamerecord/winrate")
		// 여기에 코드를 작성하세요.
		public ResponseEntity<?> getWinrate() {
			try {
				List<Map<String, Object>> users = objectMapper.readValue(
						Files.readAllBytes(Paths.get(DATA_DIR, "records.json")),
						new TypeReference<>() {
						});
				System.out.println(users);
				return ResponseEntity.ok("Hello, world!");
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
	}
}