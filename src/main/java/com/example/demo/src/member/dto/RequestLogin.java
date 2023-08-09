package com.example.demo.src.member.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestLogin {

    @NotNull
    private String email;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;

}
