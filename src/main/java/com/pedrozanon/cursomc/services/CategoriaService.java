package com.pedrozanon.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pedrozanon.cursomc.domain.Categoria;
import com.pedrozanon.cursomc.repositories.CategoriaRepository;
import com.pedrozanon.cursomc.services.exceptions.DataIntegrityException;
import com.pedrozanon.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}
	
	public List<Categoria> findAll() {
		List<Categoria> lista = repo.findAll();
		return lista;
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String dir) {
		PageRequest pageReq = PageRequest.of(page, linesPerPage, Direction.valueOf(dir), orderBy);
		return repo.findAll(pageReq);
	}
}
