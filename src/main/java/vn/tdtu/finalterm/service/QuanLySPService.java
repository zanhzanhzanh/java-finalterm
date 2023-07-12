package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.enums.DefaultValues;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.QuanLySPRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QuanLySPService {
    @Autowired
    QuanLySPRepository quanLySPRepository;
    @Autowired
    ChiNhanhRepository chiNhanhRepository;

    public ResponseEntity<ResponseObject> findAllQuanLySP() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLy Success", quanLySPRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findAllQuanLySPByChiNhanh(ChiNhanh chiNhanh) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLy By ChiNhanh Success", quanLySPRepository.findAllByMaCN(chiNhanh))
        );
    }

    public ResponseEntity<ResponseObject> findAllQuanLySPByChiNhanhId(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with id = " + chiNhanhId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLy By ChiNhanh Success", quanLySPRepository.findAllByMaCN(chiNhanh.get()))
        );
    }

    // Use for System
    public void insertQuanLySP(Date ngayNhap, List<ChiTietPhieuNhap> chiTietPhieuNhapList) {
        // Take ngayNhap and Plus KYHAN for hanTon
        LocalDate date = ngayNhap.toLocalDate().plusDays((long) DefaultValues.KYHAN.getValue());
        Date hanTon = Date.valueOf(date);

        for(ChiTietPhieuNhap item : chiTietPhieuNhapList) {
            QuanLySanPham updatedQLSP = quanLySPRepository.findByMaSPAndMaCN(item.getSanPham(), item.getChiNhanh())
                    .map(QLSP -> {
                        // Set new giaBan
                        QLSP.setGiaBan(item.getGiaNhap() * DefaultValues.HESOGIATANG.getValue());

                        // trenKe no update
                        // trongKho update by Old + New soLuong
                        int sumSoLuong = QLSP.getTrongKho() + item.getSoLuong();
                        QLSP.setTrongKho(sumSoLuong);

                        // Update new tongHang = trongKho + trenKe
                        QLSP.setTongHang(sumSoLuong + QLSP.getTrenKe());

                        // Update new hanTon
                        QLSP.setHanTon(hanTon);

                        // Update trangThai
                        QLSP.setTrangThai(1);

                        return quanLySPRepository.save(QLSP);
                    }).orElseGet(() -> {
                        // Take giaNhap and multiply HESOGIATANG for giaBan
                        float giaBan = item.getGiaNhap() * DefaultValues.HESOGIATANG.getValue();

                        QuanLySanPham quanLySanPham = new QuanLySanPham(
                                null,
                                hanTon,
                                giaBan,
                                0,
                                item.getSoLuong(),
                                item .getSoLuong(),
                                1,
                                item.getSanPham(),
                                item.getChiNhanh());

                        return quanLySPRepository.save(quanLySanPham);
                    });
        }
    }

    // Use for System
    public void updateTrongKhoSameSoLuong(SanPham sanPham, ChiNhanh chiNhanh, int soLuongOld, int soLuongNew) {
        QuanLySanPham quanLySanPham = quanLySPRepository.findByMaSPAndMaCN(sanPham, chiNhanh)
                .map(QLSP -> {
                    QLSP.setTrongKho(QLSP.getTrongKho() - soLuongOld + soLuongNew);

                    return quanLySPRepository.save(QLSP);
                }).orElseGet(() -> {
                    return null;
                });
    }

    public ResponseEntity<ResponseObject> updateQuanLySP(QuanLySanPham quanLySanPham, Long quanLySPId) {
        QuanLySanPham updateQuanLySanPham = quanLySPRepository.findById(quanLySPId)
                .map((QLSP) -> {
                    QLSP.setGiaBan(quanLySanPham.getGiaBan());

                    return quanLySPRepository.save(QLSP);
                }).orElseGet(() -> {
                    return null;
                });

        if(updateQuanLySanPham == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find QuanLySanPham with id = " + quanLySPId, "")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update QuanLySanPham Success", updateQuanLySanPham)
        );
    }

    public ResponseEntity<ResponseObject> moveSPInSameCN(QuanLySanPham quanLySanPham, Long quanLySPId) {
        Optional<QuanLySanPham> foundQuanLySanPham = quanLySPRepository.findById(quanLySPId);

        if(!foundQuanLySanPham.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find QuanLySanPham with id = " + quanLySPId, "")
            );
        }

        // Move trenKe to trongKho
        if(quanLySanPham.getTrenKe() > 0) {
            // Valid trenKe
            if(quanLySanPham.getTrenKe() > foundQuanLySanPham.get().getTrongKho()) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Can't move with quantity > trongKho", "")
                );
            }

            foundQuanLySanPham.get().setTrenKe(foundQuanLySanPham.get().getTrenKe() + quanLySanPham.getTrenKe());
            foundQuanLySanPham.get().setTrongKho(foundQuanLySanPham.get().getTrongKho() - quanLySanPham.getTrenKe());

        // Move trongKho to trenKe
        } else if(quanLySanPham.getTrongKho() > 0) {
            // Valid trongKho
            if(quanLySanPham.getTrongKho() > foundQuanLySanPham.get().getTrenKe()) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Can't move with quantity > trenKe", "")
                );
            }

            foundQuanLySanPham.get().setTrongKho(foundQuanLySanPham.get().getTrongKho() + quanLySanPham.getTrongKho());
            foundQuanLySanPham.get().setTrenKe(foundQuanLySanPham.get().getTrenKe() - quanLySanPham.getTrongKho());
        }

        // Value is negative
        else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Can't move with negative quantity", "")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update QuanLySanPham Success", quanLySPRepository.save(foundQuanLySanPham.get()))
        );
    }
}
