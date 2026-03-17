package org.example.unicare_backend.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();

    private Bucket createNewBucket(String key) {
        // 10 requests per 5 minutes
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(5)));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket resolveBucket(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        return cache.get(ipAddress, this::createNewBucket);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/auth")) {
            Bucket bucket = resolveBucket(request);
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
            if (probe.isConsumed()) {
                response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
                filterChain.doFilter(request, response);
            } else {
                long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
                response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "You have exhausted your API Request Quota");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
