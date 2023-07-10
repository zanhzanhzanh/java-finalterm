package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "tbl_PhieuNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "chiTietPhieuNhapList")
public class PhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Please input NgayNhap")
    private Date ngayNhap;

    @PositiveOrZero
    private float tongCong;

    @ManyToOne
    @JoinColumn(name = "fk_chiNhanh")
    private ChiNhanh chiNhanh;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "phieuNhap")
    @JsonIgnore
    private List<ChiTietPhieuNhap> chiTietPhieuNhapList;
}
