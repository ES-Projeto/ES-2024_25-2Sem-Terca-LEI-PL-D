/**
 * Representa uma sugestão de troca de propriedades entre dois proprietários,
 * com avaliação do ganho de área média unificada e do potencial da troca.
 */
public class SugestaoTroca {

    private Propriedade propA;
    private Propriedade propB;
    private double ganhoTotal;
    private double potencial;

    /**
     * Construtor da classe SugestaoTroca.
     *
     * @param propA        propriedade proposta pelo primeiro proprietário
     * @param propB        propriedade proposta pelo segundo proprietário
     * @param ganhoTotal   ganho total médio de área unificada resultante da troca
     * @param potencial    valor representando a viabilidade da troca (quanto menor a diferença de áreas, maior o potencial)
     */
    public SugestaoTroca(Propriedade propA, Propriedade propB, double ganhoTotal, double potencial) {
        this.propA = propA;
        this.propB = propB;
        this.ganhoTotal = ganhoTotal;
        this.potencial = potencial;
    }

    /**
     * Retorna a propriedade proposta pelo primeiro proprietário.
     *
     * @return objeto {@link Propriedade} do primeiro participante
     */
    public Propriedade getPropA() {
        return propA;
    }

    /**
     * Retorna a propriedade proposta pelo segundo proprietário.
     *
     * @return objeto {@link Propriedade} do segundo participante
     */
    public Propriedade getPropB() {
        return propB;
    }

    /**
     * Retorna o ganho total médio obtido com a troca.
     *
     * @return valor numérico do ganho de área média
     */
    public double getGanhoTotal() {
        return ganhoTotal;
    }

    /**
     * Retorna o potencial da troca, com base na proximidade das áreas das propriedades.
     *
     * @return valor de potencial da troca (entre 0 e 1)
     */
    public double getPotencial() {
        return potencial;
    }

    /**
     * Altera o proprietário da propriedade A.
     *
     * @param newOwner novo nome do proprietário
     */
    public void setOwnerA(String newOwner) {
        this.propA.setOwner(newOwner);
    }

    /**
     * Altera o proprietário da propriedade B.
     *
     * @param newOwner novo nome do proprietário
     */
    public void setOwnerB(String newOwner) {
        this.propB.setOwner(newOwner);
    }

    /**
     * Retorna uma descrição textual da sugestão de troca, incluindo informações dos dois proprietários,
     * áreas das propriedades e os indicadores de ganho e potencial.
     *
     * @return descrição detalhada da sugestão
     */
    @Override
    public String toString() {
        return String.format("Troca sugerida:\n - Proprietário %s troca parcela %d (%.2f m²)\n - Proprietário %s troca parcela %d (%.2f m²)\n - Ganho médio total: %.2f\n - Potencial de troca: %.2f",
                propA.getOwner(), propA.getPar_id(), propA.getShapeArea(),
                propB.getOwner(), propB.getPar_id(), propB.getShapeArea(),
                ganhoTotal, potencial);
    }
}