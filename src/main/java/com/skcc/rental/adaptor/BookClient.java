package com.skcc.rental.adaptor;

import com.skcc.rental.config.FeignConfiguration;
import com.skcc.rental.web.rest.dto.BookInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "book", configuration = {FeignConfiguration.class})
public interface BookClient {
    @GetMapping("/api/getBookInfo/{bookIds}/{userid}")
    ResponseEntity<List<BookInfoDTO>> getBookInfo(@PathVariable("bookIds") List<Long> bookIds, @PathVariable("userid") Long userid);
}
