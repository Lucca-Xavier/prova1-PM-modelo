import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Clinica clinica = new Clinica();

    public static void main(String[] args) {
        criarDadosIniciais();
        int opcao;
        do {
            exibirMenu();
            opcao = lerInt("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> cadastrarAtendimento();
                case 2 -> associarVeterinario();
                case 3 -> atribuirAtendimentoASala();
                case 4 -> exibirAtendimentosDaSala();
                case 5 -> exibirFinalizadasPorSala();
                case 6 -> buscarPorStatus();
                case 7 -> exibirDetalhesAtendimento();
                case 8 -> alterarStatusAtendimento();
                case 0 -> System.out.println("Programa encerrado.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
        scanner.close();
    }

    private static void criarDadosIniciais() {
        clinica.adicionarVeterinario(new Veterinario("Pedro", "111.111.111-11", "aaa", "31999990001"));
        clinica.adicionarVeterinario(new Veterinario("Thiago", "222.222.222-22", "bbb", "31999990002"));
        clinica.adicionarVeterinario(new Veterinario("Carlos", "333.333.333-33", "ccc", "31999990003"));
        clinica.adicionarSala(new Sala(1, "a", 5, "bloco A"));
        clinica.adicionarSala(new Sala(2, "b", 5, "bloco B"));
        clinica.adicionarSala(new Sala(3, "c", 5, "bloco C"));
    }

    private static void exibirMenu() {
        System.out.println("\n Clínica");
        System.out.println("1 - Cadastrar atendimento");
        System.out.println("2 - Associar veterinário a uma sala");
        System.out.println("3 - Atribuir atendimento a uma sala");
        System.out.println("4 - Exibir atendimentos de uma sala");
        System.out.println("5 - Quantidade de atendimentos finalizados por sala");
        System.out.println("6 - Buscar atendimentos por status");
        System.out.println("7 - Exibir detalhes de um atendimento");
        System.out.println("8 - Alterar o status de um atendimento");
        System.out.println("0 - Sair");
        System.out.println("---------------------------------------");
    }

    private static void cadastrarAtendimento() {
        System.out.println("\n--- Cadastro de Atendimento ---");
        int codigo = lerInt("Código: ");
        if (clinica.buscarAtendimento(codigo) != null) {
            System.out.println("Já existe um atendimento com esse código.");
            return;
        }
        String nomeAnimal = lerTexto("Nome do animal: ");
        String especie = lerTexto("Espécie: ");
        String nomeTutor = lerTexto("Nome do tutor: ");
        String data = lerTexto("Data: ");
        String status = lerStatus();
        String observacao = lerTexto("Observação: ");
        System.out.println("\nDados do procedimento:");
        String nomeProcedimento = lerTexto("Nome: ");
        int duracaoEstimada = lerInt("Duração estimada(min): ");
        double valor = lerDouble("Valor: ");
        String nivelComplexidade = lerTexto("Nível de complexidade: ");

        Procedimento procedimento = new Procedimento(nomeProcedimento, duracaoEstimada, valor, nivelComplexidade);
        clinica.cadastrarAtendimento(new Atendimento(codigo, nomeAnimal, especie, nomeTutor, data, status, observacao, procedimento));
        System.out.println("Atendimento cadastrado com sucesso!");
        System.out.println("O atendimento foi cadastrado sem sala.");
        System.out.println(status.equals(Clinica.FINALIZADA)
                ? "Como o status é \"finalizada\", ele não poderá ser atribuído a uma sala."
                : "Use a opção 3 para atribuí-lo a uma sala.");
    }

    private static void associarVeterinario() {
        System.out.println("\n--- Associar Veterinário a uma Sala ---");
        listarVeterinarios();
        Veterinario veterinario = clinica.buscarVeterinarioPorCpf(lerTexto("CPF: "));
        if (veterinario == null) {
            System.out.println("Veterinário não encontrado.");
            return;
        }
        listarSalas();
        Sala sala = pedirSala();
        if (sala == null) {
            return;
        }
        System.out.println(clinica.associarVeterinarioASala(veterinario, sala)
                ? "Veterinário associado à sala com sucesso!"
                : "Não foi possível associar. O veterinário pode já possuir uma sala ou a sala já possui responsável.");
    }

    private static void atribuirAtendimentoASala() {
        System.out.println("\n--- Atribuir Atendimento a uma Sala ---");
        Atendimento atendimento = pedirAtendimento();
        if (atendimento == null) {
            return;
        }
        if (!clinica.statusPermiteAtribuirASala(atendimento.getStatus())) {
            System.out.println("Somente atendimentos abertos ou em execução podem ser atribuídos a uma sala.");
            return;
        }
        if (atendimento.getSala() != null) {
            System.out.println("Este atendimento já está na sala " + atendimento.getSala().getNumero() + ".");
            return;
        }
        listarSalas();
        Sala sala = pedirSala();
        if (sala == null) {
            return;
        }
        if (clinica.atribuirAtendimentoASala(atendimento, sala)) {
            System.out.println("Atendimento atribuído à sala com sucesso!");
        } else {
            System.out.println("Não foi possível atribuir o atendimento. A sala pode estar cheia, ou a complexidade do "
                    + "procedimento (" + atendimento.getProcedimento().getComplexidade() + ") pode não corresponder ao tipo "
                    + "da sala (" + sala.getTipoSala() + ").");
        }
    }

    private static void exibirAtendimentosDaSala() {
        System.out.println("\n--- Atendimentos de uma Sala ---");
        Sala sala = pedirSala();
        if (sala == null) {
            return;
        }
        System.out.println(sala);
        if (sala.getAtendimentos().isEmpty()) {
            System.out.println("Nenhum atendimento atribuído.");
        } else {
            sala.getAtendimentos().forEach(System.out::println);
        }
        System.out.println("Total de atendimentos: " + sala.getAtendimentos().size());
    }

    private static void exibirFinalizadasPorSala() {
        System.out.println("\n--- Atendimentos Finalizados por Sala ---");
        for (Sala sala : clinica.getSalas()) {
            System.out.println("Sala " + sala.getNumero() + ": " + sala.quantidadeFinalizadas()
                    + " atendimento(s) finalizado(s)");
        }
    }

    private static void buscarPorStatus() {
        System.out.println("\n--- Busca de Atendimentos por Status ---");
        List<Atendimento> atendimentos = clinica.buscarPorStatus(lerStatus());
        if (atendimentos.isEmpty()) {
            System.out.println("Nenhum atendimento encontrado com esse status.");
            return;
        }
        for (Atendimento atend : atendimentos) {
            System.out.println("\n" + atend);
            System.out.println("Sala: " + descreverSala(atend.getSala()));
            System.out.println("Vet: " + descreverVeterinario(atend.getSala()));
        }
    }

    private static void exibirDetalhesAtendimento() {
        System.out.println("\n--- Detalhes do Atendimento ---");
        Atendimento atendimento = pedirAtendimento();
        if (atendimento == null) {
            return;
        }
        Procedimento procedimento = atendimento.getProcedimento();
        System.out.println("Código: " + atendimento.getCodigo());
        System.out.println("Nome do animal: " + atendimento.getNomeAnimal());
        System.out.println("Espécie: " + atendimento.getEspecie());
        System.out.println("Nome do tutor: " + atendimento.getNomeTutor());
        System.out.println("Data: " + atendimento.getData());
        System.out.println("Status: " + atendimento.getStatus());
        System.out.println("Observação: " + atendimento.getObservacao());
        System.out.println("Procedimento: " + procedimento.getNome());
        System.out.println("Tempo estimado: " + procedimento.getTempoEstimado() + " minutos");
        System.out.println("Valor: R$ " + String.format("%.2f", procedimento.getValor()));
        System.out.println("Complexidade: " + procedimento.getComplexidade());
        System.out.println("Sala: " + descreverSala(atendimento.getSala()));

        Veterinario vet = atendimento.getSala() == null ? null : atendimento.getSala().getVeterinarioResponsavel();
        if (vet != null) {
            System.out.println("Veterinário responsável: " + vet.getNome());
            System.out.println("Especialidade: " + vet.getEspecialidade());
        }
    }

    private static void alterarStatusAtendimento() {
        System.out.println("\n--- Alterar Status do Atendimento ---");
        Atendimento atendimento = pedirAtendimento();
        if (atendimento == null) {
            return;
        }
        System.out.println("Status atual: " + atendimento.getStatus());
        String novoStatus = lerStatus();
        if (novoStatus.equals(atendimento.getStatus())) {
            System.out.println("O status não mudou.");
            return;
        }
        atendimento.setStatus(novoStatus);
        System.out.println("Status alterado para \"" + novoStatus + "\" com sucesso!");
    }

    private static Atendimento pedirAtendimento() {
        Atendimento atendimento = clinica.buscarAtendimento(lerInt("Código do atendimento: "));
        if (atendimento == null) {
            System.out.println("Atendimento não encontrado.");
        }
        return atendimento;
    }

    private static Sala pedirSala() {
        Sala sala = clinica.buscarSala(lerInt("Número da sala: "));
        if (sala == null) {
            System.out.println("Sala não encontrada.");
        }
        return sala;
    }

    private static String descreverSala(Sala sala) {
        return sala == null ? "Nenhum" : String.valueOf(sala.getNumero());
    }

    private static String descreverVeterinario(Sala sala) {
        if (sala == null) {
            return "Nenhum";
        }
        Veterinario vet = sala.getVeterinarioResponsavel();
        return vet == null ? "Nenhum responsável associado." : vet.getNome();
    }

    private static void listarVeterinarios() {
        System.out.println("\nVeterinários disponíveis:");
        clinica.getVeterinarios().forEach(System.out::println);
    }

    private static void listarSalas() {
        System.out.println("\nSalas disponíveis:");
        clinica.getSalas().forEach(System.out::println);
    }

    private static String lerStatus() {
        while (true) {
            String status = lerTexto("Status (aberta/em execução/finalizada): ").toLowerCase();
            if (Clinica.STATUS_VALIDOS.contains(status)) {
                return status;
            }
            System.out.println("Status inválido.");
        }
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        if (!scanner.hasNextLine()) {
            System.out.println("\nEntrada encerrada. Programa finalizado.");
            System.exit(0);
        }
        return scanner.nextLine();
    }

    private static int lerInt(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(lerTexto(mensagem).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor numérico válido.");
            }
        }
    }
}
