package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(private val customerRepository: CustomerRepository): ICustomerService {



    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)


    override fun findById(id: Long): Customer = this.customerRepository.findById(id).orElseThrow {
        throw RuntimeException("ID $id not found")
    }

    override fun delele(id: Long) {
        findById(id)
        this.customerRepository.deleteById(id)

    }
}