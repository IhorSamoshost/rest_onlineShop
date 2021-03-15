package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CreateAccountDto;
import com.cursor.onlineshop.dtos.UserDto;
import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.User;
import com.cursor.onlineshop.repositories.AccountRepo;
import com.cursor.onlineshop.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;
    static AccessDeniedException ACCESS_DENIED = new AccessDeniedException("Access denied");
    static AccessDeniedException REGISTER_DENIED =
            new AccessDeniedException("Such username already exists in DB! Try enter any other username!");

    @Autowired
    public UserService(AccountRepo accountRepo, UserRepo userRepo, @Lazy BCryptPasswordEncoder encoder) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public UserDetails login(String userName, String password) throws AccessDeniedException {
        var account = accountRepo.findByUsername(userName).orElseThrow(() -> ACCESS_DENIED);
        if (!encoder.matches(password, account.getPassword())) throw ACCESS_DENIED;
        return account;
    }

    public UserDetails registerUser(CreateAccountDto createAccountDto) throws AccessDeniedException {
        if (accountRepo.findByUsername(createAccountDto.getUsername()).isPresent()) {
            throw REGISTER_DENIED;
        }
        Account newAccount = accountRepo.save(new Account(createAccountDto.getUsername(),
                encoder.encode(createAccountDto.getPassword()), createAccountDto.getEmail()));
        userRepo.save(new User(newAccount.getAccountId()));
        return newAccount;
    }

    public UserDetails registerWithRole(CreateAccountDto createAccountDto) throws AccessDeniedException {
        if (accountRepo.findByUsername(createAccountDto.getUsername()).isPresent()) {
            throw REGISTER_DENIED;
        }
        Account newAccount = accountRepo.save(new Account(createAccountDto.getUsername(),
                encoder.encode(createAccountDto.getPassword()), createAccountDto.getEmail(),
                createAccountDto.getPermissions()));
        userRepo.save(new User(newAccount.getAccountId()));
        return newAccount;
    }

    public Account getAccountById(String accountId) {
        return accountRepo.findById(accountId).orElseThrow();
    }

    public UserDto getUserInfoById(String accountId) {
        User foundUser = userRepo.findById(accountId).orElseThrow();
        Account foundAccount = accountRepo.findById(accountId).orElseThrow();
        return new UserDto(accountId, foundAccount.getUsername(), foundAccount.getPassword(),
                foundAccount.getEmail(), foundAccount.getPermissions(),
                foundUser.getFirstName(), foundUser.getLastName(),
                foundUser.getAge(), foundUser.getPhoneNumber());
    }

    public List<UserDto> getAll(int limit, int offset) {
        List<User> usersFromDb = userRepo.findAll();
        List<Account> accountsFromDb = accountRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User u : usersFromDb) {
            Account uAccount = null;
            for (Account a : accountsFromDb) {
                if (a.getAccountId().equals(u.getAccountId())) {
                    uAccount = a;
                    break;
                }
            }
            assert uAccount != null;
            UserDto userDto = new UserDto(uAccount.getAccountId(), uAccount.getUsername(),
                    uAccount.getPassword(), uAccount.getEmail(), uAccount.getPermissions(),
                    u.getFirstName(), u.getLastName(), u.getAge(), u.getPhoneNumber());
            userDtos.add(userDto);
        }
        List<UserDto> sortedDtoListForReturn = new ArrayList<>();
        List<UserDto> nonNullLastNameDtos = new ArrayList<>();
        List<UserDto> nonNullFirstNameDtos = new ArrayList<>();
        List<UserDto> nullBothNamesDtos = new ArrayList<>();
        for (UserDto uDto : userDtos) {
            if (uDto.getLastName() != null) {
                if (uDto.getFirstName() != null) {
                    sortedDtoListForReturn.add(uDto);
                } else {
                    nonNullLastNameDtos.add(uDto);
                }
            } else {
                if (uDto.getFirstName() != null) {
                    nonNullFirstNameDtos.add(uDto);
                } else {
                    nullBothNamesDtos.add(uDto);
                }
            }
        }
        sortedDtoListForReturn.sort(Comparator.comparing(UserDto::getLastName).thenComparing(UserDto::getFirstName));
        nonNullLastNameDtos.sort(Comparator.comparing(UserDto::getLastName));
        nonNullFirstNameDtos.sort(Comparator.comparing(UserDto::getFirstName));
        sortedDtoListForReturn.addAll(nonNullLastNameDtos);
        sortedDtoListForReturn.addAll(nonNullFirstNameDtos);
        sortedDtoListForReturn.addAll(nullBothNamesDtos);
        return sortedDtoListForReturn.stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    public UserDetails update(String accountId, UserDto editedUserDto, UserDto requestedUserDto) {
        editedUserDto.setAccountId(accountId);
        if (editedUserDto.getUsername() == null) {
            editedUserDto.setUsername(requestedUserDto.getUsername());
        }
        if (editedUserDto.getEmail() == null) {
            editedUserDto.setEmail(requestedUserDto.getEmail());
        }
        if (editedUserDto.getPermissions() == null) {
            editedUserDto.setPermissions(requestedUserDto.getPermissions());
        }
        if (editedUserDto.getFirstName() == null) {
            editedUserDto.setFirstName(requestedUserDto.getFirstName());
        }
        if (editedUserDto.getLastName() == null) {
            editedUserDto.setLastName(requestedUserDto.getLastName());
        }
        if (editedUserDto.getAge() == null) {
            editedUserDto.setAge(requestedUserDto.getAge());
        }
        if (editedUserDto.getPhoneNumber() == null) {
            editedUserDto.setPhoneNumber(requestedUserDto.getPhoneNumber());
        }
        Account editedAccount;
        if (editedUserDto.getPassword() == null) {
            editedAccount = accountRepo.save(new Account(editedUserDto.getAccountId(),
                    editedUserDto.getUsername(), requestedUserDto.getPassword(),
                    editedUserDto.getEmail(), editedUserDto.getPermissions()));
        } else {
            editedAccount = accountRepo.save(new Account(editedUserDto.getAccountId(),
                    editedUserDto.getUsername(), encoder.encode(editedUserDto.getPassword()),
                    editedUserDto.getEmail(), editedUserDto.getPermissions()));
        }
        userRepo.save(new User(editedUserDto.getAccountId(),
                editedUserDto.getFirstName(), editedUserDto.getLastName(),
                editedUserDto.getAge(), editedUserDto.getPhoneNumber()));
        return editedAccount;
    }

    public boolean delete(String deletedAccountId) {
        accountRepo.deleteById(deletedAccountId);
        userRepo.deleteById(deletedAccountId);
        return accountRepo.findById(deletedAccountId).isEmpty()
                || userRepo.findById(deletedAccountId).isEmpty();
    }

    public Account getByUsername(String userName) {
        return accountRepo.findByUsername(userName).orElseThrow();
    }

    public User getUserByAccount(Account account) {
        return userRepo.findById(account.getAccountId()).orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return accountRepo.findByUsername(userName).orElseThrow();
    }
}

