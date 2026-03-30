package br.com.coderbank.portalcliente.services;

import br.com.coderbank.portalcliente.dtos.requests.ClienteRequestDTO;
import br.com.coderbank.portalcliente.dtos.responses.ClienteResponseDTO;
import br.com.coderbank.portalcliente.dtos.responses.ClienteResumoResponseDTO;
import br.com.coderbank.portalcliente.dtos.responses.PendingAccountStatusResponse;
import br.com.coderbank.portalcliente.entities.Cliente;
import br.com.coderbank.portalcliente.entities.PendingAccountOpening;
import br.com.coderbank.portalcliente.enums.AccountStatus;
import br.com.coderbank.portalcliente.enums.costumerStatus;
import br.com.coderbank.portalcliente.exceptions.AccountNotCreatedException;
import br.com.coderbank.portalcliente.exceptions.ClienteJaExistenteException;
import br.com.coderbank.portalcliente.openfeign.dtos.requests.AccountRequestDTO;
import br.com.coderbank.portalcliente.openfeign.feignclients.AccountClient;
import br.com.coderbank.portalcliente.repositories.ClienteRepository;
import br.com.coderbank.portalcliente.repositories.PendingAccountRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final AccountClient accountClient;
    private final PendingAccountRepository pendingAccountRepository;


    public ClienteResponseDTO salvar(final ClienteRequestDTO clienteRequestDTO){
        verificarCpfDuplicado(clienteRequestDTO);

        var clienteEntity = new Cliente();

        BeanUtils.copyProperties(clienteRequestDTO, clienteEntity);

        clienteEntity.setStatus(costumerStatus.ATIVO);

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
        } catch (FeignException  | AccountNotCreatedException e) {
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

    public PendingAccountStatusResponse checkAccountStatus(UUID uuid){
        var pending = pendingAccountRepository.existsByClientId(uuid);

        if (pending){
            return new PendingAccountStatusResponse(
                    uuid, AccountStatus.PENDING, "Opening account still in process, try again later"
            );
        }else return new PendingAccountStatusResponse(
                uuid, AccountStatus.CREATED, "Account created successfully"
        );
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
