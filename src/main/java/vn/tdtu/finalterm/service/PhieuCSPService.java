package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.PhieuChuyenSanPham;
import vn.tdtu.finalterm.models.QuanLySanPham;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.PhieuCSPRepository;
import vn.tdtu.finalterm.repositories.QuanLySPRepository;

import java.util.Optional;

@Service
public class PhieuCSPService {
    @Autowired
    PhieuCSPRepository phieuCSPRepository;
    @Autowired
    ChiNhanhRepository chiNhanhRepository;
    @Autowired
    QuanLySPRepository quanLySPRepository;

    public ResponseEntity<ResponseObject> findAllPhieuCSP() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All PhieuChuyenSanPham Success", phieuCSPRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findAllPhieuCSPByChiNhanhFromId(Long chiNhanhFromId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhFromId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with id = " + chiNhanhFromId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All PhieuChuyenSanPham By ChiNhanhFrom Success", phieuCSPRepository.findAllByChiNhanhFrom(chiNhanh.get()))
        );
    }

    public ResponseEntity<ResponseObject> findAllPhieuCSPByChiNhanhToId(Long chiNhanhToId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhToId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with id = " + chiNhanhToId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All PhieuChuyenSanPham By ChiNhanhTo Success", phieuCSPRepository.findAllByChiNhanhTo(chiNhanh.get()))
        );
    }

    public ResponseEntity<ResponseObject> insertPhieuCSP(PhieuChuyenSanPham phieuChuyenSanPham) {
        // Find QLSP of ChiNhanhFrom
        Optional<QuanLySanPham> quanLySanPhamFrom = quanLySPRepository.findById(phieuChuyenSanPham.getQuanLySanPham().getId());
        if(!quanLySanPhamFrom.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find QuanLySanPham from ChiNhanh: "+ phieuChuyenSanPham.getChiNhanhFrom().getId() +" with id = " + phieuChuyenSanPham.getQuanLySanPham().getId(), "")
        );

        // Find QLSP of ChiNhanhTo
        Optional<QuanLySanPham> quanLySanPhamTo = quanLySPRepository.findByMaSPIdAndMaCNId(quanLySanPhamFrom.get().getMaSP().getId(), phieuChuyenSanPham.getChiNhanhTo().getId());
        if(!quanLySanPhamTo.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find QuanLySanPham from ChiNhanh: "+ phieuChuyenSanPham.getChiNhanhTo().getId() +" with SanPham id = " + quanLySanPhamFrom.get().getMaSP().getId(), "")
        );

        // Check ChiNhanhId of QuanLySanPham and ChiNhanhFromId
        if(quanLySanPhamFrom.get().getMaCN().getId() != phieuChuyenSanPham.getChiNhanhFrom().getId()) return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "ChiNhanhId of QuanLySanPham isn't equal ChiNhanhFromId", "")
        );

        // Check Quantity
        int quantityTrongKhoLeft = quanLySanPhamFrom.get().getTrongKho() - phieuChuyenSanPham.getSoLuongChuyen();
        if(quantityTrongKhoLeft < 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "ChiNhanhFrom(id: "+ phieuChuyenSanPham.getChiNhanhFrom().getId() +") not enough quantity in stock", "")
            );
        }

        // Reassign and save
        quanLySanPhamFrom.get().setTrongKho(quantityTrongKhoLeft);
        quanLySanPhamTo.get().setTrongKho(quanLySanPhamTo.get().getTrongKho() + phieuChuyenSanPham.getSoLuongChuyen());

        QuanLySanPham resQLSPFrom = quanLySPRepository.save(quanLySanPhamFrom.get());
        quanLySPRepository.save(quanLySanPhamTo.get());

        // Save PCSP
        phieuChuyenSanPham.setQuanLySanPham(resQLSPFrom);
        phieuChuyenSanPham.setChiNhanhFrom(resQLSPFrom.getMaCN());
        phieuChuyenSanPham.setChiNhanhTo(quanLySanPhamTo.get().getMaCN());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Move SanPham Successfully", phieuCSPRepository.save(phieuChuyenSanPham))
        );
    }
}
