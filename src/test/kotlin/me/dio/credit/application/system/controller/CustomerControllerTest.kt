package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.dto.CustomerUpdateDTO
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import kotlin.random.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerControllerTest {

    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object{
        const val URL: String = "/api/customers"
    }

    @BeforeEach fun setup() = customerRepository.deleteAll()

    @AfterEach fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should create a customer and return 201 status`(){
        //given
        val customerDTO: CustomerDTO = buildCustomerDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerDTO)
        //whenandthen
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jefferson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Andrade"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("je@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua A"))
                .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should not save a customer with same cpf and return 409 status`(){
        //given
        customerRepository.save(buildCustomerDTO().toEntity())
        val customerDTO: CustomerDTO = buildCustomerDTO()
        val valueAsString = objectMapper.writeValueAsString(customerDTO)
        //when
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(MockMvcResultMatchers.status().isConflict)
             .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.exception")
          .value("class org.springframework.dao.DataIntegrityViolationException")
      )
      .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
      .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with name empty and return 400 status`(){
        //givem
        //customerRepository.save(buildCustomerDTO().toEntity())
        val customerDTO = buildCustomerDTO(lastName = "")
        val writeValueAsString = objectMapper.writeValueAsString(customerDTO)
        //when
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.web.bind.MethodArgumentNotValidException"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find by id a customer and return 200 status`(){
        //given
        val customer = customerRepository.save(buildCustomerDTO().toEntity())
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jefferson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Andrade"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("je@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua A"))
                .andDo(MockMvcResultHandlers.print())

    }
    @Test
    fun `should not find by id a customer with id not found and return 400 status`(){
        //given
        val customer = customerRepository.save(buildCustomerDTO().toEntity())
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${2}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class me.dio.credit.application.system.exception.BusinessException"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
                .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should delete customer by id and return 204 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDTO().toEntity())
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete customer by id and return 400 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDTO().toEntity())
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${2}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class me.dio.credit.application.system.exception.BusinessException"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should update a customer and return 201 status`(){
        //givem
        val customer = customerRepository.save(buildCustomerDTO().toEntity())
        val customerUpadteDTO = buildCustomerUpadteDTO()
        val writeValueAsString = objectMapper.writeValueAsString(customerUpadteDTO)
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Je"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Roma"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("je@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua A"))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update a customer with invalid id and return 400 status`(){
        //givem
        val invalidId: Long = Random.nextLong()
        val customerUpadteDTO = buildCustomerUpadteDTO()
        val writeValueAsString = objectMapper.writeValueAsString(customerUpadteDTO)
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=$invalidId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class me.dio.credit.application.system.exception.BusinessException"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
                .andDo(MockMvcResultHandlers.print())
    }
    @Test
    fun `should not update a customer with empty field and return 400 status`(){
        //givem
        val custumer: Customer = customerRepository.save(buildCustomerDTO().toEntity())
        val customerUpadteDTO = buildCustomerUpadteDTO(zipCode = "")
        val writeValueAsString = objectMapper.writeValueAsString(customerUpadteDTO)
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${custumer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.web.bind.MethodArgumentNotValidException"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
                .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDTO(
            firstName: String = "Jefferson",
            lastName: String = "Andrade",
            cpf: String = "28475934625",
            email: String = "je@gmail.com",
            password: String = "12345",
            zipCode: String = "12345",
            street: String = "Rua A",
            income: BigDecimal = BigDecimal.valueOf(1000.0)
    ) = CustomerDTO(
            firstName = firstName,
            lastName =  lastName,
            cpf =  cpf,
            income =  income,
            email =  email,
            password =  password,
            zipCode =  zipCode,
            street =  street
    )

    private fun buildCustomerUpadteDTO(
            firstName: String = "Je",
            lastName: String = "Roma",
            zipCode: String = "12345",
            street: String = "Rua A",
            income: BigDecimal = BigDecimal.valueOf(5000.0)
    ) = CustomerUpdateDTO(
            firstName = firstName,
            lastName = lastName,
            income = income,
            zipCode = zipCode,
            street = street
    )
}