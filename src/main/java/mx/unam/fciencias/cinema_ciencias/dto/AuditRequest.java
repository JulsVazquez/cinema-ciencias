package mx.unam.fciencias.cinema_ciencias.dto;

public record AuditRequest(
        Long userId,
        String action,
        String details
) {}