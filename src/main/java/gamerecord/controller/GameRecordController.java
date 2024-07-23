package gamerecord.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class GameRecordController {
    private final String DATA_DIR = "/Users/hyungjuncho/Documents/semester_07/capstone_01/vacation_work/work_01/CheckGameOdds/data/input";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/api/gamerecord/users")
    public ResponseEntity<?> getUsers() {
        try {
            // records.json 파일을 읽어서 자바 객체로 변환하는 과정
            List<Map<String, Object>> users = objectMapper.readValue(
                    Files.readAllBytes(Paths.get(DATA_DIR, "records.json")),
                    new TypeReference<>() {
                    });
            users = users.stream()
                    .sorted(Comparator.comparing((Map<String, Object> user) -> (String) user.get("username"))
                            .thenComparing(user -> (String) user.get("tag")))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(users);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/gamerecord/winrate")
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "data not found"));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
