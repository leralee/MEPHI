package com.leralee.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leralee.bookingservice.dto.BookingDto;
import com.leralee.bookingservice.dto.RoomDto;
import com.leralee.bookingservice.service.HotelResilientClient;
import com.leralee.bookingservice.entity.User;
import com.leralee.bookingservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

/**
 * @author valeriali
 * @project hotel-booking
 */

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelResilientClient hotelClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void shouldCreateBooking() throws Exception {
        User u = User.builder().username("user").password(passwordEncoder.encode("pwd")).role("USER").build();
        userRepository.save(u);

        BookingDto dto = new BookingDto();
        dto.setHotelId(1L);
        dto.setIdempotencyKey("test-key");
        dto.setStartDate(java.time.LocalDate.now().plusDays(1));
        dto.setEndDate(java.time.LocalDate.now().plusDays(2));

        RoomDto allocated = new RoomDto();
        allocated.setId(42L);
        allocated.setHotelId(1L);
        when(hotelClient.allocateRoom(1L)).thenReturn(allocated);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("X-Internal-Secret", "gateway-approved")
                        .header("Idempotency-Key", "test-key"))
                .andExpect(status().isCreated());
    }
}
