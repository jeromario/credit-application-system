package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerUpdateDTO(
        @field:NotEmpty(message = "First name required!") val firstName: String,
        @field:NotEmpty(message = "Last name required!") val lastName: String,
        @field:NotNull(message = "Income Required!")
        val income: BigDecimal,
        @field:NotEmpty(message = "Zipcode required!") val zipCode: String,
        @field:NotEmpty(message = "Street required!") val street: String

) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }



}
