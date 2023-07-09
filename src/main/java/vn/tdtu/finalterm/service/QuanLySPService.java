package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.QuanLySPRepository;

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

//    public ResponseEntity<ResponseObject> insertChiNhanh(ChiNhanh chiNhanh) {
//        List<ChiNhanh> foundChiNhanh = chiNhanhRepository.findByTenChiNhanh(chiNhanh.getTenChiNhanh().trim());
//
//        // If the same name
//        if(foundChiNhanh.size() > 0) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("failed", "tenChiNhanh already taken or the same id", "")
//            );
//        }
//
//        // Save first to take Id
//        ChiNhanh res = chiNhanhRepository.save(chiNhanh);
//
//        // Create new Account
//        TaiKhoan taiKhoan = new TaiKhoan("user" + Long.toString(res.getId()),Integer.toString(new Random().nextInt(90000) + 10000),null);
//        // Push to TaiKhoan Database
//        taiKhoanRepository.save(taiKhoan);
//
//        // Map to this ChiNhanh
//        res.setTaiKhoanFK(taiKhoan);
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok", "Insert ChiNhanh and TaiKhoan Success", chiNhanhRepository.save(res))
//        );
//    }
}
