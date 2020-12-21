package com.highershine.portal.handlder;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * restTemplate调用check_toke接口，token错误或失效，回返回400，抛出异常
 * 我们不想要抛异常，想让我们自定义的AuthExceptionEntryPoint去处理
 * 所以重写了hasError方法
 */
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