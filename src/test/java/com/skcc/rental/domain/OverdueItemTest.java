package com.skcc.rental.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.rental.web.rest.TestUtil;

public class OverdueItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OverdueItem.class);
        OverdueItem overdueItem1 = new OverdueItem();
        overdueItem1.setId(1L);
        OverdueItem overdueItem2 = new OverdueItem();
        overdueItem2.setId(overdueItem1.getId());
        assertThat(overdueItem1).isEqualTo(overdueItem2);
        overdueItem2.setId(2L);
        assertThat(overdueItem1).isNotEqualTo(overdueItem2);
        overdueItem1.setId(null);
        assertThat(overdueItem1).isNotEqualTo(overdueItem2);
    }
}
