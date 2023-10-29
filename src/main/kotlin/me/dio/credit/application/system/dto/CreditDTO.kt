package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
        @field:NotNull(message = "Credit value required!")
        val creditValue: BigDecimal,
        @field:Future(message = "Wrong date!")
        val dayFirstInstallment: LocalDate,
        @field:Min(1, message = "Min 1!")
        @field:Max(value = 15, message = "Max 15")
        val numberOfInstallments: Int,
        @field:NotNull(message = "Id required!")
        val customerId: Long
) {
    fun toEntity(): Credit = Credit(
            creditValue = this.creditValue,
            dayFirstInstallment = this.dayFirstInstallment,
            numberOfInstallments = this.numberOfInstallments,
            customer = Customer(id = this.customerId)

    )

}
