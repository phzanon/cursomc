package com.pedrozanon.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pedrozanon.cursomc.domain.Cliente;
import com.pedrozanon.cursomc.domain.Cliente;
import com.pedrozanon.cursomc.dto.ClienteDTO;
import com.pedrozanon.cursomc.repositories.ClienteRepository;
import com.pedrozanon.cursomc.services.exceptions.DataIntegrityException;
import com.pedrozanon.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			throw new DataIntegrityException("Não é possível excluir pois há entidades relacionadas!");
		}
	}
	
	public List<Cliente> findAll() {
		List<Cliente> lista = repo.findAll();
		return lista;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String dir) {
		PageRequest pageReq = PageRequest.of(page, linesPerPage, Direction.valueOf(dir), orderBy);
		return repo.findAll(pageReq);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(),null , null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}
}
