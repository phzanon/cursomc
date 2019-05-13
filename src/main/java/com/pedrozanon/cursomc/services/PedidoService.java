package com.pedrozanon.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pedrozanon.cursomc.domain.Cliente;
import com.pedrozanon.cursomc.domain.ItemPedido;
import com.pedrozanon.cursomc.domain.PagamentoComBoleto;
import com.pedrozanon.cursomc.domain.Pedido;
import com.pedrozanon.cursomc.domain.Produto;
import com.pedrozanon.cursomc.domain.enums.EstadoPagamento;
import com.pedrozanon.cursomc.repositories.ItemPedidoRepository;
import com.pedrozanon.cursomc.repositories.PagamentoRepository;
import com.pedrozanon.cursomc.repositories.PedidoRepository;
import com.pedrozanon.cursomc.repositories.ProdutoRepository;
import com.pedrozanon.cursomc.security.UserSS;
import com.pedrozanon.cursomc.services.exceptions.AuthorizationException;
import com.pedrozanon.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository ipRepo;
	
	@Autowired
	private ClienteService cliService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ClienteService clienteService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(cliService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			
			Optional<Produto> p = produtoRepository.findById(ip.getProduto().getId());
			ip.setProduto(p.get());
			ip.setPreco(p.get().getPreco());
			ip.setPedido(obj);
		}
		ipRepo.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		
		if(user == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}
