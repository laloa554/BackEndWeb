package Practica1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Practica1.entity.Bolsa;
import Practica1.repository.bolsaRepository;


@Service
@Transactional
public class bolsaService {
	@Autowired
	bolsaRepository bolsaRepository;
	
    public List<Bolsa> list(){
        return bolsaRepository.findAll();
    }

    public Optional<Bolsa> findByModelo(String modelo){
        return bolsaRepository.findByModelo(modelo);
    }

    public void save(Bolsa bolsa){
    	bolsaRepository.save(bolsa);
    }

    public void delete(int id){
    	bolsaRepository.deleteById(id);
    }

    public boolean existsById(int id){
        return bolsaRepository.existsById(id);
    }

    public boolean existsByModelo(String modelo){
        return bolsaRepository.existsByModelo(modelo);
    }

}