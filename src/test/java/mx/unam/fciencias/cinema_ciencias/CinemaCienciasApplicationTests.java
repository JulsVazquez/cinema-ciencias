package mx.unam.fciencias.cinema_ciencias;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// El secreto normalmente lo sirve el Config Server; en el test se apaga el cliente
// de configuración y de Eureka para no depender de otros servicios.
@SpringBootTest(properties = {
		"cinema.security.gatewaySecret=secreto-de-prueba",
		"spring.cloud.config.enabled=false",
		"eureka.client.enabled=false"
})
class CinemaCienciasApplicationTests {

	@Test
	void contextLoads() {
	}

}
