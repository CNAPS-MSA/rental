package com.skcc.rental.web.rest.errors;


import feign.Response;
import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;

public class FeignClientExceptionErrorDecoder implements ErrorDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientExceptionErrorDecoder.class);
    private StringDecoder stringDecoder = new StringDecoder();

    @Override
    public  FeignClientException decode(final String methodKey, Response response) {
        String message = "Null Response Body.";
        if (response.body() != null) {
            try {
                JSONObject jsonObject = new JSONObject(response.body().toString());
                message = jsonObject.getString("message");
                message = stringDecoder.decode(response, String.class).toString();
            } catch (IOException | JSONException e) {
                LOGGER.error(methodKey + "Error Deserializing response body from failed feign request response.", e);
            }
        }
        return new FeignClientException(response.status(), message, response.headers());
    }
}
