package com.ultimateScrapper.stream.streamUtility;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Service
public class UtilityServiceImpl implements UtilityService {
    public static final String VIDURL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";

    @Async("asyncTaskExecutor")
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> streamHandler(String video_id, String rangeHeader) {
        try {
            StreamingResponseBody responseStream;

            URL url = new URL(VIDURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            Long fileSize = connection.getContentLengthLong();
            final HttpHeaders responseHeaders = new HttpHeaders();

            if (rangeHeader == null) {
                responseHeaders.add("Content-Type", "video/mp4");
                responseHeaders.add("Content-Length", fileSize.toString());
                InputStream inputStream = connection.getInputStream();
                responseStream = outputStream -> {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    inputStream.close();
                };

                return CompletableFuture.completedFuture(new ResponseEntity<>(responseStream, responseHeaders, HttpStatus.OK));
            }

            String[] ranges = rangeHeader.substring(6).split("-");
            Long rangeStart = Long.parseLong(ranges[0]);
            Long rangeEnd = (ranges.length > 1) ? Long.parseLong(ranges[1]) : fileSize - 1;
            rangeEnd = Math.min(rangeEnd, (fileSize - 1));

            long contentLength = rangeEnd - rangeStart + 1;
            String contentRange = String.format("bytes %d-%d/%d", rangeStart, rangeEnd, fileSize);

            responseHeaders.add("Content-Type", "video/mp4");
            responseHeaders.add("Content-Length", String.valueOf(contentLength));
            responseHeaders.add("Accept-Ranges", "bytes");
            responseHeaders.add("Content-Range", contentRange);

            HttpURLConnection rangeConnection = (HttpURLConnection) url.openConnection();
            rangeConnection.setRequestProperty("Range", "bytes=" + rangeStart + "-" + rangeEnd);
            InputStream inputStream = rangeConnection.getInputStream();

            responseStream = outputStream -> {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                inputStream.close();
            };

            // Return CompletableFuture with partial content and headers
            return CompletableFuture.completedFuture(new ResponseEntity<>(responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT));

        } catch (IOException e) {
            CompletableFuture<ResponseEntity<StreamingResponseBody>> future = new CompletableFuture<>();
            future.complete(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            return future;
        }
    }
}
