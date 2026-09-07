package staniszewska.licencjat_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.AdminUserDTO;
import staniszewska.licencjat_backend.models.UserUpdatedDetailsDTO;
import staniszewska.licencjat_backend.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<AdminUserDTO> getAdminUsers(int page, int size, String search, String sortField, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return userRepository.getAdminUsers(search, pageable);
    }

    public UserUpdatedDetailsDTO updateUserDetails(UserEntity currentUser, UserUpdatedDetailsDTO user) {
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!user.getEmail().equals(currentUser.getEmail()) && userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            currentUser.setEmail(user.getEmail());
        }

        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            currentUser.setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            currentUser.setLastName(user.getLastName());
        }

        userRepository.save(currentUser);

        UserUpdatedDetailsDTO updatedUser = new UserUpdatedDetailsDTO();
        updatedUser.setEmail(currentUser.getEmail());
        updatedUser.setFirstName(currentUser.getFirstName());
        updatedUser.setLastName(currentUser.getLastName());

        return updatedUser;
    }
}