package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.TaiKhoanRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ChiNhanhService {
    @Autowired
    private ChiNhanhRepository chiNhanhRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public ResponseEntity<ResponseObject> findAllChiNhanh() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiNhanh Success", chiNhanhRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findChiNhanhById(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        return chiNhanh.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query ChiNhanh Success", chiNhanh)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find ChiNhanh with id = " + chiNhanhId, "")
                );
    }

    public ResponseEntity<ResponseObject> insertChiNhanh(ChiNhanh chiNhanh) {
        List<ChiNhanh> foundChiNhanh = chiNhanhRepository.findByTenChiNhanh(chiNhanh.getTenChiNhanh().trim());

        // If the same name
        if(foundChiNhanh.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "tenChiNhanh already taken or the same id", "")
            );
        }

        // Save first to take Id
        ChiNhanh res = chiNhanhRepository.save(chiNhanh);

        // Create new Account
        TaiKhoan taiKhoan = new TaiKhoan("user" + Long.toString(res.getId()),Integer.toString(new Random().nextInt(90000) + 10000),null);
        // Push to TaiKhoan Database
        taiKhoanRepository.save(taiKhoan);

        // Map to this ChiNhanh
        res.setTaiKhoanFK(taiKhoan);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert ChiNhanh and TaiKhoan Success", chiNhanhRepository.save(res))
        );
    }

    public ResponseEntity<ResponseObject> updateChiNhanh(ChiNhanh newChiNhanh, Long id) {
        List<ChiNhanh> foundChiNhanh = chiNhanhRepository.findByTenChiNhanh(newChiNhanh.getTenChiNhanh().trim());
        if(foundChiNhanh.size() > 0) {
            // Trường hợp update nhưng tên chi nhánh trùng thì chỉ cho thay đổi field khi khác id
            if(foundChiNhanh.get(0).getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "tenChiNhanh already taken", "")
                );
            }
        }

        ChiNhanh updatedChiNhanh = chiNhanhRepository.findById(id)
                .map(chiNhanh -> {
                    chiNhanh.setTenChiNhanh(newChiNhanh.getTenChiNhanh());
                    chiNhanh.setDiaChi(newChiNhanh.getDiaChi());

                    return chiNhanhRepository.save(chiNhanh);
                }).orElseGet(() -> {
                    return null;
                });

        if(updatedChiNhanh == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with id = " + id, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update ChiNhanh Success", updatedChiNhanh)
        );
    }

    public ResponseEntity<ResponseObject> deleteChiNhanh(Long id) {
        Optional<ChiNhanh> foundCN = chiNhanhRepository.findById(id);

        if(foundCN.isPresent()) {
            chiNhanhRepository.deleteById(id);
            taiKhoanRepository.deleteById(foundCN.get().getTaiKhoanFK().getTaiKhoan());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete ChiNhanh Success", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh to delete", "")
        );
    }

    public ResponseEntity<ResponseObject> findChiNhanhByTaiKhoan(TaiKhoan taiKhoan) {
        Optional<TaiKhoan> foundTaiKhoan = taiKhoanRepository.findById(taiKhoan.getTaiKhoan());
        if(!foundTaiKhoan.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("ok", "Cannot find TaiKhoan with taiKhoan = " + taiKhoan.getTaiKhoan(), "")
        );

        Optional<ChiNhanh> foundCN = chiNhanhRepository.findByTaiKhoanFK(foundTaiKhoan.get());

        return foundCN.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query ChiNhanh Success", foundCN)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find ChiNhanh with taiKhoan = " + taiKhoan.getTaiKhoan(), "")
                );
    }

    public ResponseEntity<ResponseObject> findCNByTenOrDiaChi(String key) {
        List<ChiNhanh> foundCN = chiNhanhRepository.findByTenChiNhanhContainsOrDiaChiContains(key, key);

        return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query ChiNhanh Success", foundCN)
        );
    }

    public ResponseEntity<ResponseObject> findCNByTen(String key) {
        List<ChiNhanh> foundCN = chiNhanhRepository.findByTenChiNhanhContains(key);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query ChiNhanh Success", foundCN)
        );
    }
}
