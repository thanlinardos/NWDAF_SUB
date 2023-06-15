/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.46).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.nwdaf.eventsubscription.api;

import io.nwdaf.eventsubscription.model.AnalyticsSubscriptionsTransfer;
import io.nwdaf.eventsubscription.model.ProblemDetails;
import io.nwdaf.eventsubscription.model.RedirectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:13.652419400+03:00[Europe/Athens]")
@Validated
public interface TransfersApi {

    @Operation(summary = "Provide information about requested analytics subscriptions transfer and potentially create a new Individual NWDAF Event Subscription Transfer resource.", description = "", security = {
        @SecurityRequirement(name = "oAuth2ClientCredentials", scopes = {
            "nnwdaf-eventssubscription"        })    }, tags={ "NWDAF Event Subscription Transfers (Collection)" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Create a new Individual NWDAF Event Subscription Transfer resource."),
        
        @ApiResponse(responseCode = "204", description = "No Content. The receipt of the information about analytics subscription(s) that are requested to be transferred and the ability to handle this information (e.g. execute the steps required to transfer an analytics subscription directly) is confirmed. "),
        
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "411", description = "Length Required", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "413", description = "Payload Too Large", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "200", description = "Generic Error") })
    @RequestMapping(value = "/nnwdaf-eventsubscription/v1/transfers",
        produces = { "application/problem+json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> createNWDAFEventSubscriptionTransfer(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody AnalyticsSubscriptionsTransfer body);


    @Operation(summary = "Delete an existing Individual NWDAF Event Subscription Transfer", description = "", security = {
        @SecurityRequirement(name = "oAuth2ClientCredentials", scopes = {
            "nnwdaf-eventssubscription"        })    }, tags={ "Individual NWDAF Event Subscription Transfer (Document)" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "No Content. The Individual NWDAF Event Subscription Transfer resource matching the  transferId was deleted. "),
        
        @ApiResponse(responseCode = "307", description = "Temporary Redirect", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RedirectResponse.class))),
        
        @ApiResponse(responseCode = "308", description = "Permanent Redirect", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RedirectResponse.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "501", description = "Not Implemented", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "200", description = "Generic Error") })
    @RequestMapping(value = "/nnwdaf-eventsubscription/v1/transfers/{transferId}",
        produces = { "application/json", "application/problem+json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteNWDAFEventSubscriptionTransfer(@Parameter(in = ParameterIn.PATH, description = "String identifying a request for an analytics subscription transfer to the  Nnwdaf_EventsSubscription Service ", required=true, schema=@Schema()) @PathVariable("transferId") String transferId);


    @Operation(summary = "Update an existing Individual NWDAF Event Subscription Transfer", description = "", security = {
        @SecurityRequirement(name = "oAuth2ClientCredentials", scopes = {
            "nnwdaf-eventssubscription"        })    }, tags={ "Individual NWDAF Event Subscription Transfer (Document)" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "The Individual NWDAF Event Subscription Transfer resource was modified successfully. "),
        
        @ApiResponse(responseCode = "307", description = "Temporary Redirect", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RedirectResponse.class))),
        
        @ApiResponse(responseCode = "308", description = "Permanent Redirect", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RedirectResponse.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "411", description = "Length Required", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "413", description = "Payload Too Large", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "501", description = "Not Implemented", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetails.class))),
        
        @ApiResponse(responseCode = "200", description = "Generic Error") })
    @RequestMapping(value = "/nnwdaf-eventsubscription/v1/transfers/{transferId}",
        produces = { "application/json", "application/problem+json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> updateNWDAFEventSubscriptionTransfer(@Parameter(in = ParameterIn.PATH, description = "String identifying a request for an analytics subscription transfer to the  Nnwdaf_EventsSubscription Service ", required=true, schema=@Schema()) @PathVariable("transferId") String transferId, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody AnalyticsSubscriptionsTransfer body);

}

