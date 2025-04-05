import org.locationtech.jts.geom.Geometry;

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
    }

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
                '}';
    }
}
