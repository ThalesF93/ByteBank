package br.com.coderbank.portalcliente.controllers;

import br.com.coderbank.portalcliente.entities.Cliente;
import br.com.coderbank.portalcliente.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteControllerV1 {

    @Autowired
    private ClienteRepository repository;

    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente){
        var clienteResponde = repository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponde);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obterTodos(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable UUID id){

        var cliente = repository.findById(id).orElse(null);

        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable(value = "id") UUID id,
                                             @RequestBody Cliente cliente){

        var clienteOptional = repository.findById(id);

        return clienteOptional
                .map(c -> ResponseEntity.ok(repository.save(cliente))).orElseGet(()-> ResponseEntity.notFound().build());
    }


}
