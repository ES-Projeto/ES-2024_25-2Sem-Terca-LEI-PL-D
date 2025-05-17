public class SugestaoTroca {

    private Propriedade propA;
    private Propriedade propB;
    private double ganhoTotal;
    private double potencial;

    public SugestaoTroca(Propriedade propA, Propriedade propB, double ganhoTotal, double potencial) {
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
        return String.format("Troca sugerida:\n - Proprietário %s troca parcela %d (%.2f m²)\n - Proprietário %s troca parcela %d (%.2f m²)\n - Ganho médio total: %.2f\n - Potencial de troca: %.2f",
                propA.getOwner(), propA.getPar_id(), propA.getShapeArea(),
                propB.getOwner(), propB.getPar_id(), propB.getShapeArea(),
                ganhoTotal, potencial);
    }

}
