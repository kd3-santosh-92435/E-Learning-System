package com.elearning.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.elearning.entity.Admin;
import com.elearning.entity.Instructor;
import com.elearning.entity.Student;
import com.elearning.repository.AdminRepository;
import com.elearning.repository.InstructorRepository;
import com.elearning.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return studentRepository.findByEmail(email)
                .map(s -> new CustomUserDetails(
                        s.getEmail(),
                        s.getPassword(),
                        "STUDENT"
                ))
                .orElseGet(() ->
                        instructorRepository.findByEmail(email)
                                .map(i -> new CustomUserDetails(
                                        i.getEmail(),
                                        i.getPassword(),
                                        "INSTRUCTOR"
                                ))
                                .orElseGet(() ->
                                        adminRepository.findByEmail(email)
                                                .map(a -> new CustomUserDetails(
                                                        a.getEmail(),
                                                        a.getPassword(),
                                                        "ADMIN"
                                                ))
                                                .orElseThrow(() ->
                                                        new UsernameNotFoundException(
                                                                "User not found: " + email
                                                        )
                                                )
                                )
                );
    }
}


