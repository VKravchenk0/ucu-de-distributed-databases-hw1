package ua.vk.ucu.ddb.hw1.server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/counter/file-based")
public class FileBasedCounterController {

    private static final Path FILE_PATH = Paths.get(String.format("counter-%d.txt", System.currentTimeMillis()));

    private final ReentrantLock lock = new ReentrantLock();

    @PostMapping
    public void increment() throws IOException {
        lock.lock();
        try {
            long value = getCounterValue();
            writeCounterValue(value + 1);
        } finally {
            lock.unlock();
        }
    }

    @GetMapping
    public long count() throws IOException {
        lock.lock();
        try {
            return getCounterValue();
        } finally {
            lock.unlock();
        }
    }

    @DeleteMapping
    public void reset() throws IOException {
        lock.lock();
        try {
            writeCounterValue(0);
        } finally {
            lock.unlock();
        }
    }

    private long getCounterValue() throws IOException {
        if (!Files.exists(FILE_PATH)) {
            return 0;
        }
        return Long.parseLong(Files.readString(FILE_PATH));
    }

    private void writeCounterValue(long value) throws IOException {
        Files.writeString(FILE_PATH, Long.toString(value),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
