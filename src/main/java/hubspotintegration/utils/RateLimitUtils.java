package hubspotintegration.utils;

import hubspotintegration.exception.HubSpotException;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class RateLimitUtils {
        private static final int MAX_REQUESTS = 110;
    private static final Duration WINDOW = Duration.ofSeconds(10);
    private static final Queue<Long> timestamps = new ConcurrentLinkedQueue<>();

    public synchronized static void validateRateLimit() {
        long now = System.currentTimeMillis();
        long windowStart = now - WINDOW.toMillis();

        while (!timestamps.isEmpty() && timestamps.peek() < windowStart) {
            timestamps.poll();
        }

        if (timestamps.size() >= MAX_REQUESTS) {
            long oldest = timestamps.peek();
            long waitMillis = (oldest + WINDOW.toMillis()) - now;
            long waitSeconds = TimeUnit.MILLISECONDS.toSeconds(waitMillis) + 1;
            throw new HubSpotException("Erro ao comunicar com API externa",
                    "Rate limit atingido. Aguarde " + waitSeconds + " segundos antes de tentar novamente.",
                    HttpStatus.TOO_MANY_REQUESTS);
        }

        timestamps.add(now);
    }
}
