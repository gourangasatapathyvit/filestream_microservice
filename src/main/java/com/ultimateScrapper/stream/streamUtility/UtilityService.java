package com.ultimateScrapper.stream.streamUtility;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.concurrent.CompletableFuture;

public interface UtilityService {
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> streamHandler(String video_id, String rangeHeader);
}
