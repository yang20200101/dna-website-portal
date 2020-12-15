package com.highershine.portal.handlder;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomerResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // 这里返回false
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }

}