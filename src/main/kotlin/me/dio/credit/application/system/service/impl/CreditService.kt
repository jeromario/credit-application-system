package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*
@Service
class CreditService(private val creditRepository: CreditRepository, private val customerService: CustomerService): ICreditService {

    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
//        credit.customer?.id?.let { this.customerService.findById(it) }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> = this.creditRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit = (this.creditRepository.findByCreditCode(creditCode)
                ?: throw RuntimeException("CreditCode: $creditCode not found!"))
        return if (credit.customer?.id == customerId) credit else throw RuntimeException("Contact Admin")
    }
}