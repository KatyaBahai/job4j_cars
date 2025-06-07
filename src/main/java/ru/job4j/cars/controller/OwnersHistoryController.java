package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.OwnersHistory;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.ownershistory.OwnersHistoryService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/ownersHistory")
public class OwnersHistoryController {
    private final OwnersHistoryService ownersHistoryService;
    private final CarService carService;

    @GetMapping("/add/{carId}")
    public String showOwnersForm(@PathVariable int carId,
                                 @RequestParam(value = "returnUrl", required = false) String returnUrl,
                                 Model model) {
        OwnersHistory ownersHistory = new OwnersHistory();
        Car car = new Car();
        car.setId(carId);
        ownersHistory.setCar(car);
        model.addAttribute("ownersHistory", ownersHistory);
        model.addAttribute("returnUrl", returnUrl);
        return "owners/add";
    }

    @PostMapping("/add")
    public String addOwner(@ModelAttribute OwnersHistory ownersHistory,
                           Model model,
                           @RequestParam(value = "returnUrl", required = false) String returnUrl) {
        ownersHistoryService.add(ownersHistory);
        return "redirect:" + returnUrl;
    }

}
