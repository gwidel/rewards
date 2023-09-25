package com.gwtech.rewards.controller;

import com.gwtech.rewards.controller.request.CustomerRequest;
import com.gwtech.rewards.controller.response.ApiError;
import com.gwtech.rewards.controller.response.CustomerResponse;
import com.gwtech.rewards.service.CustomerService;
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

import jakarta.validation.Valid;

import static com.gwtech.rewards.controller.SwaggerExamples.*;

@RestController()
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Creates Customer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples =
                    @ExampleObject(name = "Example of Customer Request",
                            value = EXAMPLE_CUSTOMER_REQUEST,
                            description = "Customer Request object"),
                            schema = @Schema(implementation = CustomerRequest.class))
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
                    )
            }
    )
    @PostMapping("/api/customers")
    public ResponseEntity<Void> create(@RequestBody @Valid CustomerRequest request) {
        long id = customerService.create(request);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).build();
    }

    @Operation(summary = "Get Customer details",
            parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Customer identification.")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation.",
                            content = @Content(examples =
                            @ExampleObject(name = "Example of Customer Response",
                                    value = EXAMPLE_CUSTOMER_RESPONSE,
                                    description = "Customer Response object"),
                                    schema = @Schema(implementation = CustomerResponse.class))
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
    @GetMapping("/api/customers/{id}")
    public CustomerResponse get(@PathVariable Long id) {
        log.info("Getting customer details with id: " + id);
        return customerService.getDetails(id);
    }
}
