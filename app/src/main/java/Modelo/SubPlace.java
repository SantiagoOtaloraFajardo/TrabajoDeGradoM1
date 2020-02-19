package Modelo;

public class SubPlace {
    private int id;
    private Place place;
    private String nombre;

    public SubPlace(int id) {
        this.id = id;
        nombre="";
        place=null;
    }

    public int getId() {
        return id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
