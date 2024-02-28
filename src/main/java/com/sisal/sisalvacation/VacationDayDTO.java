package com.sisal.sisalvacation;

import jakarta.validation.constraints.NotBlank;


public class VacationDayDTO {
    @NotBlank
    private String date;
    @NotBlank
    private String name;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
