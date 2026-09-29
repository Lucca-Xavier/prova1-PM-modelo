import java.util.ArrayList;
import java.util.List;

public class Sala {
    private int numero;
    private String tipoSala;
    private int capacidadeMaximaAnimais;
    private String bloco;
    private Veterinario veterinarioResponsavel;
    private List<Atendimento> atendimentos;

    public Sala(int numero, String tipoSala, int capacidadeMaximaAnimais, String bloco) {
        this.numero = numero;
        this.tipoSala = tipoSala;
        this.capacidadeMaximaAnimais = capacidadeMaximaAnimais;
        this.bloco = bloco;
        this.atendimentos = new ArrayList<>();
    }

    public int getNumero() { return numero; }
    public String getTipoSala() { return tipoSala; }
    public int getCapacidadeMaximaAnimais() { return capacidadeMaximaAnimais; }
    public String getBloco() { return bloco; }
    public Veterinario getVeterinarioResponsavel() { return veterinarioResponsavel; }
    public List<Atendimento> getAtendimentos() { return atendimentos; }
    public void definirVeterinario(Veterinario veterinario) { this.veterinarioResponsavel = veterinario; }

    public boolean possuiCapacidade() {
        return atendimentos.size() < capacidadeMaximaAnimais;
    }

    public boolean podeReceber(Atendimento atendimento) {
        return atendimento != null && atendimento.getProcedimento() != null && atendimento.getSala() == null
                && possuiCapacidade()
                && atendimento.getProcedimento().getComplexidade().equalsIgnoreCase(tipoSala);
    }

    public boolean adicionarAtendimento(Atendimento atendimento) {
        if (!podeReceber(atendimento)) {
            return false;
        }
        atendimentos.add(atendimento);
        atendimento.setSala(this);
        return true;
    }

    public int quantidadeFinalizadas() {
        int total = 0;
        for (Atendimento atendimento : atendimentos) {
            if (atendimento.getStatus().equalsIgnoreCase(Clinica.FINALIZADA)) {
                total++;
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return "N° da sala: " + numero + " | Tipo: " + tipoSala + " | Capacidade: " + capacidadeMaximaAnimais
                + " | Bloco: " + bloco + " | Veterinário: "
                + (veterinarioResponsavel == null ? "Nenhum" : veterinarioResponsavel.getNome());
    }
}
