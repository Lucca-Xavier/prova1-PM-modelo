import java.util.ArrayList;
import java.util.List;

public class Clinica {
    public static final String ABERTA = "aberta";
    public static final String EM_EXECUCAO = "em execução";
    public static final String FINALIZADA = "finalizada";
    public static final List<String> STATUS_VALIDOS = List.of(ABERTA, EM_EXECUCAO, FINALIZADA);
    private static final List<String> STATUS_QUE_PERMITEM_SALA = List.of(ABERTA, EM_EXECUCAO);

    private List<Veterinario> veterinarios;
    private List<Sala> salas;
    private List<Atendimento> atendimentos;

    public Clinica() {
        veterinarios = new ArrayList<>();
        salas = new ArrayList<>();
        atendimentos = new ArrayList<>();
    }

    public void adicionarVeterinario(Veterinario vet) { veterinarios.add(vet); }
    public void adicionarSala(Sala sala) { salas.add(sala); }
    public void cadastrarAtendimento(Atendimento atend) { atendimentos.add(atend); }

    public boolean statusPermiteAtribuirASala(String status) {
        return STATUS_QUE_PERMITEM_SALA.contains(status.toLowerCase());
    }

    public Veterinario buscarVeterinarioPorCpf(String cpf) {
        for (Veterinario vet : veterinarios) {
            if (vet.getCpf().equals(cpf)) {
                return vet;
            }
        }
        return null;
    }

    public Sala buscarSala(int numero) {
        for (Sala sala : salas) {
            if (sala.getNumero() == numero) {
                return sala;
            }
        }
        return null;
    }

    public Atendimento buscarAtendimento(int codigo) {
        for (Atendimento atend : atendimentos) {
            if (atend.getCodigo() == codigo) {
                return atend;
            }
        }
        return null;
    }

    public boolean associarVeterinarioASala(Veterinario vet, Sala sala) {
        if (vet == null || sala == null) {
            return false;
        }
        if (vet.possuiSala() || sala.getVeterinarioResponsavel() != null) {
            return false;
        }
        vet.definirSalaResponsavel(sala);
        sala.definirVeterinario(vet);
        return true;
    }

    public boolean atribuirAtendimentoASala(Atendimento atend, Sala sala) {
        if (atend == null || sala == null || atend.getSala() != null) {
            return false;
        }
        if (!statusPermiteAtribuirASala(atend.getStatus())) {
            return false;
        }
        return sala.adicionarAtendimento(atend);
    }

    public List<Atendimento> buscarPorStatus(String status) {
        List<Atendimento> resultado = new ArrayList<>();
        for (Atendimento atend : atendimentos) {
            if (atend.getStatus().equalsIgnoreCase(status)) {
                resultado.add(atend);
            }
        }
        return resultado;
    }

    public List<Veterinario> getVeterinarios() { return veterinarios; }
    public List<Sala> getSalas() { return salas; }
    public List<Atendimento> getAtendimentos() { return atendimentos; }
}
