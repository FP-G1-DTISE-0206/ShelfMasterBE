package com.DTISE.ShelfMasterBE.usecase.user.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.RoleResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.usecase.user.GetUserUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetUserUsecaseImpl implements GetUserUsecase {
    private final UserRepository userRepository;

    public GetUserUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getUser(String email) {
        return userRepository.findByEmail(email).map(
                user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getUserName(),
                        user.getImageUrl(),
                        user.getRoles().stream().map(RoleResponse::new).collect(Collectors.toList())
                )).orElseThrow(
                () -> new DataNotFoundException("User not found")
        );
    }

    @Override
    public Page<UserResponse> getUsers(Pageable pageable, String search) {
        Optional<Sort.Order> roleSort = pageable.getSort()
                .stream()
                .filter(order -> order.getProperty().equalsIgnoreCase("role")) // Case-insensitive
                .findFirst();

        if (roleSort.isPresent()) {
            System.out.println("Sorting by roleName: " + roleSort.get().getDirection());

            List<User> users = userRepository.findUsersBySearch(search, Pageable.unpaged()).getContent();

            Comparator<User> roleComparator = Comparator.comparing(user -> user.getRoles().stream()
                    .map(Role::getName)
                    .sorted()
                    .findFirst()
                    .orElse("")
            );

            if (roleSort.get().getDirection().isDescending()) {
                roleComparator = roleComparator.reversed();
            }

            List<UserResponse> sortedUsers = users.stream()
                    .sorted(roleComparator)
                    .map(UserResponse::new)
                    .toList();

            // Paginate manually
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), sortedUsers.size());
            List<UserResponse> paginatedUsers = sortedUsers.subList(start, end);

            return new PageImpl<>(paginatedUsers, pageable, sortedUsers.size());
        }
        return userRepository.findUsersBySearch(search, pageable).map(UserResponse::new);
    }

}
