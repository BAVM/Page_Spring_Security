package com.brian.springboot.app_1.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brian.springboot.app_1.models.MyBookList;

@Repository
public interface MyBookRepository extends JpaRepository<MyBookList,Integer> {
}
