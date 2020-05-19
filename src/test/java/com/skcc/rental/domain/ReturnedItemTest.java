package com.skcc.rental.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.rental.web.rest.TestUtil;

public class ReturnedItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnedItem.class);
        ReturnedItem returnedItem1 = new ReturnedItem();
        returnedItem1.setId(1L);
        ReturnedItem returnedItem2 = new ReturnedItem();
        returnedItem2.setId(returnedItem1.getId());
        assertThat(returnedItem1).isEqualTo(returnedItem2);
        returnedItem2.setId(2L);
        assertThat(returnedItem1).isNotEqualTo(returnedItem2);
        returnedItem1.setId(null);
        assertThat(returnedItem1).isNotEqualTo(returnedItem2);
    }
}
