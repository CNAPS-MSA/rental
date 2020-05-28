package com.skcc.rental.web.rest.mapper;

import com.skcc.rental.web.rest.mapper.RentedItemMapperImpl;
import com.skcc.rental.web.rest.mapper.RentedItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RentedItemMapperTest {

    private RentedItemMapper rentedItemMapper;

    @BeforeEach
    public void setUp() {
        rentedItemMapper = new RentedItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(rentedItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rentedItemMapper.fromId(null)).isNull();
    }
}
