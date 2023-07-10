package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_ChiNhanh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"quanLySanPhamList","phieuNhapList","chiTietPhieuNhapList"})
public class ChiNhanh {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please input tenChiNhanh")
    private String tenChiNhanh;

    @NotBlank(message = "Please input diaChi")
    private String diaChi;

    @OneToOne
    @JoinColumn(name = "fk_taiKhoan")
    private TaiKhoan taiKhoanFK;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maCN")
    @JsonIgnore
    private List<QuanLySanPham> quanLySanPhamList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chiNhanh")
    @JsonIgnore
    private List<PhieuNhap> phieuNhapList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chiNhanh")
    @JsonIgnore
    private List<ChiTietPhieuNhap> chiTietPhieuNhapList;
}
