public class Procedimento {
    private String nome;
    private int duracaoEstimada;
    private double valor;
    private String nivelComplexidade;

    public Procedimento(String nome, int duracaoEstimada, double valor, String nivelComplexidade) {
        this.nome = nome;
        this.duracaoEstimada = duracaoEstimada;
        this.valor = valor;
        this.nivelComplexidade = nivelComplexidade;
    }

    public String getNome() { return nome; }
    public int getTempoEstimado() { return duracaoEstimada; }
    public double getValor() { return valor; }
    public String getComplexidade() { return nivelComplexidade; }

    @Override
    public String toString() {
        return nome + " | Tempo: " + duracaoEstimada + " min | Valor: R$ " + String.format("%.2f", valor) + " | Nível de complexidade: " + nivelComplexidade;
    }
}
