package com.skcc.rental.web.rest.mapper;

import com.skcc.rental.web.rest.mapper.OverdueItemMapperImpl;
import com.skcc.rental.web.rest.mapper.OverdueItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OverdueItemMapperTest {

    private OverdueItemMapper overdueItemMapper;

    @BeforeEach
    public void setUp() {
        overdueItemMapper = new OverdueItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(overdueItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(overdueItemMapper.fromId(null)).isNull();
    }
}
