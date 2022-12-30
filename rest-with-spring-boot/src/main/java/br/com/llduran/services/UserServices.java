package br.com.llduran.services;

import br.com.llduran.controllers.UserController;
import br.com.llduran.data.vo.v1.UserVO;
import br.com.llduran.exceptions.RequiredObjectIsNullException;
import br.com.llduran.exceptions.ResourceNotFoundException;
import br.com.llduran.mapper.DozerMapper;
import br.com.llduran.model.User;
import br.com.llduran.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService
{
	private Logger logger = Logger.getLogger(UserServices.class.getName());

	@Autowired
	UserRepository repository;

	public UserServices(UserRepository repository) { this.repository = repository; }

	public List<UserVO> findAll()
	{
		logger.info("Finding all users!");

		var users = DozerMapper.parseListObjects(repository.findAll(), UserVO.class);
		users.stream()
				.forEach(u -> u.add(linkTo(methodOn(UserController.class).findById(u.getKey())).withSelfRel()));
		return users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		logger.info("Finding one user by name " + username + "!");
		var user = repository.findByUsername(username);
		if (user != null)
		{
			return user;
		}
		else
		{
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}

	public UserVO findById(Long id)
	{
		logger.info("Finding one User!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var vo = DozerMapper.parseObject(entity, UserVO.class);
		vo.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
		return vo;
	}

	public UserVO create(UserVO user)
	{

		if (user == null)
			throw new RequiredObjectIsNullException();

		logger.info("Creating one User!");
		var entity = DozerMapper.parseObject(user, User.class);
		var vo = DozerMapper.parseObject(repository.save(entity), UserVO.class);
		vo.add(linkTo(methodOn(UserController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public UserVO update(Long id, UserVO user)
	{
		if (user == null)
			throw new RequiredObjectIsNullException();

		logger.info("Updating one User!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		entity.setId(id);
		entity.setUserName(user.getUsername());
		entity.setFullName(user.getFullName());
		//entity.setPassword(user.getPassword());
		entity.setAccountNonExpired(user.getAccountNonExpired());
		entity.setAccountNonLocked(user.getAccountNonLocked());
		entity.setCredentialsNonExpired(user.getCredentialsNonExpired());
		entity.setEnabled(user.getEnabled());

		var vo = DozerMapper.parseObject(repository.save(entity), UserVO.class);
		vo.add(linkTo(methodOn(UserController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public void delete(Long id)
	{
		logger.info("Deleting one User!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);
	}
}