package com.softuni.angelovestates.repository;

import com.softuni.angelovestates.model.entity.Province;
import com.softuni.angelovestates.model.enums.ProvinceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    Province getProvinceByProvince(ProvinceEnum province);
}
