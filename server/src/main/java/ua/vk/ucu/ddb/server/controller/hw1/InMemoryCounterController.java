package ua.vk.ucu.ddb.server.controller.hw1;

import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/counter/in-memory")
public class InMemoryCounterController {

    private final AtomicLong counter = new AtomicLong(0);

    @PostMapping
    public void increment() {
        counter.incrementAndGet();
    }

    @GetMapping
    public long count() {
        return counter.get();
    }

    @DeleteMapping
    public void reset() throws IOException {
        counter.set(0);
    }
}
