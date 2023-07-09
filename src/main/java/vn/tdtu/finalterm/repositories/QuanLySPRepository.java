package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.QuanLySanPham;

import java.util.List;

@Repository
public interface QuanLySPRepository extends JpaRepository<QuanLySanPham, Long> {
    List<QuanLySanPham> findAllByMaCN(ChiNhanh chiNhanh);
}
