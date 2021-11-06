package link.s.sls.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sweeter
 * @date 2021/11/6
 */
@RequestMapping("client")
@Slf4j
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("refreshSecret")
    public ResponseEntity<Object> refreshSecret(String appKey,String appSecret) {
        clientService.refresh(appKey,appSecret);
        return ResponseEntity.ok(null);

    }

}
