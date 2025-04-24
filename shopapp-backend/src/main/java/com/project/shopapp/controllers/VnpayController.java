package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderResponseDTO;
import com.project.shopapp.helpers.PaymentProcessor;
import com.project.shopapp.services.orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RestController
@RequestMapping("${api.prefix}/payment")
public class VnpayController {

    @Autowired
    private OrderService orderService;
    @GetMapping(value = "createURL/{amount}",  produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveCV(@PathVariable long amount) {
        try {
            return new ResponseEntity<Object>(new Object() {
                public String paymentUrl = PaymentProcessor.processPayment(amount);
            }, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("orders/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

}
