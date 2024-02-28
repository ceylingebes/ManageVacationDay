package com.sisal.sisalvacation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

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
    public ResponseEntity<?> updateVacationDay(@PathVariable Long id, @RequestBody @Valid VacationDayDTO vacationDay) {
        try {
            VacationDay updatedVacationDay = vacationDayService.updateVacationDay(id, vacationDay);
            return ResponseEntity.ok(updatedVacationDay);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>("Vacation day with the given ID not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid request payload.", HttpStatus.BAD_REQUEST);
        }
    }

/*    private boolean isValidDate(LocalDate date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(formatter);
            LocalDate parsedDate = LocalDate.parse(formattedDate, formatter);
            return parsedDate.equals(date);
        } catch (DateTimeParseException e) {
            return false;
        }
    }*/


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVacationDay(@PathVariable Long id) {
        try {
            vacationDayService.deleteVacationDay(id);
            return ResponseEntity.ok("Vacation day deleted successfully.");
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>("Vacation day with the given ID not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid request payload.", HttpStatus.BAD_REQUEST);
        }
    }
}

