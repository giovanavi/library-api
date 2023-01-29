package com.giovana.library.resources;

import com.giovana.library.dto.LivroDTO;
import com.giovana.library.entity.Emprestimo;
import com.giovana.library.entity.Livro;
import com.giovana.library.services.EmprestimoService;
import com.giovana.library.services.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/livro")
public class LivroResource {

    @Autowired
    private LivroService service;

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Livro> findById(@PathVariable Integer id){
        Livro livro = service.findById(id);
        return ResponseEntity.ok().body(livro);
    }
    @GetMapping(value = "/")
    public ResponseEntity<List<LivroDTO>> findAll(){
        List<Livro> list = service.findAll();

        List<LivroDTO> listDTO = list.stream().map(obj -> new LivroDTO(obj)).toList();

        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
    public ResponseEntity<Livro> create(@RequestBody Livro livro){
        //alterando informações do livro no emprestimo
        Emprestimo emp = livro.getEmprestimo();
        emp.setLivro(livro);
        emprestimoService.update(emp.getId(), emp);

        livro = service.create(livro);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livro.getId()).toUri();

        return ResponseEntity.created(uri).body(livro);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Livro> update(@PathVariable Integer id, @RequestBody Livro livro){
        Livro newLivro = service.update(id, livro);

        return ResponseEntity.ok().body(newLivro);
    }
}
