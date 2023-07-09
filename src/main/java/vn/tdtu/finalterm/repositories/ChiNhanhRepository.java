package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.TaiKhoan;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiNhanhRepository extends JpaRepository<ChiNhanh, Long> {
    List<ChiNhanh> findByTenChiNhanh(String trim);

    Optional<ChiNhanh> findByTaiKhoanFK(TaiKhoan taiKhoan);
}
