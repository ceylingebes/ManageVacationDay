package com.sisal.sisalvacation;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/eft/vacation-day")
public class VacationDayController {
    private final VacationDayService vacationDayService;

    public VacationDayController(VacationDayService vacationDayService) {
        this.vacationDayService = vacationDayService;
    }

    @GetMapping
    public ResponseEntity<List<VacationDay>> getAllVacationDays() {
        List<VacationDay> vacationDays = vacationDayService.getAllVacationDays();
        return ResponseEntity.ok(vacationDays);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacationDay> getVacationDayById(@PathVariable Long id) {
        Optional<VacationDay> vacationDay = vacationDayService.getVacationDayById(id);
        return vacationDay.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> createVacationDay(@RequestBody VacationDayDTO vacationDayDto) {
        Long id = vacationDayService.createVacationDay(vacationDayDto).getId();
        Map<String, Long> response = Map.of("id", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateVacationDay(@PathVariable Long id, @RequestBody VacationDayDTO vacationDay) {
        try {
            validateVacationDay(vacationDay);
            VacationDay updatedVacationDay = vacationDayService.updateVacationDay(id, vacationDay);
            return ResponseEntity.ok(updatedVacationDay);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid request payload."));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacation day with the given ID not found.");
        }
    }
    private void validateVacationDay(VacationDayDTO vacationDay) {
        if (!StringUtils.hasText(vacationDay.getName())) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (vacationDay.getDate() == null || !isValidDate(LocalDate.parse(vacationDay.getDate()))) {
            throw new IllegalArgumentException("Invalid request payload");
        }
    }

    private boolean isValidDate(LocalDate date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(formatter);
            LocalDate parsedDate = LocalDate.parse(formattedDate, formatter);
            return parsedDate.equals(date);
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVacationDay(@PathVariable Long id) {
        try {
            vacationDayService.deleteVacationDay(id);
            return ResponseEntity.ok("Vacation day deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request payload.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacation day with the given ID not found.");
        }
    }

}

