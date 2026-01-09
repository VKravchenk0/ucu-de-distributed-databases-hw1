package ua.vk.ucu.ddb.hw1.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {

    private static final String COUNTER_URL_TEMPLATE = "http://localhost:8080/counter/%s";
    private static final String INC_URL_TEMPLATE = COUNTER_URL_TEMPLATE + "/inc";
    private static final String COUNT_URL_TEMPLATE = COUNTER_URL_TEMPLATE + "/count";

    public static void main(String[] args) throws Exception {
        String counterType = String.valueOf(args[0]);
        int parallelClients = Integer.parseInt(args[1]);
        int requestsPerClient = Integer.parseInt(args[2]);

        HttpClient client = HttpClient.newHttpClient();
        String incrementUrl = INC_URL_TEMPLATE.formatted(counterType);
        log.info("Increment url: {}", incrementUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(incrementUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        long start = System.nanoTime();

        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < parallelClients; i++) {
                tasks.add(() -> {
                    for (int j = 0; j < requestsPerClient; j++) {
                        client.send(request, java.net.http.HttpResponse.BodyHandlers.discarding());
                    }
                    return null;
                });
            }

            executor.invokeAll(tasks);
        }

        double elapsedSeconds =
                (System.nanoTime() - start) / 1_000_000_000.0;

        int totalRequests = parallelClients * requestsPerClient;

        log.info("Total requests: " + totalRequests);
        log.info("Elapsed time: {} sec", String.format("%.3f", elapsedSeconds));
        log.info("Throughput: {} req/sec",
                String.format("%.2f", totalRequests / elapsedSeconds));

        HttpRequest countRequest = HttpRequest.newBuilder()
                .uri(URI.create(COUNT_URL_TEMPLATE.formatted(counterType)))
                .GET()
                .build();
        HttpResponse<String> countResponse = client.send(
                countRequest,
                HttpResponse.BodyHandlers.ofString()
        );

        log.info("Final counter value: {}", countResponse.body());

        
    }
}