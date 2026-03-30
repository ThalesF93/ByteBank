package br.com.coderbank.portalcliente.controllers;

import br.com.coderbank.portalcliente.dtos.requests.CheckingPendingAccountsDTO;
import br.com.coderbank.portalcliente.dtos.requests.ClienteRequestDTO;
import br.com.coderbank.portalcliente.dtos.responses.ClienteResponseDTO;
import br.com.coderbank.portalcliente.dtos.responses.ClienteResumoResponseDTO;
import br.com.coderbank.portalcliente.dtos.responses.PagedResponse;
import br.com.coderbank.portalcliente.dtos.responses.PendingAccountStatusResponse;
import br.com.coderbank.portalcliente.entities.Cliente;
import br.com.coderbank.portalcliente.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v2/clientes")
@RequiredArgsConstructor
public class ClienteControllerV2 {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO){

        var cliente = service.salvar(clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cliente);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<PendingAccountStatusResponse> checkPendingAccount(@PathVariable UUID id){
        var pending = service.checkAccountStatus(id);
        return ResponseEntity.ok(pending);
    }

    @GetMapping
    public PagedResponse<ClienteResumoResponseDTO> obterClientes(@RequestParam(defaultValue = "0") int pagina,
                                                                 @RequestParam(defaultValue = "10") int tamanho){
         var pageable = PageRequest.of(pagina, tamanho);

         var paginaCliente = service.obterClientes(pageable);

         return new PagedResponse<>(paginaCliente);
    }
    }


