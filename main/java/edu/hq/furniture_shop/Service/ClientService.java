package edu.hq.furniture_shop.Service;

import edu.hq.furniture_shop.Model.Client;
import edu.hq.furniture_shop.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client authenticate(String email, String password) {
        // Tìm kiếm client qua email và mật khẩu
        Optional<Client> client = clientRepository.findByEmailAndPassword(email, password);
        return client.orElse(null);
    }

    public boolean checkEmailExists(String email) {
        // Kiểm tra email đã tồn tại
        return clientRepository.findByEmail(email).isPresent();
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
    }


}
