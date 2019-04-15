package com.pedrozanon.cursomc.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pedrozanon.cursomc.domain.Categoria;
import com.pedrozanon.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

	@Transactional
	//@Query("select distinct obj from Produto obj inner join obj.categorias cat where obj.nome like %:nome% and cat in :categorias")
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
