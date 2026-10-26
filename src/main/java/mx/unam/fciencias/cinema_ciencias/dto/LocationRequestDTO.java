package mx.unam.fciencias.cinema_ciencias.dto;

import jakarta.validation.constraints.NotNull;

public class LocationRequestDTO {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}

