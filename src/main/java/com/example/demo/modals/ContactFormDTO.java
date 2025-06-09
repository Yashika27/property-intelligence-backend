package com.example.demo.modals;

import com.google.auto.value.AutoValue.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ContactFormDTO {
    public String name;
    public String email;
    public String phone;
    public String practiceArea;
    public String subject;
    public String message;
}

