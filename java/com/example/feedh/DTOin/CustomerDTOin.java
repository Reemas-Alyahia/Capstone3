package com.example.feedh.DTOin;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDTOin {
    private Integer id;

    @NotEmpty(message = "Customer Name cannot be empty")
    @Size(min = 2, max = 50, message = "Customer Name must be between 2 and 50 characters")
    private String name;

    @NotEmpty(message = "Customer Email cannot be empty")
    @Size(min = 8, max = 50, message = "Customer Email must be between 8 and 50 characters")
    @Email(message = "Customer Email must be in valid email format")
    private String email;

    @NotEmpty(message = "Customer Phone Number cannot be empty")
    @Pattern(regexp = "^05\\d{8}$",
            message = "Customer Phone number must start with '05' and be exactly 10 digits long.")
    private String phoneNumber;

    @NotEmpty(message = "Customer Address cannot be empty")
    @Size(min = 1, max = 100, message = "Customer Address must be between 1 and 100 characters")
    private String address;

    @NotEmpty(message = "Customer Password cannot be empty")
    @Size(max = 50, message = "Customer Password must be 50 characters maximum")
    @Pattern(regexp = "^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$",
            message = "Customer Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.")
    private String password;

    @NotNull(message = "Customer Foundation File cannot be empty")
    private Boolean foundationFile;

    @NotNull(message = "Customer Agricultural Register cannot be empty")
    @Pattern(regexp = "^\\d{6}$", message = "Customer Agricultural Register must be exactly 6 digits")
    private Integer agriculturalRegister;
}
