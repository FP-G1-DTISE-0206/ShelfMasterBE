package com.DTISE.ShelfMasterBE.usecase.admin;

import com.DTISE.ShelfMasterBE.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GetAdminsUsecase {
    Page<User> getAdmins(Pageable pageable, String search);
}
