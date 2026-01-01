//package com.example.user_service
//
//import com.example.user_service.model.User
//import com.example.user_service.repository.UserRepository
//import com.example.user_service.service.DynamoTableService
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//
//class UserServiceTest {
//
//	private val repository = mockk<UserRepository>()
//	private val service = DynamoTableService(repository)
//
//	@Test
//	fun `should return user when exists`() {
//		val user = User("u1", "Vivek", "vivek@test.com")
//		every { repository.findById("u1") } returns user
//
//		val result = service.get("u1")
//
//		assertEquals(user, result)
//	}
//}
//
