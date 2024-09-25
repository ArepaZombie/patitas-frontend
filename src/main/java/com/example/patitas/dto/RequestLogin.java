package com.example.patitas.dto;


public record RequestLogin(
  String tipoDocumento,
  String numeroDocumento,
  String password

) {
}
