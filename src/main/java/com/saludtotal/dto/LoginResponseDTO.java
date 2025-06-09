package com.saludtotal.dto;

public class LoginResponseDTO {

    private String message;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String message) {
        this.message = message;
    }

    // Getter y setter

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
