package com.pedrozanon.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pedrozanon.cursomc.domain.Categoria;
import com.pedrozanon.cursomc.domain.Cidade;
import com.pedrozanon.cursomc.domain.Cliente;
import com.pedrozanon.cursomc.domain.Endereco;
import com.pedrozanon.cursomc.domain.Estado;
import com.pedrozanon.cursomc.domain.ItemPedido;
import com.pedrozanon.cursomc.domain.Pagamento;
import com.pedrozanon.cursomc.domain.PagamentoComBoleto;
import com.pedrozanon.cursomc.domain.PagamentoComCartao;
import com.pedrozanon.cursomc.domain.Pedido;
import com.pedrozanon.cursomc.domain.Produto;
import com.pedrozanon.cursomc.domain.enums.EstadoPagamento;
import com.pedrozanon.cursomc.domain.enums.TipoCliente;
import com.pedrozanon.cursomc.repositories.CategoriaRepository;
import com.pedrozanon.cursomc.repositories.CidadeRepository;
import com.pedrozanon.cursomc.repositories.ClienteRepository;
import com.pedrozanon.cursomc.repositories.EnderecoRepository;
import com.pedrozanon.cursomc.repositories.EstadoRepository;
import com.pedrozanon.cursomc.repositories.ItemPedidoRepository;
import com.pedrozanon.cursomc.repositories.PagamentoRepository;
import com.pedrozanon.cursomc.repositories.PedidoRepository;
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
	
	@Autowired
	private ClienteRepository cliRep;
	
	@Autowired
	private EnderecoRepository endRep;
	
	@Autowired
	private PedidoRepository pedRepos;
	
	@Autowired
	private PagamentoRepository pagRepos;
	
	@Autowired
	private ItemPedidoRepository iprepos;
	
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
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "123456789", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("12345678","789456123"));
		
		Endereco e1 = new Endereco(null, "Rua Flores" , "300", "Ap 303", "Jardim", "04011062", cli1, c1);
		Endereco e2 = new Endereco(null, "Rua teste" , "450", "Ap 1001", "Jardim Pta", "19050500", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		cliRep.save(cli1);
		endRep.saveAll(Arrays.asList(e1,e2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
		
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedRepos.saveAll(Arrays.asList(ped1,ped2));
		pagRepos.saveAll(Arrays.asList(pagto1,pagto2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		iprepos.saveAll(Arrays.asList(ip1,ip2,ip3));
	}

}
