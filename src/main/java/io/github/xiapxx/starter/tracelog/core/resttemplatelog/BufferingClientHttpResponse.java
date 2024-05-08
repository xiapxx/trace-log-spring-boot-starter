package io.github.xiapxx.starter.tracelog.core.resttemplatelog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author xiapeng
 * @Date 2024-05-08 14:28
 */
public class BufferingClientHttpResponse implements ClientHttpResponse {

    private final ClientHttpResponse response;

    private InputStream body;

    private String bodyString;


    public BufferingClientHttpResponse(ClientHttpResponse response) throws IOException {
        this.response = response;
        byte[] bodyArr = StreamUtils.copyToByteArray(this.response.getBody());
        this.body = new ByteArrayInputStream(bodyArr);
        this.bodyString = new String(bodyArr);
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return this.response.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
        return this.body;
    }

    public String getBodyString(){
        return this.bodyString;
    }

    @Override
    public void close() {
        this.response.close();
    }
}
