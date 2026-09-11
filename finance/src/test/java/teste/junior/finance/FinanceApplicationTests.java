package teste.junior.finance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FinanceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void apiFlowsRespectAuthenticationAndBusinessRules() throws Exception {
		mockMvc.perform(get("/api/transacoes"))
				.andExpect(status().isForbidden());

		var userAToken = register("user-a@example.com");
		mockMvc.perform(get("/api/transacoes").header("Authorization", bearer(userAToken)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());

		var categoryId = objectMapper.readTree(mockMvc.perform(post("/api/categorias")
				.header("Authorization", bearer(userAToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\":\"Teste API Editada\"}"))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()).get("id").asLong();

		mockMvc.perform(post("/api/transacoes")
				.header("Authorization", bearer(userAToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"descricao\":\"Compra de teste\",\"valor\":42.50,\"data\":\"2026-09-09\",\"tipo\":\"DESPESA\",\"categoriaId\":" + categoryId + "}"))
				.andExpect(status().isCreated());

		var transactions = mockMvc.perform(get("/api/transacoes").header("Authorization", bearer(userAToken)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].descricao").value("Compra de teste"))
				.andExpect(jsonPath("$[0].categoriaNome").value("Teste API Editada"))
				.andReturn().getResponse().getContentAsString();
		var transactionId = objectMapper.readTree(transactions).get(0).get("id").asLong();

		mockMvc.perform(get("/api/dashboard/resumo").header("Authorization", bearer(userAToken)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.receitas").value(0.0))
				.andExpect(jsonPath("$.despesas").value(42.5))
				.andExpect(jsonPath("$.saldo").value(-42.5));

		mockMvc.perform(post("/api/transacoes")
				.header("Authorization", bearer(userAToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"descricao\":\"Categoria ausente\",\"valor\":10,\"data\":\"2026-09-09\",\"tipo\":\"DESPESA\",\"categoriaId\":999999}"))
				.andExpect(status().isNotFound());

		mockMvc.perform(delete("/api/categorias/" + categoryId).header("Authorization", bearer(userAToken)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message").value("Categoria possui transações vinculadas"));

		var userBToken = register("user-b@example.com");
		mockMvc.perform(get("/api/transacoes").header("Authorization", bearer(userBToken)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
		mockMvc.perform(put("/api/transacoes/" + transactionId)
				.header("Authorization", bearer(userBToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"descricao\":\"Tentativa\",\"valor\":1,\"data\":\"2026-09-09\",\"tipo\":\"DESPESA\",\"categoriaId\":" + categoryId + "}"))
				.andExpect(status().isNotFound());
	}

	private String register(String email) throws Exception {
		var result = mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"" + email + "\",\"senha\":\"123456\"}"))
				.andExpect(status().isCreated())
				.andReturn();
		JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
		return body.get("token").asText();
	}

	private String bearer(String token) {
		return "Bearer " + token;
	}

}
