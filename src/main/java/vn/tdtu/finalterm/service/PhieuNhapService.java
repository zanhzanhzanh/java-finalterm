package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.ChiTietPNRepository;
import vn.tdtu.finalterm.repositories.PhieuNhapRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PhieuNhapService {
    @Autowired
    PhieuNhapRepository phieuNhapRepository;
    @Autowired
    ChiNhanhRepository chiNhanhRepository;
    @Autowired
    ChiTietPNRepository chiTietPNRepository;

    public ResponseEntity<ResponseObject> findAllPhieuNhap() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All PhieuNhap Success", phieuNhapRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findAllPNByChiNhanhId(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("ok", "Can't find ChiNhanh with id = " + chiNhanhId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All PhieuNhap By ChiNhanhId Success", phieuNhapRepository.findAllByChiNhanh(chiNhanh))
        );
    }

    public ResponseEntity<ResponseObject> updatePhieuNhap(PhieuNhap newPhieuNhap, Long id) {
        Optional<PhieuNhap> foundPN = phieuNhapRepository.findById(id);

        if(!foundPN.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find PhieuNhap with id = " + id, "")
            );
        }

        PhieuNhap updatedPN = foundPN
                .map(phieuNhap -> {
                    phieuNhap.setNgayNhap(newPhieuNhap.getNgayNhap());

                    return phieuNhapRepository.save(phieuNhap);
                }).orElseGet(() -> {
                    return null;
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update PhieuNhap Success", updatedPN)
        );
    }

    public ResponseEntity<ResponseObject> deletePhieuNhap(Long phieuNhapId) {
        Optional<PhieuNhap> foundPN = phieuNhapRepository.findById(phieuNhapId);

        if(!foundPN.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find PhieuNhap with id = " + phieuNhapId, "")
            );
        }

        // Find All ChiTietPhieuNhap relevant to Delete
        List<ChiTietPhieuNhap> boxCTPN = chiTietPNRepository.findAllByPhieuNhap(foundPN);
        chiTietPNRepository.deleteAll(boxCTPN);

        // Delete
        phieuNhapRepository.deleteById(phieuNhapId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete PhieuNhap Success", "")
        );
    }
}
