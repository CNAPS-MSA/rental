package com.skcc.rental.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.rental.web.rest.TestUtil;

public class RentedItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentedItem.class);
        RentedItem rentedItem1 = new RentedItem();
        rentedItem1.setId(1L);
        RentedItem rentedItem2 = new RentedItem();
        rentedItem2.setId(rentedItem1.getId());
        assertThat(rentedItem1).isEqualTo(rentedItem2);
        rentedItem2.setId(2L);
        assertThat(rentedItem1).isNotEqualTo(rentedItem2);
        rentedItem1.setId(null);
        assertThat(rentedItem1).isNotEqualTo(rentedItem2);
    }
}
