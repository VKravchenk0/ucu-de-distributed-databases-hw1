package ua.vk.ucu.ddb.hw1.server;

import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/counter/inmemory")
public class InMemoryCounterController {

    private final AtomicLong counter = new AtomicLong(0);

    @PostMapping("/inc")
    public void increment() {
        long newValue = counter.incrementAndGet();
        // log.info("Incrementing counter. New value: {}", newValue);
    }

    @GetMapping("/count")
    public long count() {
        // log.info("Retrieving value");
        return counter.get();
    }
}
