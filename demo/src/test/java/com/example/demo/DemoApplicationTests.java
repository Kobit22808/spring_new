package com.example.demo;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	// @Autowired
	// private MockMvc mockMvc;

	// @BeforeEach
	// public void setUp() {
	// 	// Здесь можно выполнить настройку перед каждым тестом, если это необходимо
	// }

	// @Test
	// @WithMockUser (username = "admin", roles = {"ADMIN"})
	// public void testCreateComandWithAdminRole() throws Exception {
	// 	mockMvc.perform(get("/comands/create"))
	// 			.andExpect(status().isOk())
	// 			.andExpect(view().name("createComand")); // Убедитесь, что возвращается правильное представление
	// }

	// @Test
	// @WithMockUser (username = "user", roles = {"USER"})
	// public void testCreateComandWithUserRole() throws Exception {
	// 	mockMvc.perform(get("/comands/create"))
	// 			.andExpect(status().isForbidden()); // Доступ запрещен для пользователей без роли ADMIN
	// }

	// @Test
	// public void testCreateComandWithoutAuthentication() throws Exception {
	// 	mockMvc.perform(get("/comands/create"))
	// 			.andExpect(status().is3xxRedirection()) // Ожидаем перенаправление на страницу входа
	// 			.andExpect(redirectedUrl("/login")); // Убедитесь, что перенаправление на страницу входа
	// }
}
