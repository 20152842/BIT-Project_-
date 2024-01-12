package lastcoder.webclient;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DateWebService {

	private final WebClient webClient;

	public DateWebService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:8000").build();
	}

	public <T> T getFirstTodoTest(String url, T classes) {

		return (T) this.webClient.get().uri(url).retrieve().bodyToMono(classes.getClass()).block();
	}
}
