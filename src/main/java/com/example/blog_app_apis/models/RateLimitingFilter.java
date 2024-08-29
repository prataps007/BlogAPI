package com.example.blog_app_apis.models;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitingFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, RequestData> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        RequestData requestData = requestCounts.getOrDefault(clientIP, new RequestData(0, currentTime));

        // 1 minute
        long TIME_FRAME = 60 * 1000;   // 1 minute time - frame

        if (currentTime - requestData.getTimestamp() > TIME_FRAME) {  // out of time frame
            requestData.setTimestamp(currentTime);
            requestData.setCount(1);
        } else {
            int MAX_REQUESTS = 10;     // maximum 10 request in 1 minute

            if (requestData.getCount() < MAX_REQUESTS) {
                requestData.incrementCount();
            } else {
                response.setStatus(429);  // TOO_MANY_REQUESTS
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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void incrementCount() {
            this.count++;
        }
    }
}
