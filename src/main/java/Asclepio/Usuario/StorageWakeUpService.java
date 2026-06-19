package Asclepio.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StorageWakeUpService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.storage.url}")
    private String storageUrl;

    public void acordarStorage() {
        try {
            restTemplate.getForEntity(storageUrl + "/health", String.class);
        } catch (Exception e) {
            System.out.println("Storage ainda está acordando...");
        }
    }
}