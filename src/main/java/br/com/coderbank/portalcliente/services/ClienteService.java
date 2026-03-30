package br.com.coderbank.portalcliente.services;

import br.com.coderbank.portalcliente.dtos.requests.ClienteRequestDTO;
import br.com.coderbank.portalcliente.dtos.responses.ClienteResponseDTO;
import br.com.coderbank.portalcliente.dtos.responses.ClienteResumoResponseDTO;
import br.com.coderbank.portalcliente.entities.Cliente;
import br.com.coderbank.portalcliente.entities.PendingAccountOpening;
import br.com.coderbank.portalcliente.entities.enums.Status;
import br.com.coderbank.portalcliente.exceptions.AccountNotCreatedException;
import br.com.coderbank.portalcliente.exceptions.ClienteJaExistenteException;
import br.com.coderbank.portalcliente.openfeign.dtos.requests.AccountRequestDTO;
import br.com.coderbank.portalcliente.openfeign.feignclients.AccountClient;
import br.com.coderbank.portalcliente.repositories.ClienteRepository;
import br.com.coderbank.portalcliente.repositories.PendingAccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClienteService {


    @Autowired
    private ClienteRepository repository;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private PendingAccountRepository pendingAccountRepository;

    public ClienteResponseDTO salvar(final ClienteRequestDTO clienteRequestDTO){
        verificarCpfDuplicado(clienteRequestDTO);

        var clienteEntity = new Cliente();

        BeanUtils.copyProperties(clienteRequestDTO, clienteEntity);

        clienteEntity.setStatus(Status.ATIVO);

        repository.save(clienteEntity);


        try {
            AccountRequestDTO accountRequestDTO = new AccountRequestDTO(clienteEntity.getId());
            accountClient.openAccount(accountRequestDTO);

            return new ClienteResponseDTO(
                    clienteEntity.getId(),
                    clienteEntity.getStatus(),
                    clienteEntity.getCriadoPeloUsuario(),
                    clienteEntity.getCriadoDataEHora(),
                    null,
                    null,
                    "Cliente cadastrado e conta criada com sucesso!"
            );
        } catch (AccountNotCreatedException e) {
            PendingAccountOpening pending = new PendingAccountOpening();
            pending.setClientId(clienteEntity.getId());
            pending.setAttempts(0);
            pendingAccountRepository.save(pending);

            return new ClienteResponseDTO(
                    clienteEntity.getId(),
                    clienteEntity.getStatus(),
                    clienteEntity.getCriadoPeloUsuario(),
                    clienteEntity.getCriadoDataEHora(),
                    null,
                    null,
                    "Cliente cadastrado! Sua conta está sendo criada e ficará disponível em breve."
                    );
        }
    }

    public Page<ClienteResumoResponseDTO> obterClientes(Pageable pageable){
        return repository.findAll(pageable)
                .map(converteParaClienteResumoResponseDTO());
    }

    private static Function<Cliente, ClienteResumoResponseDTO> converteParaClienteResumoResponseDTO() {
        return cliente -> new ClienteResumoResponseDTO(
                cliente.getId(), cliente.getNome(), cliente.getStatus()
        );
    }

    private void verificarCpfDuplicado(final ClienteRequestDTO dto){
       final var cpf = dto.cpf();

        if (repository.existsByCpf(cpf)){
            throw new ClienteJaExistenteException("Cliente com o cpf " + cpf + " já existe");
        }
    }


    private ClienteResumoResponseDTO converteParaClienteConsultaResponseDTO(Cliente cliente) {
        return new ClienteResumoResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getStatus()
        );
    }
}
