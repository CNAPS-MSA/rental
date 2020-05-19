package com.skcc.rental.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.rental.web.rest.TestUtil;

public class RentalDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentalDTO.class);
        RentalDTO rentalDTO1 = new RentalDTO();
        rentalDTO1.setId(1L);
        RentalDTO rentalDTO2 = new RentalDTO();
        assertThat(rentalDTO1).isNotEqualTo(rentalDTO2);
        rentalDTO2.setId(rentalDTO1.getId());
        assertThat(rentalDTO1).isEqualTo(rentalDTO2);
        rentalDTO2.setId(2L);
        assertThat(rentalDTO1).isNotEqualTo(rentalDTO2);
        rentalDTO1.setId(null);
        assertThat(rentalDTO1).isNotEqualTo(rentalDTO2);
    }
}
