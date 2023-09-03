package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.TaiKhoanRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String taiKhoanName) throws UsernameNotFoundException {
        Optional<TaiKhoan> foundTK = taiKhoanRepository.findById(taiKhoanName);
        return new User(foundTK.get().getTaiKhoan(),foundTK.get().getMatKhau(),new ArrayList<>());
    }
}
