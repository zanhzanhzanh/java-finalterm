package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.service.SanPhamService;

import java.util.List;

@RestController
@RequestMapping(path = "")
public class SanPhamController {
    @Autowired
    SanPhamService sanPhamService;

    @GetMapping("/sanPham")
    public List<SanPham> findAllSanPham() {
        return sanPhamService.findAllSanPham();
    }

    @GetMapping("/sanPham/{id}")
    public ResponseEntity<ResponseObject> findSanPhamById(@PathVariable("id") Long id) {
        return sanPhamService.findSanPhamById(id);
    }

    @GetMapping("/sanPhamQuery/{key}") // Find Request
    public ResponseEntity<ResponseObject> findSPByTenOrLoaiOrTHOrTGOrTL(@PathVariable("key") String key) {
        return sanPhamService.findSPByTenOrLoaiOrTHOrTGOrTL(key);
    }

    @PostMapping("/sanPham")
    public ResponseEntity<ResponseObject> insertSanPham(@Valid @RequestBody SanPham sanPham) {
        return sanPhamService.insertSanPham(sanPham);
    }

    @PutMapping("/sanPham/{id}")
    public ResponseEntity<ResponseObject> updateSanPham(@Valid @RequestBody SanPham sanPham, @PathVariable("id") Long id) {
        return sanPhamService.updateSanPham(sanPham, id);
    }

    @DeleteMapping("/sanPham/{id}")
    public ResponseEntity<ResponseObject> deleteSanPham(@PathVariable("id") Long id) {
        return sanPhamService.deleteSanPham(id);
    }
}
