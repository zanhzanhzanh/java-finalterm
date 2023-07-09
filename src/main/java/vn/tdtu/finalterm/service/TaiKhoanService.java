package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.TaiKhoanRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public ResponseEntity<ResponseObject> dangNhap(TaiKhoan taiKhoan) {
        Optional<TaiKhoan> foundTK = taiKhoanRepository.findById(taiKhoan.getTaiKhoan());

        return foundTK.isPresent() && taiKhoan.getMatKhau().equals(foundTK.get().getMatKhau()) ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Successful to login", "")
                ) :
                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Wrong Password or Account not found", "")
                );
    }

    public ResponseEntity<ResponseObject> doiMatKhau(TaiKhoan taiKhoan) {
        TaiKhoan updatedTaiKhoan = taiKhoanRepository.findById(taiKhoan.getTaiKhoan())
                .map(account -> {
                    account.setMatKhau(taiKhoan.getMatKhau());

                    return taiKhoanRepository.save(account);
                }).orElseGet(() -> {
                    return null;
                });

        if(updatedTaiKhoan == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Wrong Password or Account not found", "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change Password Success", updatedTaiKhoan)
        );
    }
}
