package com.ultimateScrapper.stream.ServiceImpl;

import com.ultimateScrapper.stream.Services.StreamService;
import com.ultimateScrapper.stream.streamUtility.UtilityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/stream")
public class StreamServiceImpl implements StreamService {

    private final UtilityService utilityService;

    public StreamServiceImpl(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @Override
    public String test() {
        return "lorem";
    }

    @Override
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> playMediaV01(String video_id, String rangeHeader) {
        return utilityService.streamHandler(video_id, rangeHeader);

    }
}
