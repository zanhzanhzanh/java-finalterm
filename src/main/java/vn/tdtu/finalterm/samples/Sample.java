package vn.tdtu.finalterm.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.QuanLySanPham;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.QuanLySPRepository;
import vn.tdtu.finalterm.repositories.SanPhamRepository;
import vn.tdtu.finalterm.repositories.TaiKhoanRepository;

import java.sql.Date;
import java.util.List;

@Configuration
public class Sample {
    // logger
    private static final Logger logger = LoggerFactory.getLogger(Sample.class);

    @Bean
    CommandLineRunner initDataBase(SanPhamRepository sanPhamRepository,
                                   TaiKhoanRepository taiKhoanRepository,
                                   ChiNhanhRepository chiNhanhRepository,
                                   QuanLySPRepository quanLySPRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // SanPham
                SanPham.SanPhamBuilder productA =  SanPham.builder()
                        .tenSanPham("Doraemon")
                        .loaiSanPham("Sách")
                        .thuongHieu("NXB Kim Đồng")
                        .tacGia("Fujiko Fujio")
                        .theLoai("Truyện Tranh");
                SanPham.SanPhamBuilder productB =  SanPham.builder()
                        .tenSanPham("Hai vạn dặm dưới biển")
                        .loaiSanPham("Sách")
                        .thuongHieu("NXB Văn Học")
                        .tacGia("Jules Verne")
                        .theLoai("Tiểu Thuyết");
                SanPham.SanPhamBuilder productC =  SanPham.builder()
                        .tenSanPham("Đồng Hồ Báo Thức")
                        .loaiSanPham("Đồng Hồ")
                        .thuongHieu("Apple")
                        .tacGia("")
                        .theLoai("");
                logger.info("insert: " + sanPhamRepository.save(productA.build()));
                logger.info("insert: " + sanPhamRepository.save(productB.build()));
                logger.info("insert: " + sanPhamRepository.save(productC.build()));

                // ChiNhanh + TaiKhoan
                TaiKhoan.TaiKhoanBuilder taiKhoanA = TaiKhoan.builder()
                        .taiKhoan("user1")
                        .matKhau("1111");
                ChiNhanh.ChiNhanhBuilder chiNhanhA = ChiNhanh.builder()
                        .id(1L)
                        .tenChiNhanh("helloKitty")
                        .diaChi("Quan 7")
                        .taiKhoanFK(taiKhoanA.build());
                taiKhoanA.chiNhanh(chiNhanhA.build());
//                TaiKhoan taiKhoanP = taiKhoanA.build();
//                ChiNhanh chiNhanhP = chiNhanhA.build();
                logger.info("insert: " + taiKhoanRepository.save(taiKhoanA.build()));
                logger.info("insert: " + chiNhanhRepository.save(chiNhanhA.build()));

                TaiKhoan.TaiKhoanBuilder taiKhoanB = TaiKhoan.builder()
                        .taiKhoan("meomeo")
                        .matKhau("2222");
                ChiNhanh.ChiNhanhBuilder chiNhanhB = ChiNhanh.builder()
                        .id(2L)
                        .tenChiNhanh("helloKitty444")
                        .diaChi("Quan 8")
                        .taiKhoanFK(taiKhoanB.build());
                taiKhoanB.chiNhanh(chiNhanhB.build());
                logger.info("insert: " + taiKhoanRepository.save(taiKhoanB.build()));
                logger.info("insert: " + chiNhanhRepository.save(chiNhanhB.build()));

//                TaiKhoan taiKhoan = new TaiKhoan("user1", "1111");
//                ChiNhanh chiNhanh = new ChiNhanh(1L, "hello", "Q7");
//                taiKhoan.setChiNhanh(chiNhanh);
//                taiKhoan.getChiNhanh().setTaiKhoan(taiKhoan);
//                chiNhanh.setTaiKhoan(taiKhoan);
//                logger.info("insert: " + taiKhoanRepository.save(taiKhoan));
//                logger.info("insert: " + chiNhanhRepository.save(chiNhanh));

                List<ChiNhanh> box = chiNhanhRepository.findAll();

                logger.info("list Chi Nhanh: " + box);

                // QuanLySanPham + SanPham + ChiNhanh
                QuanLySanPham.QuanLySanPhamBuilder quanLyA = QuanLySanPham.builder()
                        .id(1L)
                        .hanTon(new Date(2023, 3, 2))
                        .giaBan(302.0f)
                        .trenKe(0)
                        .trongKho(3000)
                        .trangThai(1)
                        // This line shouldn't use productA.build() because It's just Transient
                        // not Persistent (we can fix this by put CascadeType.ALL next to FK of QuanLySP Class
                        // but it will save 2 time because productA save once above and once from CascadeType.ALL)
                        // so just call productA from DB(Detached -> Persistent) (productA was saved from the first time)
                        .maSP(sanPhamRepository.getReferenceById(1L))
                        .maCN(chiNhanhB.build());

                QuanLySanPham.QuanLySanPhamBuilder quanLyB = QuanLySanPham.builder()
                        .id(2L)
                        .hanTon(new Date(2021, 4, 2))
                        .giaBan(102.0f)
                        .trenKe(100)
                        .trongKho(2000)
                        .trangThai(0)
                        .maSP(sanPhamRepository.getReferenceById(2L))
                        .maCN(chiNhanhB.build());

                QuanLySanPham.QuanLySanPhamBuilder quanLyC = QuanLySanPham.builder()
                        .id(3L)
                        .hanTon(new Date(2021, 4, 2))
                        .giaBan(102.0f)
                        .trenKe(100)
                        .trongKho(2000)
                        .trangThai(0)
                        .maSP(sanPhamRepository.getReferenceById(3L))
                        .maCN(chiNhanhA.build());

                logger.info("insert: " + quanLySPRepository.save(quanLyA.build()));
                logger.info("insert: " + quanLySPRepository.save(quanLyB.build()));
                logger.info("insert: " + quanLySPRepository.save(quanLyC.build()));

                List<QuanLySanPham> box2 = quanLySPRepository.findAll();

                logger.info("list Quan Ly San Pham: " + box2);
            }
        };
    }
}
