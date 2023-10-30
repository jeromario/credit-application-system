package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.ICreditService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
@Service
class CreditService(private val creditRepository: CreditRepository, private val customerService: CustomerService): ICreditService {

    override fun save(credit: Credit): Credit {

        if (!this.validDayFirstInstallment(credit.dayFirstInstallment)){
            throw BusinessException("Invalid Date!")
        }

        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
//        credit.customer?.id?.let { this.customerService.findById(it) }
        return this.creditRepository.save(credit)
    }


    override fun findAllByCustomer(customerId: Long): List<Credit> = this.creditRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit = (this.creditRepository.findByCreditCode(creditCode)
                ?: throw BusinessException("CreditCode: $creditCode not found!"))
        return if (credit.customer?.id == customerId) credit else throw IllegalArgumentException("Contact Admin")
    }

    private fun validDayFirstInstallment(dayFirstInstallment: LocalDate): Boolean {

//        return if (dayFirstInstallment.isBefore(LocalDate.now().plusMonths(3))) true
////        else throw BusinessException("Invalid date!")
        return dayFirstInstallment.isBefore(LocalDate.now().plusMonths(3))
    }
}