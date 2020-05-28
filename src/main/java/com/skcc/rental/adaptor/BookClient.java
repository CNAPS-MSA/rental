package com.skcc.rental.adaptor;

import com.skcc.rental.config.FeignConfiguration;
import com.skcc.rental.web.rest.dto.BookInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
//name="book", url = "localhost:8081/api"
@FeignClient(name= "book", configuration = {FeignConfiguration.class})
public interface BookClient {
    @GetMapping("/api/getBookInfo/{bookIds}")
    List<BookInfo> getBookInfo(@PathVariable("bookIds") List<Long> bookIds);
}
