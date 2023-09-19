package com.gwtech.rewards.controller;

import com.gwtech.rewards.controller.request.TransactionRequest;
import com.gwtech.rewards.controller.response.ApiError;
import com.gwtech.rewards.controller.response.TransactionResponse;
import com.gwtech.rewards.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import static com.gwtech.rewards.controller.SwaggerExamples.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;


    @Operation(summary = "Creates transaction",
        parameters = {
                @Parameter(name = "customerId", in = ParameterIn.PATH, required = true, description = "Customer identification.")
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(examples =
                @ExampleObject(name = "Example of Transaction Request",
                        value = EXAMPLE_TRANSACTION_REQUEST,
                        description = "Transaction Request object"),
                        schema = @Schema(implementation = TransactionRequest.class))
        ),
        responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Successful operation.",
                        headers = @Header(name = "location", description = "Location of created resource.")
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Validation error.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiError.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Missing specified customer.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiError.class)
                        )
                )
        }
    )
    @PostMapping("/api/customers/{customerId}/transactions")
    public ResponseEntity<Void> create(@PathVariable Long customerId,
                                       @RequestBody @Valid TransactionRequest request) {
        var id = transactionService.create(request, customerId);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).build();
    }


    @Operation(summary = "Get Transaction details",
            parameters = {
                    @Parameter(name = "customerId", in = ParameterIn.PATH, required = true, description = "Customer identification."),
                    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Transaction identification."),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation.",
                            content = @Content(examples =
                            @ExampleObject(name = "Example of Transaction Response",
                                    value = EXAMPLE_TRANSACTION_RESPONSE,
                                    description = "Transaction Response object"),
                                    schema = @Schema(implementation = TransactionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing specified transaction or customer.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    )
            }
    )
    @GetMapping("/api/customers/{customerId}/transactions/{id}")
    public TransactionResponse get(@PathVariable Long customerId,
                                   @PathVariable Long id) {
        return transactionService.getDetails(id, customerId);
    }



    @Operation(summary = "Update Transaction",
            parameters = {
                    @Parameter(name = "customerId", in = ParameterIn.PATH, required = true, description = "Customer identification."),
                    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Transaction identification."),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples =
                    @ExampleObject(name = "Example of Transaction Request",
                            value = EXAMPLE_TRANSACTION_REQUEST,
                            description = "Transaction Request object"),
                            schema = @Schema(implementation = TransactionRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation.",
                            content = @Content(examples =
                            @ExampleObject(name = "Example of Transaction Response",
                                    value = EXAMPLE_TRANSACTION_RESPONSE,
                                    description = "Transaction Response object"),
                                    schema = @Schema(implementation = TransactionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing specified transaction or customer.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    )
            }
    )
    @PutMapping("/api/customers/{customerId}/transactions/{id}")
    public TransactionResponse update(@PathVariable Long customerId,
                                      @PathVariable Long id,
                                      @RequestBody @Valid TransactionRequest request) {
        return transactionService.update(id, customerId, request);
    }
}
