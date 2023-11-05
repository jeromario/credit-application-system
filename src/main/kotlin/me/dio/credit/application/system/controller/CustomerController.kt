package me.dio.credit.application.system.controller

import jakarta.validation.Valid
import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.dto.CustomerUpdateDTO
import me.dio.credit.application.system.dto.CustomerView

import me.dio.credit.application.system.service.impl.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val customerService: CustomerService) {

    @PostMapping
    fun save(@RequestBody @Valid customerDTO: CustomerDTO): ResponseEntity<CustomerView> {
        val savedCustomer = customerDTO.toEntity()
        customerService.save(savedCustomer)
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(savedCustomer))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer = customerService.findById(id)
        return ResponseEntity.ok(CustomerView(customer))
    }

    @PatchMapping
    fun update(@RequestParam(value = "customerId") id: Long,
               @RequestBody @Valid customerUpdateDTO: CustomerUpdateDTO): ResponseEntity<CustomerView> {

        val customer = customerService.findById(id)
        val customerUpdate = customerUpdateDTO.toEntity(customer)
        val customerUpdated = customerService.save(customerUpdate)
        return ResponseEntity.ok(CustomerView(customerUpdated))

    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        customerService.delele(id)
        return ResponseEntity.noContent().build()
    }
}