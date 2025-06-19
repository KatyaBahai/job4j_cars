package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.OwnersHistory;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.owner.OwnerService;
import ru.job4j.cars.service.ownershistory.OwnersHistoryService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/ownersHistory")
public class OwnersHistoryController {
    private final OwnersHistoryService ownersHistoryService;
    private final OwnerService ownerService;
    private final CarService carService;

    @GetMapping("/add/{carId}")
    public String showOwnersForm(@PathVariable int carId,
                                 @RequestParam(value = "returnUrl", required = false) String returnUrl,
                                 Model model) {
        OwnersHistory ownersHistory = new OwnersHistory();
        ownersHistory.setOwner(new Owner());
        model.addAttribute("ownersHistory", ownersHistory);
        model.addAttribute("carId", carId);
        model.addAttribute("returnUrl", returnUrl);
        return "owners/add";
    }

    @PostMapping("/add")
    public String addOwner(@ModelAttribute OwnersHistory ownersHistory,
                           @RequestParam int carId,
                           Model model,
                           @RequestParam(value = "returnUrl", required = false) String returnUrl) {
        ownerService.save(ownersHistory.getOwner());

        Optional<Car> carOptional = carService.findById(carId);
        if (carOptional.isEmpty()) {
            model.addAttribute("message", "There's no car with this identifier.");
            return "/errors/404";
        }
        Car car = carOptional.get();
        ownersHistory.setCar(car);

        ownersHistory.connectOwnerAndCar(car, ownersHistory.getOwner());
        ownersHistoryService.add(ownersHistory);
        return "redirect:" + returnUrl;
    }

}
