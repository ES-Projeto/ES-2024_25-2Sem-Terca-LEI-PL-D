import org.locationtech.jts.geom.Geometry;
import java.util.Random;

public class Propriedade {

    private int Objectid;
    private int par_id;
    private long par_num;
    private double shapeArea;
    private double shapeLength;
    private Geometry geometry;
    private String owner;
    private String freguesia;
    private String municipio;
    private String ilha;

    // Novos atributos
    private boolean loteavel;
    private int acesso;

    public Propriedade(int ObjectId, int par_id, long par_num, double shapeArea, double shapeLength,
                       Geometry geometry, String owner, String freguesia, String municipio, String ilha) {
        this.Objectid = ObjectId;
        this.par_id = par_id;
        this.par_num = par_num;
        this.shapeArea = shapeArea;
        this.shapeLength = shapeLength;
        this.geometry = geometry;
        this.owner = owner;
        this.freguesia = freguesia;
        this.municipio = municipio;
        this.ilha = ilha;

        // Geração aleatória razoável
        Random random = new Random(ObjectId); // Seed para consistência entre execuções
        this.loteavel = random.nextBoolean();
        this.acesso = 1 + random.nextInt(10); // [1, 10]
    }

    // Getters
    public int getObjectid() {
        return Objectid;
    }

    public int getPar_id() {
        return par_id;
    }

    public long getPar_num() {
        return par_num;
    }

    public double getShapeArea() {
        return shapeArea;
    }

    public double getShapeLength() {
        return shapeLength;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getOwner() {
        return owner;
    }

    public String getFreguesia() {
        return freguesia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getIlha() {
        return ilha;
    }

    public boolean isLoteavel() {
        return loteavel;
    }

    public int getAcesso() {
        return acesso;
    }

    // Setters (caso precises no futuro)
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setLoteavel(boolean loteavel) {
        this.loteavel = loteavel;
    }

    public void setAcesso(int acesso) {
        this.acesso = acesso;
    }

    // toString
    @Override
    public String toString() {
        return "Propriedade{" +
                "Objectid=" + Objectid +
                ", par_id=" + par_id +
                ", par_num=" + par_num +
                ", shapeArea=" + shapeArea +
                ", shapeLength=" + shapeLength +
                ", geometry=" + geometry +
                ", owner='" + owner + '\'' +
                ", freguesia='" + freguesia + '\'' +
                ", municipio='" + municipio + '\'' +
                ", ilha='" + ilha + '\'' +
                ", loteavel=" + loteavel +
                ", acesso=" + acesso +
                '}';
    }
}
