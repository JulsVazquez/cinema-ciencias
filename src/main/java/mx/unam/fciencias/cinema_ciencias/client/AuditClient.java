package mx.unam.fciencias.cinema_ciencias.client;

import mx.unam.fciencias.cinema_ciencias.dto.AuditRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuditClient {

  private final RestClient restClient;

  public AuditClient(@Qualifier("auditRestClient") RestClient restClient) {
    this.restClient = restClient;
  }

  public void sendLog(Long userId, String action, String details) {
    try {
      AuditRequest request = new AuditRequest(userId, action, details);

      restClient.post()
              .uri("/api/logs")
              .contentType(MediaType.APPLICATION_JSON)
              .body(request)
              .retrieve()
              .toBodilessEntity();

      System.out.println("Log enviado al microservicio correctamente.");

    } catch (Exception e) {
      System.err.println("Fallo al enviar el log de auditoria: " + e.getMessage());
    }
  }
}