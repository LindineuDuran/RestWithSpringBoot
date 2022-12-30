package br.com.llduran.data.vo.v1;

import br.com.llduran.model.Permission;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Data
public class UserVO extends RepresentationModel<UserVO> implements Serializable
{
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	@Mapping("id")
	private Long key;

	@JsonProperty("user_name")
	private String username;

	@JsonProperty("full_name")
	private String fullName;

	/*@JsonProperty("password")
	private String password;*/

	@JsonProperty("account_non_expired")
	private Boolean accountNonExpired;

	@JsonProperty("account_non_locked")
	private Boolean accountNonLocked;

	@JsonProperty("credentials_non_expired")
	private Boolean credentialsNonExpired;

	@JsonProperty("enabled")
	private Boolean enabled;

	@JsonProperty("permissions")
	private List<Permission> permissions;
}
