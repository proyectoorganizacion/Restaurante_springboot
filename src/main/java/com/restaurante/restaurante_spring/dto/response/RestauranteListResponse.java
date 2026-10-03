package com.restaurante.restaurante_spring.dto.response;

public class RestauranteListResponse {
    private String nombre;
    private String url_logo;

    public RestauranteListResponse(String nombre, String url_logo) {
        this.nombre = nombre;
        this.url_logo = url_logo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl_logo() {
        return url_logo;
    }

    public void setUrl_logo(String url_logo) {
        this.url_logo = url_logo;
    }
}