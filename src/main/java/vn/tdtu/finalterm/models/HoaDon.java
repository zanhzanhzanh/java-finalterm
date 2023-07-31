package vn.tdtu.finalterm.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "tbl_HoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = "chiTietHoaDonList")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Please input ngayLap")
    private Date ngayLap;

    @PositiveOrZero
    private float tongCong;

    @ManyToOne
    @JoinColumn(name = "fk_chiNhanh")
    private ChiNhanh chiNhanh;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hoaDon")
//    @JsonIgnore
    private List<ChiTietHoaDon> chiTietHoaDonList;
}
