package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CreditDTO
import me.dio.credit.application.system.dto.CreditView
import me.dio.credit.application.system.dto.CreditViewList
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditController (private val creditService: CreditService){

    @PostMapping
    fun save(@RequestBody creditDTO: CreditDTO): ResponseEntity<String>{

        val credit = creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!")
    }

    @GetMapping
    fun findAllCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewList>>{

        val creditViewLists = creditService.findAllByCustomer(customerId).stream()
                .map { credit: Credit -> CreditViewList(credit) }
                .collect(Collectors.toList())
        return ResponseEntity.ok(creditViewLists)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> {

        val credit = creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.ok(CreditView(credit))
    }
}