package com.example.patitas.dto;

public record ResponseLogin(
      String codigo,
      String mensaje,
      String nombreUsuario,
      String correoUsuario
) {
}
