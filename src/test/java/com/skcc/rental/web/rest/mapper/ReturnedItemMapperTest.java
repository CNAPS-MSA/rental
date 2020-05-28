package com.skcc.rental.web.rest.mapper;

import com.skcc.rental.web.rest.mapper.ReturnedItemMapperImpl;
import com.skcc.rental.web.rest.mapper.ReturnedItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ReturnedItemMapperTest {

    private ReturnedItemMapper returnedItemMapper;

    @BeforeEach
    public void setUp() {
        returnedItemMapper = new ReturnedItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(returnedItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(returnedItemMapper.fromId(null)).isNull();
    }
}
