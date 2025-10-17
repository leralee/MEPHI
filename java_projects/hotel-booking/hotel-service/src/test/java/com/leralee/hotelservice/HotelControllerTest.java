package com.leralee.hotelservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leralee.hotelservice.dto.HotelDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author valeriali
 * @project hotel-booking
 */
@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateHotel() throws Exception {
        HotelDto dto = new HotelDto();
        dto.setName("Test Hotel");
        dto.setAddress("123 Street");

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
