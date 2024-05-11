package br.com.alura.TabelaFipe.service;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IConvertedados {
    //converte dados para uma classe selecionada
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe) throws JsonProcessingException;

}
