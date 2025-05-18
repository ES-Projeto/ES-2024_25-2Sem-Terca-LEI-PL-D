public class SugestaoTrocaV2 {

    private Propriedade propA;
    private Propriedade propB;
    private double ganhoTotal;
    private double potencial;

    public SugestaoTrocaV2(Propriedade propA, Propriedade propB, double ganhoTotal, double potencial) {
        this.propA = propA;
        this.propB = propB;
        this.ganhoTotal = ganhoTotal;
        this.potencial = potencial;
    }

    public Propriedade getPropA() {
        return propA;
    }

    public Propriedade getPropB() {
        return propB;
    }

    public double getGanhoTotal() {
        return ganhoTotal;
    }

    public double getPotencial() {
        return potencial;
    }

    public void setOwnerA(String newOwner) {
        this.propA.setOwner(newOwner);
    }

    public void setOwnerB(String newOwner) {
        this.propB.setOwner(newOwner);
    }

    @Override
    public String toString() {
        double valorA = ValorTerrenoCalculator.calcularValor(propA);
        double valorB = ValorTerrenoCalculator.calcularValor(propB);

        return String.format(
                "Troca sugerida:\n" +
                " - Proprietário %s troca parcela %d (%.2f m², %.2f €, loteável: %s, acesso: %d)\n" +
                " - Proprietário %s troca parcela %d (%.2f m², %.2f €, loteável: %s, acesso: %d)\n" +
                " - Ganho médio total: %.2f\n" +
                " - Potencial de troca: %.2f",
                propA.getOwner(), propA.getPar_id(), propA.getShapeArea(), valorA,
                propA.isLoteavel() ? "sim" : "não", propA.getAcesso(),
                propB.getOwner(), propB.getPar_id(), propB.getShapeArea(), valorB,
                propB.isLoteavel() ? "sim" : "não", propB.getAcesso(),
                ganhoTotal, potencial
        );
    }
}
