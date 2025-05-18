import org.locationtech.jts.geom.Geometry;

/**
 * Representa uma propriedade geométrica e administrativa.
 * Esta classe armazena informações como o identificador, área, perímetro,
 * localização administrativa (freguesia, município, ilha), proprietário e geometria espacial.
 */
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
    private Boolean isLoteavel;
    private int qualidadeAcesso;

    /**
     * Construtor da classe Propriedade.
     *
     * @param ObjectId     identificador do objeto
     * @param par_id       identificador do parcelamento
     * @param par_num      número do parcelamento
     * @param shapeArea    área da forma geométrica
     * @param shapeLength  perímetro da forma geométrica
     * @param geometry     geometria da propriedade
     * @param owner        nome do proprietário
     * @param freguesia    freguesia onde está localizada
     * @param municipio    município onde está localizada
     * @param ilha         ilha onde está localizada
     */
    public Propriedade(int ObjectId, int par_id, long par_num, double shapeArea, double shapeLength, Geometry geometry, String owner,  String freguesia, String municipio, String ilha) {
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
        this.isLoteavel = new java.util.Random().nextBoolean();
        this.qualidadeAcesso = 1 + new java.util.Random().nextInt(10);
    }

    /**
     * @return identificador do objeto
     */
    public int getObjectid() {
        return Objectid;
    }

    /**
     * @return identificador do parcelamento
     */
    public int getPar_id() {
        return par_id;
    }

    /**
     * @return número do parcelamento
     */
    public long getPar_num() {
        return par_num;
    }

    /**
     * @return área da forma geométrica
     */
    public double getShapeArea() {
        return shapeArea;
    }

    /**
     * @return perímetro da forma geométrica
     */
    public double getShapeLength() {
        return shapeLength;
    }

    /**
     * @return geometria da propriedade
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @return nome do proprietário
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return freguesia onde está localizada a propriedade
     */
    public String getFreguesia() {
        return freguesia;
    }

    /**
     * @return município onde está localizada a propriedade
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * @return ilha onde está localizada a propriedade
     */
    public String getIlha() {
        return ilha;
    }

    /**
     * @return se a propriedade é loteável
     */
    public Boolean getIsLoteavel() {
        return isLoteavel;
    }

    /**
     * @return qualidade de acesso da propriedade
     */
    public int getQualidadeAcesso() {
        return qualidadeAcesso;
    }

    /**
     * @return representação em string do objeto Propriedade
     */
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
                ", isLoteavel=" + isLoteavel +
                ", qualidadeAcesso=" + qualidadeAcesso +
                '}';
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
