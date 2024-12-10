package com.example.acmecorporation.repository;

import com.example.acmecorporation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
