package com.PetCaretopia.order.controller;


import com.PetCaretopia.order.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/decrease")
    public void decreaseStock(@RequestParam("productId") Long productId,
                              @RequestParam("quantity") int quantity) {
        inventoryService.decreaseStock(productId, quantity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/increase")
    public void increaseStock(@RequestParam("productId") Long productId,
                              @RequestParam("quantity") int quantity) {
        inventoryService.increaseStock(productId, quantity);
    }
}
