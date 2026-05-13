package staniszewska.licencjat_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SupabaseStorageService {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String endpoint = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(supabaseKey);
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + fileName;
        } else {
            throw new RuntimeException("Błąd podczas wgrywania pliku do Supabase");
        }
    }

    public void deleteFileFromUrl(String fileUrl) {
        try {
            String prefix = "/storage/v1/object/public/" + bucketName + "/";
            if (!fileUrl.contains(prefix)) {
                return;
            }
            String fileName = fileUrl.substring(fileUrl.indexOf(prefix) + prefix.length());

            String endpoint = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(supabaseKey);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("Nie udało się usunąć pliku fizycznego z Supabase: " + fileName);
            }
        } catch (Exception e) {
            System.err.println("Błąd podczas komunikacji z Supabase przy usuwaniu: " + e.getMessage());
        }
    }
}
