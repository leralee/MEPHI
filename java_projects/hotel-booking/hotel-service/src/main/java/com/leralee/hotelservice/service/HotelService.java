package com.leralee.hotelservice.service;

import com.leralee.hotelservice.dto.HotelDto;
import com.leralee.hotelservice.entity.Hotel;
import com.leralee.hotelservice.mapper.HotelMapper;
import com.leralee.hotelservice.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public List<HotelDto> findAll() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toDto)
                .toList();
    }

    public HotelDto create(HotelDto dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toDto(saved);
    }

    public HotelDto update(Long id, HotelDto dto) {
        Hotel existing = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());

        return hotelMapper.toDto(hotelRepository.save(existing));
    }

    public void delete(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new IllegalArgumentException("Hotel not found");
        }
        hotelRepository.deleteById(id);
    }

}
