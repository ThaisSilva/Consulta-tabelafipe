package principal;
import br.com.alura.TabelaFipe.model.DadosVeiculo;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoAPI;
import br.com.alura.TabelaFipe.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitor = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String endereco = "https://parallelum.com.br/fipe/api/v1/";
    private ConverteDados conversor = new ConverteDados();


    public void Menu() throws JsonProcessingException {
        System.out.println("""
                **** OPÇÕES ****\n
                Carros
                Motos
                Caminhão\n
                Digite uma das opções para consultar valores """);
        var opcaoEscolhida = leitor.nextLine();
        String enderecoCompleto;

        if (opcaoEscolhida.toLowerCase().contains("carr")){
            enderecoCompleto = endereco + "carros/marcas";
        } else if (opcaoEscolhida.toLowerCase().contains("mot")) {
            enderecoCompleto = endereco + "motos/marcas";
        }else { enderecoCompleto = endereco + "caminhoes/marcas";
        }

        var json = consumo.obterDados(enderecoCompleto);
        //DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(json);
        var marcas = conversor.obterLista(json, DadosVeiculo.class);
        marcas.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca que deseja consultar");
        var codigoMarca = leitor.nextLine();

        enderecoCompleto = enderecoCompleto + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(enderecoCompleto);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("Modelo dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do veículo a ser buscado");
        var nomeVeiculo = leitor.nextLine();
        List<DadosVeiculo> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                    .collect(Collectors.toList());
        System.out.println("\nModelos filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo para consultar a avaliação");
        var codigoModelo = leitor.nextLine();

        enderecoCompleto = enderecoCompleto + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(enderecoCompleto);
        List<DadosVeiculo> anos = conversor.obterLista(json, DadosVeiculo.class);
        List<Veiculo> veiculos = new ArrayList<>();
        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = enderecoCompleto + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("Todos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);


    }
}
