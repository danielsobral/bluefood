package com.daniel.bluefood.domain.application.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daniel.bluefood.domain.pedido.Carrinho;
import com.daniel.bluefood.domain.pedido.ItemPedido;
import com.daniel.bluefood.domain.pedido.ItemPedidoPK;
import com.daniel.bluefood.domain.pedido.ItemPedidoRepository;
import com.daniel.bluefood.domain.pedido.Pedido;
import com.daniel.bluefood.domain.pedido.Pedido.Status;
import com.daniel.bluefood.domain.pedido.PedidoRepository;
import com.daniel.bluefood.util.SecurityUtils;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public Pedido criarEPagar(Carrinho carrinho, String nunumCartao) {
		
		Pedido pedido = new Pedido();
		
		pedido.setData(LocalDateTime.now());
		pedido.setCliente(SecurityUtils.loggedCliente());
		pedido.setRestaurante(carrinho.getRestaurante());
		pedido.setStatus(Status.Producao);
		pedido.setTaxaEntrega(carrinho.getRestaurante().getTaxaEntrega());
		pedido.setSubtotal(carrinho.getPrecoTotal(false));
		pedido.setTotal(carrinho.getPrecoTotal(true));
		
		pedidoRepository.save(pedido);
		
		int ordem = 1;
		
		for (ItemPedido itemPedido : carrinho.getItens()) {
			itemPedido.setId(new ItemPedidoPK(pedido, ordem++));
			itemPedidoRepository.save(itemPedido);
		}
		
		//TODO: Pagamentos
		
		return pedido;
	}
}
