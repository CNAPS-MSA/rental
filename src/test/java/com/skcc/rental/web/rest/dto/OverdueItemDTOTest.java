package com.skcc.rental.web.rest.dto;

import com.skcc.rental.web.rest.dto.OverdueItemDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.rental.web.rest.TestUtil;

public class OverdueItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OverdueItemDTO.class);
        OverdueItemDTO overdueItemDTO1 = new OverdueItemDTO();
        overdueItemDTO1.setId(1L);
        OverdueItemDTO overdueItemDTO2 = new OverdueItemDTO();
        assertThat(overdueItemDTO1).isNotEqualTo(overdueItemDTO2);
        overdueItemDTO2.setId(overdueItemDTO1.getId());
        assertThat(overdueItemDTO1).isEqualTo(overdueItemDTO2);
        overdueItemDTO2.setId(2L);
        assertThat(overdueItemDTO1).isNotEqualTo(overdueItemDTO2);
        overdueItemDTO1.setId(null);
        assertThat(overdueItemDTO1).isNotEqualTo(overdueItemDTO2);
    }
}
