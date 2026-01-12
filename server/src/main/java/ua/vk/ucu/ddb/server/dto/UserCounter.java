package ua.vk.ucu.ddb.server.dto;

public record UserCounter(Long userId, Long counter, Integer version) {
    
}
