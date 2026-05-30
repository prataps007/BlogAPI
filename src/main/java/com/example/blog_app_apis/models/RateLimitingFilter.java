package com.example.blog_app_apis.models;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RateLimitingFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_SIZE_MS = 60 * 1000L;

    public RateLimitingFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        String key = "rate_limit:" + clientIP;
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - WINDOW_SIZE_MS;

        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // remove entries outside the sliding window
        zSetOps.removeRangeByScore(key, 0, windowStart);

        Long requestCount = zSetOps.zCard(key);
        if (requestCount != null && requestCount >= MAX_REQUESTS) {
            response.setStatus(429);
            response.getWriter().write("Too many requests - Rate limit exceeded");
            return;
        }

        // record this request; expire key to auto-clean inactive IPs
        zSetOps.add(key, UUID.randomUUID().toString(), currentTime);
        redisTemplate.expire(key, WINDOW_SIZE_MS, TimeUnit.MILLISECONDS);

        filterChain.doFilter(request, response);
    }

    // ---- OLD: fixed-window in-memory counter (replaced by Redis sliding window above) ----
    /*
    private final java.util.concurrent.ConcurrentHashMap<String, RequestData> requestCounts = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        RequestData requestData = requestCounts.getOrDefault(clientIP, new RequestData(0, currentTime));

        long TIME_FRAME = 60 * 1000;

        if (currentTime - requestData.getTimestamp() > TIME_FRAME) {
            requestData.setTimestamp(currentTime);
            requestData.setCount(1);
        } else {
            int MAX_REQUESTS = 10;

            if (requestData.getCount() < MAX_REQUESTS) {
                requestData.incrementCount();
            } else {
                response.setStatus(429);
                response.getWriter().write("Too many requests - Rate limit exceeded");
                return;
            }
        }
        requestCounts.put(clientIP, requestData);
        filterChain.doFilter(request, response);
    }

    private static class RequestData {
        private int count;
        private long timestamp;

        public RequestData(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }

        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public void incrementCount() { this.count++; }
    }
    */
    // ---- END OLD ----
}
