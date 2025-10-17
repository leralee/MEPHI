package com.leralee.hotelservice.controller;

import com.leralee.hotelservice.dto.HotelDto;
import com.leralee.hotelservice.dto.RoomDto;
import com.leralee.hotelservice.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author valeriali
 * @project hotel-booking
 */

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public List<HotelDto> getAllHotels() {
        return hotelService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelDto createHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.create(hotelDto);
    }

    @PutMapping("/{id}")
    public HotelDto updateHotel(@PathVariable Long id, @RequestBody HotelDto dto) {
        return hotelService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable Long id) {
        hotelService.delete(id);
    }




}
