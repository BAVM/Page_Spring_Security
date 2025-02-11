package com.brian.springboot.app_1.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brian.springboot.app_1.models.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    public AppUser findByEmail (String email);

    
}
