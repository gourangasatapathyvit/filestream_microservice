package com.ultimateScrapper.stream.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.concurrent.CompletableFuture;

public interface StreamService {

    @GetMapping("/test")
    String test();

    @GetMapping(value = "/play/media/v01/{vid_id}")
    @ResponseBody
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> playMediaV01(
            @PathVariable("vid_id")
            String video_id,
            @RequestHeader(value = "Range", required = false)
            String rangeHeader);

}
