package com.pedrozanon.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedrozanon.cursomc.domain.Cidade;
import com.pedrozanon.cursomc.domain.Cliente;
import com.pedrozanon.cursomc.domain.Endereco;
import com.pedrozanon.cursomc.domain.enums.TipoCliente;
import com.pedrozanon.cursomc.dto.ClienteDTO;
import com.pedrozanon.cursomc.dto.ClienteNewDTO;
import com.pedrozanon.cursomc.repositories.ClienteRepository;
import com.pedrozanon.cursomc.repositories.EnderecoRepository;
import com.pedrozanon.cursomc.services.exceptions.DataIntegrityException;
import com.pedrozanon.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository er;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		er.saveAll(obj.getEnderecos());
		return obj;
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
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(),null , null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), "", null);
		Endereco end = new Endereco(null, objDto.getLogradouro(),objDto.getNumero() , objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if(objDto.getTelefone2() != null)
			cli.getTelefones().add(objDto.getTelefone2());
		
		if(objDto.getTelefone3() != null)
			cli.getTelefones().add(objDto.getTelefone3());
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}
}
