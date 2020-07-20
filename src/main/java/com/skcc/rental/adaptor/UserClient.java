package com.skcc.rental.adaptor;


import com.skcc.rental.config.FeignConfiguration;
import com.skcc.rental.web.rest.dto.LatefeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name= "gateway", configuration = {FeignConfiguration.class})
public interface UserClient {
    @PutMapping("/api/usepoints")
    ResponseEntity usePoint(@RequestBody LatefeeDTO latefeeDTO);
}
