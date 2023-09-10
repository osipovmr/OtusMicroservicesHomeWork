package otus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/")
    public ResponseEntity<String> checkStatus() {
        return ResponseEntity.ok("Hello, user! It is working!");
    }

    @GetMapping("/health")
    public ResponseEntity<Void> helloOtus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}