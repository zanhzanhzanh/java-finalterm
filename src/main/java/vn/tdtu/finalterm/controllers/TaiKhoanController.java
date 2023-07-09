package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.service.TaiKhoanService;

@RestController
@RequestMapping(path = "")
public class TaiKhoanController {
    @Autowired
    TaiKhoanService taiKhoanService;

    @PostMapping("/taiKhoan") // Custom Router
    public ResponseEntity<ResponseObject> dangNhap(@Valid @RequestBody TaiKhoan taiKhoan) {
        return taiKhoanService.dangNhap(taiKhoan);
    }

    @PutMapping("/taiKhoan") // Custom Router
    public ResponseEntity<ResponseObject> doiMatKhau(@Valid @RequestBody TaiKhoan taiKhoan) {
        return taiKhoanService.doiMatKhau(taiKhoan);
    }
}
