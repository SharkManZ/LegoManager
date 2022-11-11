package ru.shark.home.legomanager.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.shark.home.legomanager.exception.RemoteDataException;

@Component
public class RemoteDataProvider {
    public String getDataFromUrl(String url, String errorMsg) throws RemoteDataException {
        ResponseEntity<String> restResponse = new RestTemplate().getForEntity(url,
                String.class);
        if (restResponse.getStatusCodeValue() != 200) {
            throw new RemoteDataException(restResponse.getStatusCodeValue(), errorMsg + ": " +
                    restResponse.getStatusCode().getReasonPhrase());
        }
        return restResponse.getBody();
    }
}
