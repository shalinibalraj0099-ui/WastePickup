package com.wastepickup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "index";
    }

    @GetMapping("/zones")
    public String zones() {
        return "Zones";
    }

    @GetMapping("/households")
    public String households() {
        return "household";
    }

    @GetMapping("/schedules")
    public String schedules() {
        return "shedule";
    }

    @GetMapping("/pickups")
    public String pickups() {
        return "pickup";
    }
}