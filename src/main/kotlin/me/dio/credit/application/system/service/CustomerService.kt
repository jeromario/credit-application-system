package me.dio.credit.application.system.service

import me.dio.credit.application.system.entity.Customer

interface CustomerService {

    fun save(customer: Customer)
    fun findById(id: Long)
    fun update(id: Long,customer: Customer)
    fun delele(id: Long)
}