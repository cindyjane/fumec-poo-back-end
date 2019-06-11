package com.siscom.controller;

import com.siscom.controller.dto.CompraDto;
import com.siscom.controller.dto.EstatisticaDto;
import com.siscom.controller.dto.PessoaDto;
import com.siscom.controller.dto.ProdutoDto;
import com.siscom.controller.dto.VendaDto;
import com.siscom.controller.mapper.PessoaMapper;
import com.siscom.controller.mapper.ProdutoMapper;
import com.siscom.service.ComercialService;
import com.siscom.service.model.Cliente;
import com.siscom.service.model.Compra;
import com.siscom.service.model.Estatistica;
import com.siscom.service.model.NomeData;
import com.siscom.service.model.Pessoa;
import com.siscom.service.model.Produto;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@RestController
public class ComercialController {

    private ComercialService comercialService;

    @Autowired
    public ComercialController(ComercialService comercialService) {
        this.comercialService = comercialService;
    }

    // GET

    @GetMapping("/pessoa/{cpfCnpj}")
    public ResponseEntity<Pessoa> buscarPessoa(@PathVariable(value = "cpfCnpj") String cpfCnpj) {
        Pessoa pessoa = comercialService.buscarPessoa(cpfCnpj);

        if (pessoa == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pessoa);
    }

    /**
     * Filtra pessoas por nome e tipo
     *
     * @param query
     * @param tipoPessoa
     * @return
     */
    @GetMapping("/pessoa")
    public ResponseEntity<ArrayList<Pessoa>> buscarPessoasOrdemAlfabetica(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "tipoPessoa",
                          required = false)
                    TipoPessoa tipoPessoa) {
        return ResponseEntity.ok(comercialService.buscarPessoaOrdemAlfabetica(query, tipoPessoa));
    }

    /**
     * Bucscar um produto pelo codigo
     *
     * @param codigo
     * @return
     */
    @GetMapping("/produto/{codigo}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable Integer codigo) {
        Produto prod = comercialService.buscarProduto(codigo);

        if (prod == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prod);
    }

    @GetMapping("/produto")
    public ResponseEntity<ArrayList<Produto>> buscarProdutosOrdemAlfabetica(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "emFalta", required = false)
                    Boolean emFalta) {
        return ResponseEntity.ok(comercialService.buscarProdutoOrdemAlfabetica(query, emFalta));
    }

    @GetMapping("/venda")
    public ResponseEntity<ArrayList<NomeData>> obterListaVendas(
            @RequestParam(value = "tipoPessoa", required = false) TipoPessoa tipoPessoa,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "de") @DateTimeFormat(pattern = "yyyy-MM-dd") Date de,
            @RequestParam(value = "para") @DateTimeFormat(pattern = "yyyy-MM-dd") Date para) throws Exception {
        return ResponseEntity.ok(comercialService.obterListaVendas(tipoPessoa, query, de, para));
    }

    @GetMapping("/compra")
    public ResponseEntity<ArrayList<NomeData>> obterListaCompras(
            @RequestParam(value = "nomeFornecedor", required = false) String nomeFornecedor,
            @RequestParam(value = "de") @DateTimeFormat(pattern = "yyyy-MM-dd") Date de,
            @RequestParam(value = "para") @DateTimeFormat(pattern = "yyyy-MM-dd") Date para) {
        return ResponseEntity.ok(comercialService.obterListaCompras(nomeFornecedor, de, para));
    }

    @GetMapping("/venda/statistics")
    public ResponseEntity<ArrayList<Estatistica>> buscarEstatisticaVendas(
            @RequestParam(value = "tipoPessoa") TipoPessoa tipoPessoa,
            @RequestParam(value = "de") @DateTimeFormat(pattern = "yyyy-MM-dd") Date de,
            @RequestParam(value = "para") @DateTimeFormat(pattern = "yyyy-MM-dd") Date para) throws Exception {

        return ResponseEntity.ok(comercialService.buscarEstatisticaVendas(tipoPessoa, de, para));
    }

    @GetMapping("/compra/statistics")
    public ResponseEntity<ArrayList<Estatistica>> buscarEstatisticaCompras(
            @RequestParam(value = "de") @DateTimeFormat(pattern = "yyyy-MM-dd") Date de,
            @RequestParam(value = "para") @DateTimeFormat(pattern = "yyyy-MM-dd") Date para) {
        return ResponseEntity.ok(comercialService.buscarEstatisticaCompras(de, para));
    }

    // POST

    /**
     * Inserir uma pessoa
     *
     * @param pessoa
     * @return
     * @throws Exception
     */
    @PostMapping("/pessoa")
    public ResponseEntity addPessoa(@RequestBody PessoaDto pessoa) throws Exception {
        comercialService.addPessoa(PessoaMapper.mapToModel(pessoa));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/compra")
    public ResponseEntity fazerCompra(@RequestBody CompraDto compra) throws Exception {
        comercialService.criarCompra(compra.getCodigoFornecedor(), compra.getItensCompra());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/venda")
    public ResponseEntity fazerVenda(@RequestBody VendaDto venda) throws Exception {
        comercialService.fazerVendaParaCliente(venda.getCodigoCliente(), venda.getCodigoVendedor(), venda.getFormaPagamento(), venda.getItensVenda());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/produto")
    public ResponseEntity inserirProduto(@RequestBody ProdutoDto produto) {
        comercialService.addProduto(ProdutoMapper.mapToModel(produto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DELETE

    @DeleteMapping("/pessoa/{codigo}")
    public ResponseEntity deletePessoa(@PathVariable(value = "codigo") int codigo) throws Exception {
        comercialService.excluirPessoa(codigo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/compra/{codigo}")
    public ResponseEntity deleteCompra(@PathVariable(value = "codigo") Integer codigo) {
        comercialService.excluirCompra(codigo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/venda/{codigo}")
    public ResponseEntity deleteVenda(@PathVariable(value = "codigo") Integer codigo) {
        comercialService.excluirVenda(codigo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/produto/{codigo}")
    public ResponseEntity deleteProduto(@PathVariable(value = "codigo") Integer codigo) throws Exception {
        comercialService.excluirProduto(codigo);
        return ResponseEntity.noContent().build();
    }
}
