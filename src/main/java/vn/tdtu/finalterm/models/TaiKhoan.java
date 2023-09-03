package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "chiNhanh")
public class TaiKhoan {
    @Id
    @NotBlank(message = "Please input taiKhoan")
    private String taiKhoan;

    @NotBlank(message = "Please input matKhau")
    private String matKhau;

    private boolean enabled = false;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taiKhoanFK")
    @JsonIgnore
    private ChiNhanh chiNhanh;
}
