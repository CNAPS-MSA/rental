package com.skcc.rental.web.rest.mapper;

import com.skcc.rental.web.rest.mapper.RentalMapperImpl;
import com.skcc.rental.web.rest.mapper.RentalMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RentalMapperTest {

    private RentalMapper rentalMapper;

    @BeforeEach
    public void setUp() {
        rentalMapper = new RentalMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(rentalMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rentalMapper.fromId(null)).isNull();
    }
}
