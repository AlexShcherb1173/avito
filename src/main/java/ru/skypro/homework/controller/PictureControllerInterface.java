package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PictureControllerInterface {
    @GetMapping(value = "/pictures/{id}")
    byte[] getImage(@PathVariable Integer id);
}
