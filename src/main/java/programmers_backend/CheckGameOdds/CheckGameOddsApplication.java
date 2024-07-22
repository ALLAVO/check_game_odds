package programmers_backend.CheckGameOdds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@SpringBootApplication
public class CheckGameOddsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckGameOddsApplication.class, args);
	}

	@RestController
	public static class ApiController {
//		private final String DATA_DIR = "../data/input";
		private final String DATA_DIR = "/Users/hyungjuncho/Documents/semester_07/capstone_01/vacation_work/work_01/CheckGameOdds/data/input"; // 절대 경로 사용
		private final ObjectMapper objectMapper = new ObjectMapper();

		@GetMapping("/api/gamerecord/users")
		// 여기에 코드를 작성하세요.
		public ResponseEntity<?> getUsers() {
			try {
				List<Map<String, Object>> users = objectMapper.readValue(
						Files.readAllBytes(Paths.get(DATA_DIR, "records.json")),
						new TypeReference<>() {
						});
				// 불러온 user 정보에 대한 정렬 => username, tag 순으로 오름차순
				// username과 tag로 정렬
				users = users.stream()
						.sorted(Comparator.comparing((Map<String, Object> user) -> (String) user.get("username"))
								.thenComparing(user -> (String) user.get("tag")))
						.collect(Collectors.toList());
				System.out.println(users);
				return ResponseEntity.ok(users);
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}

		@GetMapping("/api/gamerecord/winrate")
		// 여기에 코드를 작성하세요.
		public ResponseEntity<?> getWinrate(@RequestParam String username, @RequestParam String tag) {
			if (username == null || tag == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid data format"));
			}
			try {
				List<Map<String, Object>> users = objectMapper.readValue(
						Files.readAllBytes(Paths.get(DATA_DIR, "records.json")),
						new TypeReference<>() {
						});
				for (Map<String, Object> user : users) {
					if (user.get("username").equals(username) && user.get("tag").equals(tag)) {
						int win = (Integer) user.get("win");
						int lose = (Integer) user.get("lose");
						int winrate = (win * 100) / (win + lose);
						return ResponseEntity.ok(Collections.singletonMap("winrate", winrate));
					}
				}

				System.out.println(users);
				// return ResponseEntity.ok("Hello, world!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "data not found"));
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
	}
}