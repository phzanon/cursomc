package com.pedrozanon.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pedrozanon.cursomc.domain.Categoria;
import com.pedrozanon.cursomc.domain.Cidade;
import com.pedrozanon.cursomc.domain.Estado;
import com.pedrozanon.cursomc.domain.Produto;
import com.pedrozanon.cursomc.repositories.CategoriaRepository;
import com.pedrozanon.cursomc.repositories.CidadeRepository;
import com.pedrozanon.cursomc.repositories.EstadoRepository;
import com.pedrozanon.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{

	@Autowired
	private ProdutoRepository pr;
	
	@Autowired
	private CategoriaRepository repo;
	
	@Autowired
	private EstadoRepository er;
	
	@Autowired
	private CidadeRepository cr;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");
		
		Produto p1 = new Produto(null, "Computador", 2000.0);
		Produto p2 = new Produto(null, "Impressora", 800.0);
		Produto p3 = new Produto(null, "Mouse", 80.0);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		repo.saveAll(Arrays.asList(cat1,cat2));
		pr.saveAll(Arrays.asList(p1,p2,p3));
		
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlandia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		er.saveAll(Arrays.asList(est1, est2));
		cr.saveAll(Arrays.asList(c1,c2,c3));
	}

}
