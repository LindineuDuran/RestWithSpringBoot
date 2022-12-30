package br.com.llduran.controllers;

import br.com.llduran.data.vo.v1.UserVO;
import br.com.llduran.services.UserServices;
import br.com.llduran.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoints for Managing Users")
public class UserController
{
	@Autowired private UserServices service;

	//@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds all Users", description = "Finds all Users",
			tags = {"Users"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = {
									@Content(
											mediaType = "application/json",
											array = @ArraySchema(schema = @Schema(implementation = UserVO.class))
									)
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	public List<UserVO> findAll()
	{
		return service.findAll();
	}

	//@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces ={ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds a User", description = "Finds a User",
			tags = {"Users"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = @Content(schema = @Schema(implementation = UserVO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	public UserVO findById(@PathVariable(value = "id") Long id)
	{
		return service.findById(id);
	}

	//@CrossOrigin(origins = {"http://localhost:8080", "https://erudio.com.br"})
	@PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
				 produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Adds a new User",
			description = "Adds a new User by passing in a JSON, XML or YML representation of the User!",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = @Content(schema = @Schema(implementation = UserVO.class))
					),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	public UserVO create(@RequestBody UserVO user) { return service.create(user); }

	@PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
								 produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a User",
			description = "Updates a User by passing in a JSON, XML or YML representation of the User!",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Updated", responseCode = "200",
							content = @Content(schema = @Schema(implementation = UserVO.class))
					),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	public UserVO update(@PathVariable(value = "id") Long id, @RequestBody UserVO user)
	{
		return service.update(id, user);
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a User",
			description = "Deletes a User by passing in a JSON, XML or YML representation of the User!",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id)
	{
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}