package br.com.bytebank.accounts.api.openapi.controller;

import br.com.bytebank.accounts.api.dtos.client.response.CustomerClientResponseDTO;
import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.DepositRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.WithdrawRequestDTO;
import br.com.bytebank.accounts.api.dtos.response.AccountResponseDTO;
import br.com.bytebank.accounts.api.dtos.response.BalanceResponseDTO;
import br.com.bytebank.accounts.domain.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;


@Tag(name = "Accounts")
public interface AccountControllerOpenApi {

    @Operation(summary = "Open Account", description = "Open Account After receiving a Customer Created Event")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Account created Successfully",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))),

            @ApiResponse(
                    responseCode = "422",
                    description = "Unprocessable Entity",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AccountResponseDTO> openAccount(@RequestBody(description = "DTO from account", required = true ) AccountRequestDTO accountRequestDTO);


    @Operation(summary = "Find account by passing UUID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account found in repository",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    }
    )
    ResponseEntity<AccountResponseDTO> findAccount(@Parameter(description = "Account ID", required = true) UUID id);

    @Operation(summary = "Inactivate an Account")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Turns an active account to inactive"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "It is not possible to inactivate account with positive balance",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<Void> closeAccount(@Parameter(description = "Account id", required = true) UUID id);

    @Operation(
            summary = "Debit a value from Account",
            description = "The method is exposed on the controller so de MS-Transactions can use with FeignClient when actually operating transactions")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Debit performed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Insufficient Balance for operation",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    ResponseEntity<Void> debit(@RequestBody(description = "DTO to inform and perform the debit", required = true) WithdrawRequestDTO withdrawRequestDTO);


    @Operation(
            summary = "Credit a value from Account",
            description = "The method is exposed on the controller so de MS-Transactions can use with FeignClient when actually operating transactions")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credit performed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<Void> credit(@RequestBody(description = "DTO to inform and perform the credit", required = true) DepositRequestDTO depositRequestDTO);

    @Operation(summary = "Retrieve the balance of an specific account")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account found and balance returned",
                    content = @Content(schema = @Schema(implementation = BalanceResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<BalanceResponseDTO> getBalance(@Parameter(description = "Account id", required = true) UUID id);

    @Operation(summary = "Lists all accounts from a specific customer", description = "Uses Customer Client with Feign Client to retrieve customer ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The search in repository is completed",
                    content = @Content(schema=@Schema(implementation = AccountResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )

    })
    ResponseEntity<List<AccountResponseDTO>> getListAccountsByCustomer(@Parameter(description = "Account id", required = true) UUID id);

    @Operation(
            summary = "Return the customer by searching with the account id",
            description = "Uses customer client to perform the search" )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer found  using Feign Client",
                    content = @Content(schema = @Schema(implementation = CustomerClientResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<CustomerClientResponseDTO> findCustomerByAccountId(@Parameter(description = "Input is an account id", required = true) UUID id);

    @Operation(summary = "List all the account in repository sorted by balance")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List from all existing accounts",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))
            )
    })
    ResponseEntity<List<AccountResponseDTO>> listAllAccountsByBalance();

}
