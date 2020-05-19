package com.skcc.rental.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.rental.web.rest.TestUtil;

public class ReturnedItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnedItemDTO.class);
        ReturnedItemDTO returnedItemDTO1 = new ReturnedItemDTO();
        returnedItemDTO1.setId(1L);
        ReturnedItemDTO returnedItemDTO2 = new ReturnedItemDTO();
        assertThat(returnedItemDTO1).isNotEqualTo(returnedItemDTO2);
        returnedItemDTO2.setId(returnedItemDTO1.getId());
        assertThat(returnedItemDTO1).isEqualTo(returnedItemDTO2);
        returnedItemDTO2.setId(2L);
        assertThat(returnedItemDTO1).isNotEqualTo(returnedItemDTO2);
        returnedItemDTO1.setId(null);
        assertThat(returnedItemDTO1).isNotEqualTo(returnedItemDTO2);
    }
}
