package com.siscom.service;

import com.siscom.dao.CompraRepository;
import com.siscom.dao.PessoaRepository;
import com.siscom.dao.ProdutoRepository;
import com.siscom.dao.VendaRepository;
import com.siscom.exception.SisComException;
import com.siscom.service.model.Cliente;
import com.siscom.service.model.Compra;
import com.siscom.service.model.FormaPgto;
import com.siscom.service.model.Fornecedor;
import com.siscom.service.model.ItemCompra;
import com.siscom.service.model.ItemVenda;
import com.siscom.service.model.Pessoa;
import com.siscom.service.model.Produto;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Venda;
import com.siscom.service.model.Vendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComercialService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private VendaRepository vendaRepository;

    public ComercialService() {
    }

    //Add

    /**
     * Add a Person
     *
     * @param pessoa
     * @return
     * @throws Exception
     */
    public void addPessoa(Pessoa pessoa) throws Exception {
        if (pessoa instanceof Cliente) {
            final Cliente cliente = (Cliente) pessoa;

            if (pessoaRepository.buscarCliente(cliente.getCpf()) != null) {
                throw new Exception("Client already in system.");
            } else {
                pessoaRepository.addCliente(cliente);
            }
        } else if (pessoa instanceof Fornecedor) {
            final Fornecedor fornecedor = (Fornecedor) pessoa;

            if (pessoaRepository.buscarFornecedor(fornecedor.getCnpj()) != null) {
                throw new Exception("Provider already in system.");
            } else {
                pessoaRepository.addFornecedor(fornecedor);
            }
        } else {
            final Vendedor vendedor = (Vendedor) pessoa;

            if (pessoaRepository.buscarVendedor(vendedor.getCpf()) != null) {
                throw new Exception("Sales person already in system.");
            } else if (vendedor.getMetaMensal() <= 0) {
                throw new Exception("Sales goal must be greater than 0");
            } else {
                pessoaRepository.addVendedor(vendedor);
            }
        }
    }

    /**
     * Add a Product
     *
     * @param produto
     */
    public void addProduto(Produto produto) {
        produtoRepository.addProduto(produto);
    }

    /**
     * Add a Sale
     *
     * @param cliente
     * @param vendedor
     * @param formaPgto
     * @param listaItensVenda
     * @throws Exception
     */
    public void fazerVendaParaCliente(Cliente cliente,
                                      Vendedor vendedor,
                                      FormaPgto formaPgto,
                                      List<ItemVenda> listaItensVenda) throws Exception {
        double valorTotal = 0;
        for (ItemVenda itemVenda : listaItensVenda) {
            valorTotal = +itemVenda.getValorVenda();
        }
        if (formaPgto.equals(FormaPgto.PAGAMENTO_A_PRAZO) && cliente.getLimiteCredito() < valorTotal) {
            throw new Exception("Venda não pode ser feita.");
        } else {
            Venda venda = new Venda(0, cliente, vendedor, listaItensVenda, formaPgto.getCodigo(), new Date());

            vendaRepository.fazerVenda(venda);
            for (ItemVenda itemVenda : listaItensVenda) {
                excluirEstoque(itemVenda.getCodProduto(), itemVenda.getQuantVenda());
            }

        }

    }

    /**
     * Add item to Stock
     *
     * @param codProduto
     * @param quantidade
     */
    public void addItemEstoque(Integer codProduto, Integer quantidade) {
        Produto produto = buscarProduto(codProduto);
        produto.addEstoque(quantidade);
        produtoRepository.atualizarEstoque(produto.getCodigo(), produto.getEstoque());
    }

    /**
     * Add a Purchase
     *
     * @param codFornecedor
     * @param listaItensCompra
     * @throws SisComException
     */
    public void criarCompra(Integer codFornecedor, List<ItemCompra> listaItensCompra) throws SisComException {
        for (ItemCompra itemCompra : listaItensCompra) {
            Produto produto = buscarProduto(itemCompra.getCodProduto());
            produto.removeEstoque(itemCompra.getQuantCompra());
            produtoRepository.atualizarEstoque(produto.getCodigo(), produto.getEstoque());
        }

        Compra compra = new Compra(null, codFornecedor, listaItensCompra, new Date());

        compraRepository.fazerCompra(compra);
    }

    //Remove

    /**
     * Delete a Person
     *
     * @param codPessoa
     * @throws Exception
     */
    public void excluirPessoa(Integer codPessoa) throws Exception {
        Pessoa pessoa = pessoaRepository.buscarPessoa(codPessoa);

        if (pessoa instanceof Fornecedor) {
            excluirFornecedor(codPessoa);
            return;
        }

        if (pessoa instanceof Cliente) {
            excluirCliente(codPessoa);
            return;
        }

        if (pessoa instanceof Vendedor) {
            excluirVendedor(codPessoa);
            return;
        }
    }

    private void excluirFornecedor(Integer codPessoa) throws Exception {
        Integer qtdCompras = compraRepository.buscarQtdCompras(codPessoa);

        if (qtdCompras > 0) {
            throw new Exception("Fornecedor tem compra registrada para ele. Nao pode excluir.");
        } else {
            pessoaRepository.excluirPessoa(codPessoa);
        }
    }

    private void excluirCliente(Integer codPessoa) throws Exception {
        Integer qtdVendas = vendaRepository.buscarQtdVendasCliente(codPessoa);

        if (qtdVendas > 0) {
            throw new Exception("Cliente tem venda registrada para ele. Nao pode excluir.");
        } else {
            pessoaRepository.excluirPessoa(codPessoa);
        }
    }

    private void excluirVendedor(Integer codPessoa) throws Exception {
        Integer qtdVendas = vendaRepository.buscarQtdVendasVendedor(codPessoa);

        if (qtdVendas > 0) {
            throw new Exception("Vendedor tem venda registrada para ele. Nao pode excluir.");
        } else {
            pessoaRepository.excluirPessoa(codPessoa);
        }
    }

    /**
     * Delete a Product
     *
     * @param codigo
     * @throws Exception
     */
    public void excluirProduto(Integer codigo) throws Exception {
        Produto produto = buscarProduto(codigo);

        if (vendaRepository.buscarQtdProdutosVenda(codigo) > 0 ||
                compraRepository.buscarQtdProdutosCompra(codigo) > 0) {
            throw new Exception("Não podemos excluir o produto, pois pertence a uma venda/compra");
        } else {
            produtoRepository.excluirProduto(codigo);
        }
    }

    /**
     * Delete a Product from Stock
     *
     * @param codProduto
     * @param quantidade
     * @throws SisComException
     */
    public void excluirEstoque(Integer codProduto, Integer quantidade) throws SisComException {
        Produto produto = buscarProduto(codProduto);
        produto.removeEstoque(quantidade);
        produtoRepository.atualizarEstoque(produto.getCodigo(), produto.getEstoque());
    }

    /**
     * Delete a Purchase DONE
     *
     * @param numCompra
     */
    public void excluirCompra(Integer numCompra) {
        for (ItemCompra itemCompra : compraRepository.buscarItens(numCompra)) {
            addItemEstoque(itemCompra.getCodProduto(), itemCompra.getQuantCompra());
        }

        compraRepository.excluirCompra(numCompra);
    }

    /**
     * Delete a Sale
     *
     * @param numVenda
     */
    public void excluirVenda(Integer numVenda) {
        for (ItemVenda itemVenda : vendaRepository.buscarItens(numVenda)) {
            addItemEstoque(itemVenda.getCodProduto(), itemVenda.getQuantVenda());
        }

        vendaRepository.excluirVenda(numVenda);
    }

    //Search

    /**
     * Search for a Product
     *
     * @param codigo
     * @return
     */
    public Produto buscarProduto(Integer codigo) {
        return produtoRepository.buscarProduto(codigo);
    }

    /**
     * Search for a list of Clients
     *
     * @param nomeCliente
     * @return
     */
    public List<Cliente> obterListaClientes(String nomeCliente, TipoPessoa tipoPessoa) {
        return pessoaRepository.buscarPessoasOrdemAlfabetica(nomeCliente, tipoPessoa);
    }

}
