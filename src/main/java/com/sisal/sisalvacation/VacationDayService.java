package com.sisal.sisalvacation;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VacationDayService {
    private final VacationDayRepository vacationDayRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public VacationDayService(VacationDayRepository vacationDayRepository) {
        this.vacationDayRepository = vacationDayRepository;
    }

    public List<VacationDay> getAllVacationDays() {
        return vacationDayRepository.findAll();
    }

    public Optional<VacationDay> getVacationDayById(Long id) {
        return vacationDayRepository.findById(id);
    }

    public VacationDay createVacationDay(VacationDayDTO vacationDayDTO) {
        return vacationDayRepository.save(new VacationDay(vacationDayDTO.getName(), LocalDate.parse(vacationDayDTO.getDate(), dateTimeFormatter)));
    }

    public VacationDay updateVacationDay(Long id, VacationDayDTO vacationDayDTO) {
        if (vacationDayRepository.existsById(id)) {
            VacationDay vacationDay = vacationDayRepository.getReferenceById(id);
            vacationDay.setName(vacationDayDTO.getName());
            vacationDay.setDate(LocalDate.parse(vacationDayDTO.getDate()));
            return vacationDayRepository.save(vacationDay);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacation day with the given ID not found.");
        }
    }

    public void deleteVacationDay(Long id) {
        if (vacationDayRepository.existsById(id)) {
            vacationDayRepository.deleteById(id);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacation day with the given ID not found.");
        }
    }
}

