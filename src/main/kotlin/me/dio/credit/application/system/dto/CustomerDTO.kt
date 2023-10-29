package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDTO(
        @field:NotEmpty(message = "First name required!") val firstName: String,
        @field:NotEmpty(message = "Last name required!") val lastName: String,
        @field:NotEmpty(message = "CPF required!")
        @field:CPF(message = "Invalid CPF!")
        val cpf: String,
        @field:NotNull(message = "Income Required!")
        val income: BigDecimal,
        @field:NotEmpty(message = "email required!")
        @field:Email(message = "Invalid Email!")
        val email: String,
        @field:NotEmpty(message = "password required!") val password: String,
        @field:NotEmpty(message = "Zipcode required!") val zipCode: String,
        @field:NotEmpty(message = "Street required!") val street: String
) {
    fun toEntity(): Customer = Customer(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = this.cpf,
            income = this.income,
            email = this.email,
            password = this.password,
            address = Address(zipCode = this.zipCode, street = this.street),


            )
}
