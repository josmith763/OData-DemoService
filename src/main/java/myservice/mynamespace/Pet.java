package myservice.mynamespace;

import javax.persistence.*;

@Entity(name = "Table")
@Table(schema = "\"OLINGO\"", name = "\"Pet\"")
public class Pet {
    public Pet() {
        super();
    }

//    @Id
//    @Column(length = 32)
//    private Integer id;

    @Column(name = "\"name\"", length = 250)
    private String name;

//    @Column(name = "\"category\"", length = 250)
//    private String category;
//
//    @Column(name = "\"photoUrls\"", length = 250)
//    private String photoUrls;
//
//    @Column(name = "\"tags\"", length = 250)
//    private String tags;
//
//    @Column(name = "\"status\"", length = 250)
//    private String status;

}
