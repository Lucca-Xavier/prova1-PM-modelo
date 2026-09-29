public class Veterinario {
    private String nome;
    private String cpf;
    private String especialidade;
    private String telefone;
    private Sala salaResponsavel;

    public Veterinario(String nome, String cpf, String especialidade, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.especialidade = especialidade;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEspecialidade() { return especialidade; }
    public String getTelefone() { return telefone; }
    public Sala getSalaResponsavel() { return salaResponsavel; }
    public boolean possuiSala() { return salaResponsavel != null; }
    public void definirSalaResponsavel(Sala sala) { this.salaResponsavel = sala; }

    @Override
    public String toString() {
        return "Veterinário: " + nome + " | CPF: " + cpf + " | Especialidade: " + especialidade + " | Telefone: " + telefone;
    }
}
